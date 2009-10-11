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
	
	
	//������ �����ϱ� - �����ϱ� ���� ������ ���ýð� ���
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);		

		clear();		

		//�ӽ����� ��ġ�� �ӽ����� textview ���
		unit.step(STEP_INIT); //init

		// TODO : ������ �����ϱ� - �����ϱ� ���� ������ ���ýð� ���

		//��ư ������
		Button dirButton = (Button) findViewById(R.id.FS_Button_DIR);
		dirButton.setOnClickListener(this);

		Button upButton = (Button) findViewById(R.id.FS_Button_DIRUP);
		upButton.setOnClickListener(this);
		
		//ContextMenu(LongClick) Listener
		registerForContextMenu(getListView());
		

		//����Ʈ �ڽ�
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
	
		//��� ����
		adListAdapter adList_adpt = new adListAdapter(this, R.layout.list_row, arrayFsList);		
		setListAdapter(adList_adpt);
		
		Log.d(tag,"" + arrayFsList.size() + unit.getServerPath());
	}
	
	//onClick method : ��θ� ���̰� ���� �� ���� 
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
		if (position < fsDirCount) {  // TODO : ���丮 ��Ÿ���� ǥ���� �ؾ� �� ��			
			unit.setServerPath(unit.getServerPath() + arrayFsList.get(position));			
			onResume();
			return ;
		}		
	}
	



	//ContextMenu ���� 
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
		menu.setHeaderTitle("CONTEXT_MENU");

		menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0, R.string.context_menu_delete);
		menu.add(0, CONTEXT_MENU_ITEM_COPY, 0, R.string.context_menu_copy);		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		
		
			

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_DELETE:             	//TODO : delete�� �ؾߵ�
			showDialog(DIALOG_DELETE);
			return true;
		case CONTEXT_MENU_ITEM_COPY: //TODO : ����..
			unit.copyFile();
			return true;
		}
		return false;
	}
	//ContextMenu ��

	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		unit.step(STEP_CLOSE); //������ - ���� �ݱ�		
	}

	// Menu �κ� ����
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
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = null;

		
		switch(id) {
		case DIALOG_CREATE_DIR:
			view = inflater.inflate(R.layout.create_dir, null);
			final EditText editText = (EditText) view.findViewById(R.id.Dialog_EditText_NEWDIR);
			editText.setText("");			
			
			return new AlertDialog.Builder(this)
			.setTitle(R.string.menu_create_dir).setView(view)
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

		case DIALOG_DELETE:
			view = inflater.inflate(R.layout.delete, null);
			TextView textView = (TextView) view.findViewById(R.id.Dialog_TextView_delete);
			
			ListView l = getListView();
			final String strSeleted = arrayFsList.get(l.getSelectedItemPosition());
			
			textView.setText(unit.getServerPath() + strSeleted);
			

			return new AlertDialog.Builder(this)
			.setTitle(R.string.menu_create_dir).setView(view)
			.setPositiveButton(android.R.string.ok, 
					new android.content.DialogInterface.OnClickListener() {						
				@Override
				public void onClick(DialogInterface dialog, int which) {							
					unit.setServerPath(unit.getServerPath() + strSeleted);
					unit.step(STEP_DELETE);
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
			

		}

		return super.onCreateDialog(id);
	}
	// Menu �κ� ��	

	public static void clear() {
		fsDirCount = 0;
		arrayFsList.clear();
		arrayFiles.clear();
	}
}