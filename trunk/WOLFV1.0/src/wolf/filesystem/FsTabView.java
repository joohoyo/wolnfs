package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

public class FsTabView extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("tab1")
			   .setIndicator("ANDROID")
			   .setContent(R.id.textview1));

        tabHost.addTab(tabHost.newTabSpec("tab2")
 			   .setIndicator("SERVER")
			   .setContent(new Intent(this, FsList.class)));


	}

}
