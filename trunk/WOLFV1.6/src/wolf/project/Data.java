package wolf.project;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Data extends ListActivity {
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_CREATE=0;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int MODIFY_ID = Menu.FIRST + 2;
    
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
    
// Menu 부분 시작
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.ip_insert);
        menu.add(0, DELETE_ID, 0, R.string.ip_delete);
        menu.add(0, MODIFY_ID, 0, R.string.ip_modify);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
            
        case DELETE_ID:
    		mDbHelper.deleteNote(getListView().getSelectedItemId());
    		fillData();
    		return true;
    		
        case MODIFY_ID:
        	modifyNote(getListView().getSelectedItemId());
        	return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
 // Menu 부분 끝	
    
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
    	/*
    	Cursor c = mDbHelper.fetchNote(id);
        c.moveToPosition(position);
        startManagingCursor(c);
        
    	Bundle bundle = new Bundle();
        
        bundle.putString(EventsData.IP_ADDRESS, c.getString(
                c.getColumnIndexOrThrow(EventsData.IP_ADDRESS)));
        bundle.putString(EventsData.MAC_ADDRESS, c.getString(
                c.getColumnIndexOrThrow(EventsData.MAC_ADDRESS)));
        bundle.putLong(EventsData.ROWID, id);
        */
    	Intent mIntent = new Intent();
    	
    	
    	Cursor c = (Cursor) l.getItemAtPosition(position);
    	mIntent.putExtra(EventsData.IP_ADDRESS, c.getString(2));
        
        setResult(RESULT_OK, mIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
