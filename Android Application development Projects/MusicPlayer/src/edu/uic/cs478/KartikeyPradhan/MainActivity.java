package edu.uic.cs478.KartikeyPradhan;

import edu.uic.cs478.KartikeyPradhan.SongList.ListSelectionListener;
import edu.uic.cs478.ServiceForMusic.MusicPlayer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

public class MainActivity extends Activity implements ListSelectionListener
{
	//object for the music player
	private MusicPlayer mPlayer;
	private boolean mIsBound;
	
	//
	public static String[] mSongList;
	public static MusicOptions mMusicOptions;
	 /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.main);
        mMusicOptions = (MusicOptions) getFragmentManager().findFragmentById(R.id.playSong);   
        mSongList = getResources().getStringArray(R.array.songList);
       
    }        
    
	@Override
	public void onListSelection(int index) {
		// TODO Auto-generated method stub
		if (mMusicOptions.getShownIndex() != index) {
			
			// Play the song selected
			mMusicOptions.playMusic(index);
		}
	}    
    
    
   @Override
	protected void onResume() {
		super.onResume();
		
		//bounding the service
		if (!mIsBound) {

			Intent intent = new Intent(MusicPlayer.class.getName());
			bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);
			
		}
	}
    
 	@Override
 	protected void onDestroy() {
 		super.onDestroy();
 		
 		//stopping the music service
 		mMusicOptions.stopMusic();
 		
 		//unbound the service
 		if (mIsBound) {
 			
 			unbindService(this.mConnection);

 		}
 		
 	}

 	private final ServiceConnection mConnection = new ServiceConnection() {

 		public void onServiceConnected(ComponentName className, IBinder iservice) {
			
 			mPlayer =  MusicPlayer.Stub.asInterface(iservice);

 			mIsBound = true;
 		
 		}

 		public void onServiceDisconnected(ComponentName className) {

 			mPlayer = null;

 			mIsBound = false;
 			
 		}
 	};


 	public MusicPlayer getMediaPlayer()
 	{
 		return mPlayer;
 	}
    
}
