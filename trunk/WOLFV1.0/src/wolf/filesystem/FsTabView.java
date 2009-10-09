package wolf.filesystem;

import wolf.project.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class FsTabView extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fs_tab);
		
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("tab1")
			   .setIndicator("SERVER", getResources().getDrawable(R.drawable.server))
			   .setContent(new Intent(this, FsList.class)));

        tabHost.addTab(tabHost.newTabSpec("tab2")
 			   .setIndicator("ANDROID", getResources().getDrawable(R.drawable.android))
               .setContent(new Intent(this, AndList.class)));

	}

}
