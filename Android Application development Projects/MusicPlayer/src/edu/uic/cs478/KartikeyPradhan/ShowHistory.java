package edu.uic.cs478.KartikeyPradhan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowHistory extends Activity {

	private Bundle getRecords;
	private ArrayList<String> records;
	private ListView lv;
	private ArrayAdapter<String> arrayAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_history);
		
		//get the intent to show the activity to display the music play history
		Intent intent = getIntent();
		
		//get the bundle objects
		getRecords = intent.getExtras();
		
		//set the object for the arraylist
		records = new ArrayList<String>();
		
		//check for !getRecords.getStringArrayList("ALL_ROWS").isEmpty()
		if(!getRecords.getStringArrayList("ALL_ROWS").isEmpty())
		{
			//get the rows
			records = getRecords.getStringArrayList("ALL_ROWS");
			
			//set the list view
			lv = (ListView) findViewById(R.id.history);
			
			//set the array adapter
			arrayAdapter = new ArrayAdapter<String>(this, R.layout.array_list,records );
			
			//set list view
		    lv.setAdapter(arrayAdapter);
			
		}
	
	}
	
}
