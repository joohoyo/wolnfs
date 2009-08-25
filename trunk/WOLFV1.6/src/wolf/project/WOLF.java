package wolf.project;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;



public class WOLF extends Activity implements OnClickListener {
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
    		startActivity(j);
    		break;

    	}
    	
     

    }
    
}