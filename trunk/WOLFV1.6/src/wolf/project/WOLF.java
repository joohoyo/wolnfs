package wolf.project;

import wolf.filesystem.FsList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WOLF extends Activity implements OnClickListener {
	private static final int ACTIVITY_SELECTED = 0;
	private static final int ACTIVITY_WOL_COMPLETE = 1;

	private String selec_Ip_Address;
	private String selec_Mac_Address;
	private EventsData mDbHelper;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		View iplistButton = this.findViewById(R.id.iplist_button);
		iplistButton.setOnClickListener(this);
		View ipsetButton = this.findViewById(R.id.ipset_button);
		ipsetButton.setOnClickListener(this);
		View aboutButton = this.findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		
		//////////////////////////////////////////////////////nov9.4
		View fstestButton = this.findViewById(R.id.fstest_button);
		fstestButton.setOnClickListener(this);
		
	}

	public void onClick(View v){
		//ABOUT BUTTON Activity
		switch (v.getId()){
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
			
		case R.id.iplist_button:
			Intent j = new Intent(this, Data.class);
			startActivityForResult(j, ACTIVITY_SELECTED);
			break;
			
		case R.id.fstest_button:
			if (selec_Ip_Address != null){
				Intent i_woltest = new Intent(this, woltest.class);

				i_woltest.putExtra("selectIP", selec_Ip_Address);
				i_woltest.putExtra("selectMAC", selec_Mac_Address);
				startActivityForResult(i_woltest, ACTIVITY_WOL_COMPLETE);
			}
			else{
				Intent i_selec_warn = new Intent (this, selecIp.class);
				startActivity(i_selec_warn);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		TextView main_view_Ip = (TextView)findViewById(R.id.selected_Ip);

		Bundle extras = intent.getExtras();
		
		switch(requestCode) {
		case ACTIVITY_SELECTED:
			selec_Ip_Address = extras.getString(EventsData.IP_ADDRESS);
			selec_Mac_Address = extras.getString(EventsData.MAC_ADDRESS);
			main_view_Ip.setText(selec_Ip_Address);
			break;
			
		case ACTIVITY_WOL_COMPLETE:
			Intent Go_FS_intent = new Intent(this, wolf.filesystem.FsList.class);
			startActivity(Go_FS_intent);
			break;
		}
	}
}
