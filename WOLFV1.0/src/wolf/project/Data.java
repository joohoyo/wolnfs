package wolf.project;

import java.util.ArrayList;

import wolf.filesystem.FsPlay;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Data extends ListActivity {

	private static final String tag = "IPLIST EDIT";
	private static final int ACTIVITY_EDIT=1;
	private static final int ACTIVITY_CREATE=0;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int MODIFY_ID = Menu.FIRST + 2;
	private String listItemSeleted = null;
	public static ArrayList<String> arrayIpList = new ArrayList<String>();

	private EventsData mDbHelper;
	private Cursor mEventsCursor;

	private long mRowId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_list);
		mDbHelper = new EventsData(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	private void fillData() {
		Cursor notesCursor = mDbHelper.fetchAllNotes();
		startManagingCursor(notesCursor);

		String[] from = new String[]{EventsData.COMMENT};

		int[] to = new int[]{R.id.text1};

		SimpleCursorAdapter notes = 
			new SimpleCursorAdapter(this, R.layout.db_row, notesCursor, from, to);
		setListAdapter(notes);
	}

	//ContextMenu Ω√¿€ 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("CONTEXT_MENU");
		menu.add(0, INSERT_ID, 0, R.string.ip_insert);
		menu.add(0, DELETE_ID, 0, R.string.ip_delete);
		menu.add(0, MODIFY_ID, 0, R.string.ip_modify);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {		
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case INSERT_ID:
				createNote();
				return true;

			case DELETE_ID:
				mDbHelper.deleteNote(menuInfo.id);
				fillData();
				return true;

			case MODIFY_ID:
				modifyNote(menuInfo.id);
				return true;
		}
		return false;
	}	
	//ContextMenu ≥°

	private void createNote() {
		Intent i = new Intent(this, IPlistEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	private void modifyNote(long id) {
		Intent i = new Intent(this, IPlistEdit.class);
		i.putExtra(EventsData.ROWID, id);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent mIntent = new Intent();

		Cursor c = (Cursor) l.getItemAtPosition(position);
		mIntent.putExtra(EventsData.IP_ADDRESS, c.getString(2));
		mIntent.putExtra(EventsData.MAC_ADDRESS, c.getString(3));

		setResult(RESULT_OK, mIntent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK)fillData();
		else if (resultCode == RESULT_CANCELED); 

	}
}
