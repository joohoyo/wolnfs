package wolf.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EventsData {

	public static final String TABLE_NAME = "iplist";
	public static final String ROWID = "_id";	
	public static final String COMMENT = "commnet";
	public static final String IP_ADDRESS = "ipadd";
	public static final String MAC_ADDRESS = "macadd";

	private static final String TAG = "EventsData";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE =
		("CREATE TABLE " 
				+ TABLE_NAME + " (" 
				+ ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ MAC_ADDRESS + " TEXT NOT NULL," 
				+ IP_ADDRESS + " TEXT NOT NULL," 
				+ COMMENT + " TEXT NOT NULL);");

	private static final String DATABASE_NAME = "iplist.db";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	public EventsData(Context ctx) {
		this.mCtx = ctx;
	}

	public EventsData open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}
	/*
	 *  public static final String COMMENT = "commnet";
    public static final String IP_ADDRESS = "ipadd";
    public static final String MAC_ADDRESS = "macadd";
	 */
	public long createNote(String comment, String ipadd, String macadd) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COMMENT, comment);
		initialValues.put(IP_ADDRESS, ipadd);
		initialValues.put(MAC_ADDRESS, macadd);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	public boolean deleteNote(long rowId) {

		return mDb.delete(TABLE_NAME, ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllNotes() {

		return mDb.query(TABLE_NAME, new String[] {ROWID, COMMENT, IP_ADDRESS
				, MAC_ADDRESS}, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException if note could not be found/retrieved
	 */
	public Cursor fetchNote(long rowId) throws SQLException {

		Cursor mCursor =
			mDb.query(true, TABLE_NAME, new String[] {ROWID, COMMENT, IP_ADDRESS
					, MAC_ADDRESS}, ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId id of note to update
	 * @param title value to set note title to
	 * @param body value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNote(long rowId, String comment, String ipadd, String macadd) {
		ContentValues args = new ContentValues();
		args.put(COMMENT, comment);
		args.put(IP_ADDRESS, ipadd);
		args.put(MAC_ADDRESS, macadd);

		return mDb.update(TABLE_NAME, args, ROWID + "=" + rowId, null) > 0;
	}
}
