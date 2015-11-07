package edu.uic.cs478.kartikeypradhan;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


@SuppressLint("NewApi")
public class ImagePreviewer extends Fragment {

	
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
		
		return inflater.inflate(R.layout.image_previewer, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		MainActivity.setToast("Showing Selected Image!");
		
		Bundle mBundle = getActivity().getIntent().getBundleExtra(ImageDisplayer.mBundleName);
		
		Bitmap mBitmap = (Bitmap) mBundle.get(ImageDisplayer.mImageId);
		
		ImageView mImageView = (ImageView) getActivity().findViewById(R.id.imageView111);
		
		mImageView.setImageBitmap(mBitmap);
		
	}

	
	public void onPause() {
		super.onPause();
		
		MainActivity.setToast("Preview Images!");
		
	};
	

	
	
}
