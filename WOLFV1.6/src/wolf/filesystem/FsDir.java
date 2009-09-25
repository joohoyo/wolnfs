package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FsDir extends Activity implements OnClickListener{
	private static final String tag = "FsDir";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_dir);

		Button dirButton = (Button) findViewById(R.id.FS_DIR_Button);
		dirButton.setOnClickListener(this);

	}

	public void onClick(View v) {	
		switch(v.getId()) {
		case R.id.FS_DIR_Button:			
			Log.d(tag, "FsDir:line26");
			Intent i = new Intent(this, FsList.class);
			//getIntent().putExtra(FsList.DIR_PATH, (CharSequence) "D:/");
			i.putExtra(FsList.DIR_PATH, (CharSequence) "D:/");
			
			setResult(RESULT_OK);
			finish();
			break;
		}			
	}
}
