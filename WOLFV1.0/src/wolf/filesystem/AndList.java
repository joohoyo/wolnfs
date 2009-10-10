package wolf.filesystem;

import java.util.ArrayList;
import wolf.project.R;
import wolf.project.woltest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AndList extends ListActivity implements OnClickListener, Constant {
	private static final String tag = "AndList";

	//public
	
	//public static String fsList[] = "";
	public static int andDirCount = 0;	
	public static ArrayList<String> arrayAndList = new ArrayList<String>();
	public static ArrayList<String> arrayAndFiles = new ArrayList<String>();

	
	//private
	private static final int ACTIVITY_DIR = 0;
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

		//에디트 박스
		pathEdit = (EditText) findViewById(R.id.AND_EditText_DIR);
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
		case CONTEXT_MENU_ITEM_PLAY:
			Intent i_Play = new Intent(this, FsPlay.class);
			// 경로값 Value로 넣어주삼
			//			i_Play.putExtra("Vfile_Path", Value );
			startActivity(i_Play);
		}
		return false;
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		pathEdit.setText(unit.getAndroidPath());
		unit.setTurn(AND);
		unit.step(STEP_SHOW_DIR);

		ArrayList<String> adList = new ArrayList<String>();
		adListAdapter adList_adpt = new adListAdapter(this, R.layout.list_row, adList); 
		setListAdapter(adList_adpt); 
		
		ListView con_List = getListView();

		con_List.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) { 
				AdapterView.AdapterContextMenuInfo info;
				try {
					info = (AdapterView.AdapterContextMenuInfo) menuInfo;
				} catch (ClassCastException e) {
					Log.e(tag, "bad menuInfo", e);
					return;
				}
				menu.setHeaderTitle("CONTEXT_MENU");

				menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0, R.string.delete);
				menu.add(0, CONTEXT_MENU_ITEM_SEND, 0, R.string.send);
				//파일명이 동영상 관련 끝나면 
				//if (파일명  == ".mp4" || )이런식?
				menu.add(0, CONTEXT_MENU_ITEM_PLAY, 0, R.string.play );
				
			}});

	}
	
	//fsList랑 여기랑 둘다 파일 이름이 표시가 안되삼
	private class adListAdapter extends ArrayAdapter<String> {
		private ArrayList<String> list_item;
		
		public adListAdapter (Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId, items); 
			this.list_item = items; 
		}		
        
        public View getView(int position, View convertView, ViewGroup parent) {
                View cView = convertView;
                if (cView == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    cView = vi.inflate(R.layout.list_row, null);
                }
                String po = list_item.get(position);
                if ( po != null) {
                        ImageView icon = (ImageView) cView.findViewById(R.id.file_image);
                        TextView name = (TextView) cView.findViewById(R.id.file_name);
                        if (icon != null){
                        	icon.setImageResource(R.drawable.folder);                           
                        }
                        if(name != null){
                        		name.setText(po);
                        }
                }
                return cView;
        }
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(tag,"position = " + position);
		if (position < andDirCount) {
			unit.setAndroidPath(unit.getAndroidPath() + arrayAndList.get(position));			
			onResume();
			return ;
		}		
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
					unit.setAndroidPath(unit.getAndroidPath() + editText.getText().toString());
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

	//onClick method : 경로만 보이게 설정 해 놓음
	public void onClick(View v){
		String tempAndroidPath = unit.getAndroidPath();
		
		switch(v.getId()) {
		case R.id.AND_Button_DIR:
			unit.setAndroidPath(tempAndroidPath);
			break;
		case R.id.AND_Button_DIRUP:
			int indexOfSlash = tempAndroidPath.lastIndexOf('/',tempAndroidPath.length()-2);			
			Log.d(tag,"indexofslash = " + indexOfSlash);
			if (indexOfSlash >= 0) {
				tempAndroidPath = tempAndroidPath.copyValueOf(tempAndroidPath.toCharArray(), 0, indexOfSlash+1);				
			}
			unit.setAndroidPath(tempAndroidPath);			
			break;				
		}
		onResume();
	}	

	public static void clear() {
		andDirCount = 0;	
		arrayAndList.clear();
		arrayAndFiles.clear();		
	}
}
