package com.example.contentresolverclass;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	public static String FIRST_NAME = "first_name";
	public static String LAST_NAME = "last_name";
	public static String[] COLUMN_NAME = {FIRST_NAME, LAST_NAME};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ContentResolver c1 = getContentResolver();
		Cursor mCursor;
		//ListView l1 = (ListView) findViewById(R.id.listView1);
		
		ContentValues values = new ContentValues();
		values.put(FIRST_NAME,"Rahul");
		values.put(LAST_NAME, "Vaidya");
		c1.insert(Uri.parse("content://test/test_table"), values);
		values.clear();
		
		values.put(FIRST_NAME,"Jay");
		values.put(LAST_NAME, "Dave");
		c1.insert(Uri.parse("content://test/test_table"), values);
		values.clear();
		
		
		mCursor = c1.query(Uri.parse("content://test/test_table"), COLUMN_NAME, null, null, null);
		
		SimpleCursorAdapter sca ;
		sca =  new SimpleCursorAdapter(this, R.layout.list_layout, 
											mCursor,
											COLUMN_NAME, 
											new int[] { R.id.firstName, R.id.lastName },
											0); 
		setListAdapter(sca);

	}

	
	
	
}
