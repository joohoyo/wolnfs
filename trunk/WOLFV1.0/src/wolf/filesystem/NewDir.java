package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewDir extends Activity implements OnClickListener, Constant{
	private static final String tag = "FsDir";
	
	private EditText e = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag,"FsDir:line18:onCreate");
		
		setContentView(R.layout.fs_dir);
		
		e = (EditText) findViewById(R.id.FS_DIR_EditText);
		e.setText(new Unit().getServerPath());
		

		Button dirButton = (Button) findViewById(R.id.FS_DIR_Button);
		dirButton.setOnClickListener(this);
	}

	public void onClick(View v) {	
		switch(v.getId()) {
		case R.id.FS_DIR_Button:			
			Log.d(tag, "FsDir:line26");
			new Unit().setServerPath(e.getText().toString());			
			finish();
			break;
		}			
	}
}
