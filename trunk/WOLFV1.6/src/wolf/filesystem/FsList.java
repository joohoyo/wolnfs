package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FsList extends Activity {
	/** Called when the activity is first created. */  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);		

		Button dirButton = (Button) findViewById(R.id.FS_DIR_button);

		dirButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Context c = FsList.this;
				Intent i = new Intent(c.getApplicationContext(), FsDir.class);
				startActivity(i);
			}
		});

	}
}

/* 이렇게 하면 왜 안될까
 
public class FsList extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_list);
	}		
		
	public void onClick(View v){
		switch(v.getId()) {
		case R.id.FS_DIR_button:
			Intent i = new Intent(this, FsDir.class);
			startActivity(i);
			break;
		}
	}
}
 */
 

