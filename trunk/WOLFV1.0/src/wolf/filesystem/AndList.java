package wolf.filesystem;

import java.util.ArrayList;

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
	protected void onResume() {
		super.onResume();
		pathEdit.setText(unit.getAndroidPath());
		unit.step(STEP_SHOW_DIR);

		ArrayAdapter<String> adList = new ArrayAdapter<String>(this,                 
				android.R.layout.simple_list_item_1, arrayAndList);  

		setListAdapter(adList);          

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(tag,"position = " + position);
		if (position < andDirCount) {
			unit.setAndroidPath(unit.getAndroidPath() + arrayAndList.get(position));			
			onResume();
			return ;
		}		
	}

	//onClick method : 경로만 보이게 설정 해 놓음
	public void onClick(View v){
		String tempAndroidPath = pathEdit.getText().toString();
		
		switch(v.getId()) {
		case R.id.AND_Button_DIR:
			unit.setAndroidPath(tempAndroidPath);
			break;
		case R.id.AND_Button_DIRUP:
			int indexOfSlash = tempAndroidPath.lastIndexOf('\\',tempAndroidPath.length()-2);			
			Log.d(tag,"indexofslash = " + indexOfSlash);
			if (indexOfSlash > 0) {
				tempAndroidPath = tempAndroidPath.copyValueOf(tempAndroidPath.toCharArray(), 0, indexOfSlash+1);				
			}
			unit.setServerPath(tempAndroidPath);			
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
