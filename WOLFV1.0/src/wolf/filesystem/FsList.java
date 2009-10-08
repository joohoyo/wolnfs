package wolf.filesystem;

import java.util.ArrayList;

import wolf.project.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class FsList extends ListActivity implements OnClickListener, Constant {
	private static final String tag = "FsList";

	//public

	//public static final String DIR_PATH = "dir_path";
	public static String fileName = "";
	//public static String fsList[] = "";
	public static int fsDirCount = 0;
	public static ArrayList<String> arrayFsList = new ArrayList<String>();
	public static ArrayList<String> arrayFiles = new ArrayList<String>();

	//private
	//private static final int ACTIVITY_DIR = 0;	
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

		//에디트 박스
		pathEdit = (EditText) findViewById(R.id.FS_EditText_DIR);		
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
		Log.d(tag,"position = " + position);		
		if (position < fsDirCount) {  // TODO : 디렉토리 나타내는 표식을 해야 할 듯			
			unit.setServerPath(unit.getServerPath() + arrayFsList.get(position));			
			onResume();
			return ;
		}		
	}

	// item을 누르고 있을 때의 메뉴
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {

		Log.d(tag, "onCreateContextMenu");
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e(tag, "bad menuInfo", e);
			return;
		}
		
		/*
        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
        	Log.d(tag, "null");
            return;
        }
		 */
		// Setup the menu header
		menu.setHeaderTitle("CONTEXT_MENU");

		menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0, R.string.delete);
		menu.add(0, CONTEXT_MENU_ITEM_SEND, 0, R.string.send);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		super.onContextItemSelected(item);

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_DELETE:             	
			showDialog(DIALOG_CREATE_DIR);
			return true;
		case CONTEXT_MENU_ITEM_SEND: //TODO : 젭라..
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag,"onResume");		
		pathEdit.setText(unit.getServerPath());
		unit.setTurn(FS);
		unit.step(STEP_REQUEST_DIR); //request dir
		Log.d(tag,"after step1");

		ArrayAdapter<String> adList = new ArrayAdapter<String>(this,                
				android.R.layout.simple_list_item_1, arrayFsList); 
		setListAdapter(adList);

		Log.d(tag,"" + arrayFsList.size() + unit.getServerPath());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		unit.step(STEP_CLOSE); //끝내기 - 소켓 닫기		
	}

	// Menu 부분 시작
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CREATE_DIR, 0, R.string.create_dir);

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
			View view = inflater.inflate(R.layout.create_dir, null);
			final EditText editText = (EditText) view.findViewById(R.id.EditText_NEWDIR);
			editText.setText("");
			return new AlertDialog.Builder(this)
			.setTitle(R.string.create_dir).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				@Override
				public void onClick(DialogInterface dialog, int which) {							
					unit.setServerPath(unit.getServerPath() + editText.getText().toString());
					unit.step(STEP_CREATE_DIR);
					onResume();
					Log.d(tag,"alert dialog");							
				}
			})
			.setNegativeButton(android.R.string.cancel, 
					new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing						
				}					
			}).create();
			//case DIALOG_DELETE_DIR:


		}

		return super.onCreateDialog(id);
	}
	// Menu 부분 끝	

	public static void clear() {
		fsDirCount = 0;
		arrayFsList.clear();
		arrayFiles.clear();
	}
}