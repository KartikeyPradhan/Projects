package edu.uic.cs478.kartikeypradhan;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;



@SuppressLint("NewApi")
public class ImageDisplayer extends Fragment {

	//Object for the table layout
	private TableLayout mTableObj;
	
	//Variable to check the current selection
	public int mCurrIdx=-1;
	
	//Array to hold the URL strings
	private String[] mUrlLink;
	
	//ArrayList for bitmap images
	private ArrayList<Bitmap> bitmapImages;
	
	//Object for Asyntask extending class
	private GetImagesFromNet getImageFromNetObj;
	
	//Hashmap to store the images with respect to corresponding ImageView
	private static HashMap<String, Bitmap> mImageMap = new HashMap<String, Bitmap>();
	
	//String variable to store the image ID, will be used to extract image from hashmap
	public static String mImageId;
	
	//String variable used for passing the intent
	public static String mBundleName = "Bundle_Name";
	
	//return the current pointer/selected category
	public int getShownIndex() {
		
		return mCurrIdx;
	}
	
	//Display the images in the fragment
	public void showImageAtIndex(int newIndex) {
		
		mCurrIdx = newIndex;

		if (mCurrIdx == 0)
		{
			mUrlLink = getResources().getStringArray(R.array.cars);
		}
		else if (mCurrIdx == 1)
		{
			mUrlLink = getResources().getStringArray(R.array.flowers);
		}
		else if (mCurrIdx == 2)
		{
			mUrlLink = getResources().getStringArray(R.array.animals);

		}
		else 
			return;
	
		//Instantiating getImageFromNetObj object
		getImageFromNetObj = new GetImagesFromNet();
		
		//execute image download on Asyntask thread
		getImageFromNetObj.execute(mUrlLink);
	
	}

	
	//class for performing task in non UI thread i.e AsynTask thread
	public class GetImagesFromNet extends AsyncTask<String, Integer, ArrayList<Bitmap>>
	{
		
		//All computation done in this function
		@Override
		protected ArrayList<Bitmap> doInBackground(String... strings) {
			// TODO Auto-generated method stub
			URL aUrl = null ; 
			ArrayList<Bitmap> result= new ArrayList<Bitmap>();
			for(int i = 0;i<strings.length;i++)
			{
				try {
					MainActivity.setToast("Downloading Images!");
					aUrl = new URL(strings[i]) ; 
					
					//this bitmap arraylist will be used on storing the images
					result.add(BitmapFactory.decodeStream((InputStream) aUrl.getContent()));
					
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				} ;
				
			
			}
			
			//storing the bitmap images in an array, to be used on orientation change to initialize the ImageView
			bitmapImages = result;
			return result;
		}
		
			
		
		// This method is executed in the UI thread after doInBackground() has returned
		protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
			
			//Setup of the toast message
			MainActivity.setToast("Preview Images!");
			
			//this method call will initialize all ImageView with images
			setImagesOnView(bitmaps);
		}
		
		

		
	}
	
	
	//this method is responsible for initializing all the ImageView's with images
	public void setImagesOnView(ArrayList<Bitmap> result)
	{
		//counter to maintain the proper traverse for bitmap arraylist
		int count = 0;
		
		//loop for table layout children
		for(int i = 0;i<mTableObj.getChildCount();i++)
		{
			//loop for tablerow children
			TableRow mTableRowObj = (TableRow) mTableObj.getChildAt(i);
			
			//loop for imageview 
			for(int j = 0; j < mTableRowObj.getChildCount() ; j++)
			{
				//imageView object to contain the image
				ImageView mImageObj = (ImageView) mTableRowObj.getChildAt(j);
				
				//setting the image for the imageview
				mImageObj.setImageBitmap(result.get(count));
				
				//initializing the hashmap
				ImageDisplayer.createMapForImages(mImageObj.getId()+"", result.get(count));
				
				//external counter to select correct image from the ArrayList<Bitmap>
				count++;
			}
		}
		
		
	}
	
	//initializing the hashmap with key,value pair ==> ImageView ID, Bitmap Image
	public static void createMapForImages(String string, Bitmap value)
	{
		mImageMap.put(string+"", value);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Retain this Fragment across Activity reconfigurations
		setRetainInstance(true);
	
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		return inflater.inflate(R.layout.image_display, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mTableObj = (TableLayout) getActivity().findViewById(R.id.tableLayout1);
		
		if(bitmapImages == null)
		{
			bitmapImages = new ArrayList<Bitmap>();
		}
		else
		{
			if(bitmapImages.size()>0)
			setImagesOnView(bitmapImages);
		}
		
		//loop for table layout children		
		for(int i = 0;i<mTableObj.getChildCount();i++)
		{
			//loop for tablerow children
			TableRow mTableRowObj = (TableRow) mTableObj.getChildAt(i);
			
			for(int j = 0; j < mTableRowObj.getChildCount() ; j++)
			{
				//loop for imageview 
				ImageView mImageView = (ImageView) mTableRowObj.getChildAt(j);
				
				//imageView object to contain the image
				mImageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						//converting view to image view
						ImageView img = (ImageView) v;
						
						//initializing the key value
						mImageId = img.getId()+"";
						
						//initializing a bundle object
						Bundle imgBundle = new Bundle();
						
						//putting a parcelable object
						imgBundle.putParcelable(mImageId, (Parcelable) mImageMap.get(mImageId));
						
						//initializing intent
						Intent i = getActivity().getIntent();
						
						//putting extra to pass bundle object
						i.putExtra(mBundleName,imgBundle);
						
						//creating dynamic fragment
						FragmentManager mFragmentManager = getActivity().getFragmentManager();
						FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
						mFragmentTransaction.replace(R.id.imageDisplayer, new ImagePreviewer(), "IMAGE_PREVIEWER");
						mFragmentTransaction.addToBackStack(null);
						mFragmentTransaction.commit();	
					}
				});
			}
		}
	}
}


