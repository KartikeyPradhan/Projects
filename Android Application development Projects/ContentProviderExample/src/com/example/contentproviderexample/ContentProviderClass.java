package com.example.contentproviderexample;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.net.Uri;


public class ContentProviderClass extends ContentProvider {

	public DatabaseHelper mdbHelper;
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		
		mdbHelper = new DatabaseHelper(getContext());
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		
		Cursor c1 = mdbHelper.getReadableDatabase().query(DatabaseHelper.TABLE_NAME,
														DatabaseHelper.COLUMN_NAME,
														null,
														null,
														null,
														null,
														null);
		
		
		return c1;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		
		long c1 = mdbHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_NAME, null, values);
		
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
