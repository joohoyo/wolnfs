package wolf.filesystem;

import java.util.ArrayList;
import wolf.project.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.TabActivity;

public class FsList extends ListActivity implements OnClickListener {
	private static final String tag = "FsList";
	
	//public
	public static final String DIR_PATH = "dir_path";
	public static String dirPath = "C:\\";
	public static String fsList = "";
	
	//private
	private static final int ACTIVITY_DIR = 0;	
	private FsUnit fsUnit = new FsUnit();
	private EditText pathEdit = null;

	//서버와 연결하기 - 연결하기 전에 서버의 부팅시간 고려
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);
			
	//버튼 리스너
		Button dirButton = (Button) findViewById(R.id.FS_Button_DIR);
		dirButton.setOnClickListener(this);
		Log.d(tag,"onCreate");
	
	//에디트 박스
		pathEdit = (EditText) findViewById(R.id.FS_DIR_EditText);
		pathEdit.setText(dirPath);
	}

	//onClick method : 경로만 보이게 설정 해 놓음 - Jp
	public void onClick(View v){
		switch(v.getId()) {
		case R.id.FS_Button_DIR:
			dirPath = pathEdit.getText().toString();
			break;
		}	
	}
	/*	
	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<String> Arr_fsList = new ArrayList<String>();
		Log.d(tag,"onResume");

		fsUnit.step(0); //init
		Log.d(tag,"step1");
		fsUnit.step(1); //request dir
		Log.d(tag,"after step1");
		
		Arr_fsList.add(fsList);
	
		ArrayAdapter<String> Ar_List = new ArrayAdapter<String>(this,                
		android.R.layout.simple_list_item_1, Arr_fsList); 
		setListAdapter(Ar_List);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1));

	}
		
		

 		TextView t = (TextView) findViewById(R.id.FS_TextView_DIR);
		t.setText(dirPath);
		//임시적인 위치와 임시적인 textview 출력
		fsUnit.step(0); //init
		Log.d(tag,"step1");
		fsUnit.step(1); //request dir
		Log.d(tag,"after step1");
		
		TextView tv = (TextView) findViewById(R.id.FS_TextView_DIR);
		Log.d("FSLIST",fsList);
		tv.setText(fsList);

	}
*/
}