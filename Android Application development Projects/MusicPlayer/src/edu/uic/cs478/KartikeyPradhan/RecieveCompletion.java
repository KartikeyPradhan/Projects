package edu.uic.cs478.KartikeyPradhan;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RecieveCompletion extends BroadcastReceiver {

	
	
	@SuppressLint("ShowToast")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String mAction = intent.getAction();
		if(mAction.equals("MUSIC_COMPLETED"))
		{
			//Toast message to be displayed
			Toast.makeText(context, "Track Completed!", Toast.LENGTH_LONG).show();
			
			//Disabling the buttons
			MainActivity.mMusicOptions.pause.setEnabled(false);
			MainActivity.mMusicOptions.stop.setEnabled(false);
			
		}
	}

}
