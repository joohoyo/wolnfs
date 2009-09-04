package wolf.project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Fstest extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.fstest);
		
		TextView tv2 = new TextView(this);
		tv2.setText("hi");
		setContentView(tv2);
	}
}

