/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")savedInstanceState;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    
// Menu �κ� ����
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
 // Menu �κ� ��	
    
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
        Cursor c = mEventsCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, woltest.class);
        i.putExtra(EventsData.ROWID, id);
        i.putExtra(EventsData.IP_ADDRESS, c.getString(
                c.getColumnIndexOrThrow(EventsData.IP_ADDRESS)));
        i.putExtra(EventsData.MAC_ADDRESS, c.getString(
                c.getColumnIndexOrThrow(EventsData.MAC_ADDRESS)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
*/
}