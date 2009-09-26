package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FsList extends Activity implements OnClickListener {
	private static final String tag = "FsList";
	
	//public
	public static final String DIR_PATH = "dir_path";
	public static String dirPath = "C:\\";
	
	//private
	private static final int ACTIVITY_DIR = 0;
	private FsUnit fsUnit = new FsUnit();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);

		//서버와 연결하기 - 연결하기 전에 서버의 부팅시간 고려
		
		fsUnit.step(0); //init
		Log.d(tag,"step1");
		fsUnit.step(1); //request dir
		Log.d(tag,"after step1");
		//버튼 리스너
		Button dirButton = (Button) findViewById(R.id.FS_Button_DIR);
		dirButton.setOnClickListener(this);
		Log.d(tag,"onCreate");
	}		

	public void onClick(View v){
		switch(v.getId()) {
		case R.id.FS_Button_DIR:
			Intent i = new Intent(this, FsDir.class);
			startActivity(i);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag,"onResume");
		TextView t = (TextView) findViewById(R.id.FS_TextView_DIR);
		t.setText(dirPath);
	}
}