package wolf.android;

import java.io.File;
import java.util.ArrayList;

import wolf.filesystem.FsUnit;
import wolf.project.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AndList extends ListActivity implements OnClickListener {
	private static final String tag = "AndList";

	//public
	public static final String DIR_PATH = "dir_path";
	public static String dirPath = "\\";

	//public static String fsList[] = "";
	public static int andDirCount = 0;
	public static ArrayList<String> arrayAndList = new ArrayList<String>();
	public static ArrayList<String> arrayAndFiles = new ArrayList<String>();

	//private
	private static final int ACTIVITY_DIR = 0;
	private EditText pathEdit = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.and_list);
		
		//버튼 리스너
		Button dirButton = (Button) findViewById(R.id.AND_Button_DIR);
		dirButton.setOnClickListener(this);

		//에디트 박스
		pathEdit = (EditText) findViewById(R.id.AND_EditText_DIR);
	}

	@Override
	protected void onResume() {
		super.onResume();
		pathEdit.setText(dirPath);
		requestDIR();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(tag,"position = " + position);
		if (position < andDirCount) {
			dirPath = dirPath + arrayAndList.get(position) + "\\";			
			onResume();
			return ;
		}		
	}

	void requestDIR() {
		Log.d(tag,"requestDIR");
	
		andDirCount = 0;
		arrayAndList.clear();
		arrayAndFiles.clear();
		
		File [] file = (new File(dirPath)).listFiles();		
		for(int i=0;i<file.length;i++) {
			if (file[i].isDirectory())
			{
				andDirCount ++;
				arrayAndList.add(file[i].getName());
			}			
			else 
				arrayAndFiles.add(file[i].getName());			
		}
		
		arrayAndList.addAll(arrayAndFiles);
		
		ArrayAdapter<String> adList = new ArrayAdapter<String>(this,                
				android.R.layout.simple_list_item_1, arrayAndList); 
		
		setListAdapter(adList);		
	}
	
	public void onClick(View v){
		switch(v.getId()) {
		case R.id.AND_Button_DIR:
			dirPath = pathEdit.getText().toString();			
			if (dirPath.charAt(dirPath.length()-1) != '\\') {
				dirPath = dirPath + '\\';
			}
			onResume();
			break;
		}	
	}
}
