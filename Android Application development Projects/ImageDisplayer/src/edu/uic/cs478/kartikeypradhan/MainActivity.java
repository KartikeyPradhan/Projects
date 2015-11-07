package edu.uic.cs478.kartikeypradhan;


import edu.uic.cs478.kartikeypradhan.ImageCategory.ListSelectionListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements ListSelectionListener
{
	
	public static String[] mImageCategory;
	public static String[] mImage;
	public ImageDisplayer mImageDisplay;
	private Button showToast;
	private static String toastMsg = "Idle!";
	
    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       
        //Checking for the any existing fragment: Base case check.
        if(getFragmentManager().findFragmentByTag("IMAGE_PREVIEWER") == null
        		&& getFragmentManager().findFragmentByTag("IMAGE_DISPLAYER") == null)
        {
        	getFragmentManager().beginTransaction().add(R.id.imageDisplayer, new ImageDisplayer(), "IMAGE_DISPLAYER").commit();
        	getFragmentManager().executePendingTransactions();
        	
        	mImageDisplay = (ImageDisplayer) getFragmentManager().findFragmentById(R.id.imageDisplayer);
        }
        
        //setup of the content view
        setContentView(R.layout.main);
        
        //category list for user selection
        mImageCategory = getResources().getStringArray(R.array.category);

        //Button setup
        showToast = (Button) findViewById(R.id.button1);
        
        //Button click action setup
        showToast.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
        
    }
    
    //Toast message setup
    public static void setToast(String msg)
    {
    	toastMsg = msg;
    }
    
    
    //List selection activity management.
	@SuppressLint("NewApi")
	@Override
	public void onListSelection(int index) {
		// TODO Auto-generated method stub
		
		//check if the fragment object is not initialized
		if(mImageDisplay == null||getFragmentManager().findFragmentByTag("IMAGE_PREVIEWER")!=null)
		{	
			
				//replacing the frame layout with respective thumbnail fragment
				getFragmentManager().beginTransaction().replace(R.id.imageDisplayer, new ImageDisplayer()).commit();
	        	
				//executing all pending transaction
				getFragmentManager().executePendingTransactions();
							
        	//fragment object initialization
        	mImageDisplay = (ImageDisplayer) getFragmentManager().findFragmentById(R.id.imageDisplayer);
		}
		
		if (mImageDisplay.getShownIndex() != index) {
			
			// get the images for the selected category
			mImageDisplay.showImageAtIndex(index);
		}
		
	}
}
