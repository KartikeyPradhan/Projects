package com.example.contentproviderexample;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

	public static String TABLE_NAME ="test_table"; 
	public static String DB_NAME = "dbname";
	public static int VERSION = 1;
	public static String FIRST_NAME = "first_name";
	public static String LAST_NAME = "last_name";
	public static String _ID = "_id";
	public static String[] COLUMN_NAME = {_ID,FIRST_NAME, LAST_NAME};

	private String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
								+ " (" + _ID 
								+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ FIRST_NAME + " TEXT, "
								+ LAST_NAME	+ " TEXT "+
								") ";
	public static Context mContext;
	public ContentValues values = new ContentValues();
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		mContext = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(createTable);
		System.out.println();
		
		values.put(FIRST_NAME,"Kartikey");
		values.put(LAST_NAME, "Pradhan");
		db.insert(TABLE_NAME, null, values);
		values.clear();
		
		values.put(FIRST_NAME, "Kunal");
		values.put(LAST_NAME, "Thorvat");
		db.insert(TABLE_NAME, null, values);
		values.clear();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
