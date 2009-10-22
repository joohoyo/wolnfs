package wolf.filesystem;

import java.util.ArrayList;
import wolf.project.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class FsList extends ListActivity implements OnClickListener, OnCreateContextMenuListener, Constant {
	private static final String tag = "FsList";

	//public
	public static int fsDirCount = 0;
	public static ArrayList<String> arrayFsList = new ArrayList<String>();
	public static ArrayList<String> arrayFiles = new ArrayList<String>();
	public static String listItemSelected = null;
	
	//private
	private Unit unit = new Unit();
	private EditText pathEdit = null;
	
	
	
	//서버와 연결하기 - 연결하기 전에 서버의 부팅시간 고려
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);		

		clear();		

		//임시적인 위치와 임시적인 textview 출력
		unit.step(STEP_INIT); //init

		// TODO : 서버와 연결하기 - 연결하기 전에 서버의 부팅시간 고려

		//버튼 리스너
		Button dirButton = (Button) findViewById(R.id.FS_Button_DIR);
		dirButton.setOnClickListener(this);

		Button upButton = (Button) findViewById(R.id.FS_Button_DIRUP);
		upButton.setOnClickListener(this);
		
		//ContextMenu(LongClick) Listener
		registerForContextMenu(getListView());
		

		//에디트 박스
		pathEdit = (EditText) findViewById(R.id.FS_EditText_DIR);		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag,"onResume");		
		pathEdit.setText(unit.getServerPath());
		unit.setTurn(FS);
		unit.step(STEP_REQUEST_DIR); //request dir
		Log.d(tag,"after step1");
	
		//목록 갱신
		adListAdapter adList_adpt = new adListAdapter(this, R.layout.list_row, arrayFsList);		
		setListAdapter(adList_adpt);
		
		Log.d(tag,"" + arrayFsList.size() + unit.getServerPath());
	}
	
	//onClick method : 경로만 보이게 설정 해 놓음 
	public void onClick(View v){
		String tempServerPath = pathEdit.getText().toString();

		switch(v.getId()) {
		case R.id.FS_Button_DIR:			
			unit.setServerPath(tempServerPath);			
			break;
		case R.id.FS_Button_DIRUP:			
			int indexOfSlash = tempServerPath.lastIndexOf('\\',tempServerPath.length()-2);			
			Log.d(tag,"indexofslash = " + indexOfSlash);
			if (indexOfSlash > 0) {
				tempServerPath = tempServerPath.copyValueOf(tempServerPath.toCharArray(), 0, indexOfSlash+1);				
			}
			unit.setServerPath(tempServerPath);			
			break;			
		}	
		onResume();
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(tag,"position = " + position);		
		if (position < fsDirCount) {  // TODO : 디렉토리 나타내는 표식을 해야 할 듯			
			unit.setServerPath(unit.getServerPath() + arrayFsList.get(position));			
			onResume();
			return ;
		}		
	}

	//ContextMenu 시작 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e(tag, "bad menuInfo", e);
			return;
		}
		
		int po = info.position;
		listItemSelected = arrayFsList.get(po);
		
		menu.setHeaderTitle("CONTEXT_MENU");

		menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0, R.string.context_menu_delete);
		if (po >= fsDirCount) //파일만 카피
		{
			menu.add(0, CONTEXT_MENU_ITEM_COPY, 0, R.string.context_menu_copy);
			//unit.setServerFile(arrayFsList.get(po));
		}
		else
		{
			;
			//unit.setServerFile("");		
		}
		//play		
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		//AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();			
		
		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_DELETE:             	//TODO : delete도 해야됨
			showDialog(DIALOG_DELETE);
			return true;
		case CONTEXT_MENU_ITEM_COPY: //TODO : 젭라..
			showDialog(DIALOG_COPY);
			return true;
		}
		return false;
	}
	//ContextMenu 끝

	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		unit.step(STEP_CLOSE); //끝내기 - 소켓 닫기		
	}

	// Menu 부분 시작
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CREATE_DIR, 0, R.string.menu_create_dir);

		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {		
		case MENU_CREATE_DIR:
			showDialog(DIALOG_CREATE_DIR);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch(id) {
		case DIALOG_CREATE_DIR:
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.menu_create_dir, null);
			final EditText editText = (EditText) view.findViewById(R.id.Dialog_EditText_NEWDIR);
			editText.setText("");			
			
			return new AlertDialog.Builder(this)
			.setTitle(R.string.menu_create_dir).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				//@Override
				public void onClick(DialogInterface dialog, int which) {							
					unit.setServerPath(unit.getServerPath() + editText.getText().toString());
					unit.step(STEP_CREATE_DIR);
					editText.setText("");
					onResume();
					Log.d(tag,"alert dialog");							
				}
			})
			.setNegativeButton(android.R.string.cancel, 
					new android.content.DialogInterface.OnClickListener() {
				//@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing						
				}					
			}).create();

		case DIALOG_DELETE:
			inflater = LayoutInflater.from(this);
			view = inflater.inflate(R.layout.menu_delete, null);
			TextView textView = (TextView) view.findViewById(R.id.Dialog_TextView_delete);
			//textView.setText(unit.getServerPath() + listItemSeleted);
			
			
			return new AlertDialog.Builder(this)
			.setTitle(R.string.context_menu_delete).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				//@Override
				public void onClick(DialogInterface dialog, int which) {							
					unit.setServerPath(unit.getServerPath() + listItemSelected);
					unit.step(STEP_DELETE);
					onResume();
					Log.d(tag,"alert dialog");							
				}
			})
			.setNegativeButton(android.R.string.cancel, 
					new android.content.DialogInterface.OnClickListener() {
				//@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing						
				}					
			}).create();
			
		case DIALOG_COPY:
			inflater = LayoutInflater.from(this);
			view = inflater.inflate(R.layout.menu_copy, null);
			TextView tvCopyFrom = (TextView) view.findViewById(R.id.Dialog_TextView_copyfrom);
			TextView tvCopyTo = (TextView) view.findViewById(R.id.Dialog_TextView_copyto);
			/*
			tvCopyFrom.setText(unit.getServerPath() + listItemSeleted);
			tvCopyTo.setText(unit.getAndroidPath());
			*/
			return new AlertDialog.Builder(this)
			.setTitle(R.string.context_menu_copy).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				//@Override
				public void onClick(DialogInterface dialog, int which) {							
					//unit.setServerPath(unit.getServerPath() + listItemSelected);
					unit.step(STEP_COPY_FROM_SERVER);
					onResume();
					Log.d(tag,"copy alert dialog");							
				}
			})
			.setNegativeButton(android.R.string.cancel, 
					new android.content.DialogInterface.OnClickListener() {
				//@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing						
				}					
			}).create();
			
			

		}
		
		return null;
		//return super.onCreateDialog(id);
	}
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		
		switch (id) {
		case DIALOG_CREATE_DIR:
			EditText et = (EditText) dialog.findViewById(R.id.Dialog_EditText_NEWDIR);
			et.setText("");
			break;

		case DIALOG_DELETE:
			TextView textView = (TextView) dialog.findViewById(R.id.Dialog_TextView_delete);
			textView.setText(unit.getServerPath() + listItemSelected);			
			break;			
		case DIALOG_COPY:
			TextView tvCopyFrom = (TextView) dialog.findViewById(R.id.Dialog_TextView_copyfrom);
			TextView tvCopyTo = (TextView) dialog.findViewById(R.id.Dialog_TextView_copyto);
			tvCopyFrom.setText(unit.getServerPath() + listItemSelected);
			tvCopyTo.setText(unit.getAndroidPath());			
			break;
		}
	}
	// Menu 부분 끝	

	public static void clear() {
		fsDirCount = 0;
		arrayFsList.clear();
		arrayFiles.clear();
	}
}