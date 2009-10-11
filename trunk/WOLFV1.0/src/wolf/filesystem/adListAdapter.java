package wolf.filesystem;

import java.util.ArrayList;

import wolf.project.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adListAdapter extends ArrayAdapter<String> implements Constant{
	private ArrayList<String> list_item;
	private Context mContext; 

	public adListAdapter (Context context, int textViewResourceId, ArrayList<String> items) {
		super(context, textViewResourceId, items); 
		this.list_item = items; 
		this.mContext = context;		
	}		

	// List에 표시 부분 설정
	public View getView(int position, View convertView, ViewGroup parent) {		
		View cView = convertView;
		if (cView == null) {			
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
			cView = vi.inflate(R.layout.list_row, null, true);
		}
		String po = list_item.get(position);
		if ( po != null) {
			ImageView icon = (ImageView) cView.findViewById(R.id.file_image);
			TextView name = (TextView) cView.findViewById(R.id.file_name);

			if (icon != null){
				if (position < (new Unit().getTurn() == FS ? FsList.fsDirCount : AndList.andDirCount))
					icon.setImageResource(R.drawable.icon_dir);
				else
					icon.setImageResource(R.drawable.icon_file);
			}
			if(name != null){
				name.setText(po);
			}
		}		
		return cView;		
	}
}