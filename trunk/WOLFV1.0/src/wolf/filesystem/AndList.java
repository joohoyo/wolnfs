package wolf.filesystem;

import java.util.ArrayList;

import wolf.project.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AndList extends ListActivity implements OnClickListener, OnCreateContextMenuListener, Constant {
	//public
	public static int andDirCount = 0;	
	public static ArrayList<String> arrayAndList = new ArrayList<String>();
	public static ArrayList<String> arrayAndFiles = new ArrayList<String>();
	public static String listItemSelected = null;
	
	//private
	private EditText pathEdit = null;
	private Unit unit = new Unit();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.and_list);
		
		clear();
		
		//버튼 리스너
		Button dirButton = (Button) findViewById(R.id.AND_Button_DIR);
		dirButton.setOnClickListener(this);

		Button upButton = (Button) findViewById(R.id.AND_Button_DIRUP);
		upButton.setOnClickListener(this);
		
		//ContextMenu(LongClick) Listener
		registerForContextMenu(getListView());
		
		//에디트 박스
		pathEdit = (EditText) findViewById(R.id.AND_EditText_DIR);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		pathEdit.setText(unit.getAndroidPath());
		unit.setTurn(AND);
		unit.step(STEP_SHOW_DIR);

		
		//목록 갱신
		adListAdapter adList_adpt = new adListAdapter(this, R.layout.list_row, arrayAndList);
		setListAdapter(adList_adpt);		

	}
	
	//ContextMenu 시작 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
			
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {			
			return;
		}
		menu.setHeaderTitle("CONTEXT_MENU");

		menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0, R.string.context_menu_delete);
		menu.add(0, CONTEXT_MENU_ITEM_COPY, 0, R.string.context_menu_copy);
		menu.add(0, CONTEXT_MENU_ITEM_PLAY, 0, R.string.context_menu_play);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {		
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		listItemSelected = arrayAndList.get(menuInfo.position);
				
		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_DELETE:             	
			showDialog(DIALOG_DELETE);
			return true;
		case CONTEXT_MENU_ITEM_COPY: //TODO : 젭라..
			showDialog(DIALOG_COPY);
			return true;
		case CONTEXT_MENU_ITEM_PLAY:
			if (listItemSelected.endsWith(".mp4")==true){
				Intent i_Play = new Intent(this, FsPlay.class);
				i_Play.putExtra("Vfile_Path", unit.getAndroidPath()+ listItemSelected);
				startActivity(i_Play);
			}
			else{ 
				showDialog(DIALOG_ERROR_PLAY);	
			}
				
		}
		return false;
	}	
	//ContextMenu 끝
	
	

	
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
					unit.setAndroidPath(unit.getAndroidPath() + editText.getText().toString());
					unit.step(STEP_CREATE_DIR);
					onResume();												
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
			
			textView.setText(unit.getAndroidPath() + listItemSelected);
			
			return new AlertDialog.Builder(this)
			.setTitle(R.string.context_menu_delete).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				//@Override
				public void onClick(DialogInterface dialog, int which) {							
					unit.setAndroidPath(unit.getAndroidPath() + listItemSelected);
					unit.step(STEP_DELETE);
					onResume();												
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
					//unit.setAndroidPath(unit.getAndroidPath() + listItemSelected);
					unit.step(STEP_COPY_FROM_ANDROID);					
					onResume();												
				}
			})
			.setNegativeButton(android.R.string.cancel, 
					new android.content.DialogInterface.OnClickListener() {
				//@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing						
				}					
			}).create();
		
		case DIALOG_ERROR_PLAY:
			AlertDialog.Builder playError = new AlertDialog.Builder(this);
            playError.setTitle("PLAY ERROR");
            playError.setMessage("This File can't Play");
            return playError.create();
		}
		return super.onCreateDialog(id);
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
			textView.setText(unit.getAndroidPath() + listItemSelected);			
			break;			
		case DIALOG_COPY:
			TextView tvCopyFrom = (TextView) dialog.findViewById(R.id.Dialog_TextView_copyfrom);
			TextView tvCopyTo = (TextView) dialog.findViewById(R.id.Dialog_TextView_copyto);
			tvCopyFrom.setText(unit.getAndroidPath() + listItemSelected);
			tvCopyTo.setText(unit.getServerPath());			
			break;
		}
	}

	// Menu 부분 끝	

	//onClick method : 경로만 보이게 설정 해 놓음
	public void onClick(View v){
		String tempAndroidPath = unit.getAndroidPath();
		
		switch(v.getId()) {
		case R.id.AND_Button_DIR:
			unit.setAndroidPath(tempAndroidPath);
			break;
		case R.id.AND_Button_DIRUP:
			int indexOfSlash = tempAndroidPath.lastIndexOf('/',tempAndroidPath.length()-2);			
			if (indexOfSlash >= 0) {
				tempAndroidPath = tempAndroidPath.copyValueOf(tempAndroidPath.toCharArray(), 0, indexOfSlash+1);				
			}
			unit.setAndroidPath(tempAndroidPath);			
			break;				
		}
		onResume();
	}	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);		
		if (position < andDirCount) {
			unit.setAndroidPath(unit.getAndroidPath() + arrayAndList.get(position));			
			onResume();
			return ;
		}		
	}

	public static void clear() {
		andDirCount = 0;	
		arrayAndList.clear();
		arrayAndFiles.clear();		
	}
}
