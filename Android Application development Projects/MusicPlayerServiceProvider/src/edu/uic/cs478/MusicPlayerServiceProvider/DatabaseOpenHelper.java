package edu.uic.cs478.MusicPlayerServiceProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	//Defining static strings for table name and columns
	final static String TABLE_NAME = "Play_History";
	final static String play_date = "date";
	final static String play_time = "time";
	final static String play_song_name = "song_name";
	final static String play_current_request = "current_status";
	final static String play_old_status = "old_status";
	final static String _ID = "_id";
	final static String[] columns = { _ID, play_date, play_time, play_song_name, play_current_request, play_old_status};

	//Create table query 
	final private static String CREATE_CMD =

	"CREATE TABLE IF NOT EXISTS "+TABLE_NAME +" (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,  "
			+ play_date + " TEXT NOT NULL,"
			+ play_time + " TEXT NOT NULL,"
			+ play_song_name + " TEXT NOT NULL,"
			+ play_current_request + " TEXT NOT NULL,"
			+ play_old_status + " TEXT DEFAULT UNPLAYED"
					+ ")";

	//Database name
	final private static String db_Name = "Song_History";
	
	//Database Version
	final private static Integer VERSION = 1;
	
	//Context for Database
	final private Context mContext;

	//Creating the Database
	public DatabaseOpenHelper(Context context) {
		super(context, db_Name, null, VERSION);
		this.mContext = context;
	}

	//Creating the table in the Database
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(db_Name);
	}
}
