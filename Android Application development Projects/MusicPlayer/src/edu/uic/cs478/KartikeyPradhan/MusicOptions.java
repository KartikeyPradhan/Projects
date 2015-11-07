package edu.uic.cs478.KartikeyPradhan;

import edu.uic.cs478.ServiceForMusic.MusicPlayer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MusicOptions extends Fragment {

	private MusicPlayer mPlayer;
	Button resume;
	Button stop;
	Button pause;
	public int mCurrIdx=-1;
	private MainActivity mActivity;
	private Button playHistory;
	private Bundle songHistoryRecords = new Bundle();
	
	//return the current index
	public int getShownIndex() {
		
		return mCurrIdx;
	}
	
	
	//Play the selected music
		public void playMusic(int newIndex) {
			
			//set correct index
			mCurrIdx = newIndex;
			
			//set the mPlayer object
			this.mPlayer = mActivity.getMediaPlayer();
			
			try {
				//play the music
				mPlayer.playMusic(mCurrIdx);
				pause.setEnabled(true);
				stop.setEnabled(true);
				mCurrIdx = -1;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		//pause the current played music
		public void pauseMusic() {
			
			try {
				//pause the music
				if(mPlayer != null)
				{
					mPlayer.pauseMusic();
					resume.setEnabled(true);
					pause.setEnabled(false);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		//resume the currently paused music
		public void resumeMusic()
		{
			try {
				if(mPlayer != null)
				{
					mPlayer.resumeMusic();
					resume.setEnabled(false);
					pause.setEnabled(true);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		//stop the played music
		public void stopMusic()
		{
			try {
				if(mPlayer != null)
				{
					mPlayer.stopMusic();
					resume.setEnabled(false);
					pause.setEnabled(false);
					stop.setEnabled(false);
					mCurrIdx = -1;
				}
				} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//show the history of the played music
		public void showMusicHistory()
		{
			try {
				//set the mPlayer object
				if(this.mPlayer == null)
				{
					this.mPlayer = mActivity.getMediaPlayer();
				}
				
				//get the bundle from the mPlayer object
				songHistoryRecords = mPlayer.getHistory();
				
				//set intent for starting new activity
				Intent mIntent = new Intent(getActivity(), ShowHistory.class);
				
				//put the bundle object with the intent
				mIntent.putExtras(songHistoryRecords);
				
				//start activity
				startActivity(mIntent);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain this Fragment across Activity reconfigurations
		setRetainInstance(true);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.music_option, container, false);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity= (MainActivity) getActivity();
		pause = (Button) getActivity().findViewById(R.id.pause);
		resume = (Button) getActivity().findViewById(R.id.resume);
		stop = (Button) getActivity().findViewById(R.id.stop);
		playHistory = (Button) getActivity().findViewById(R.id.history);
		resume.setEnabled(false);
		pause.setEnabled(false);
		stop.setEnabled(false);
		
		//set resume button listener
		resume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resumeMusic();
			}
		});
		
		//set pause button listener
		pause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pauseMusic();
			}
		});

		//set stop button listener
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMusic();
			}
		});
		
		//set playHistory button listener
		playHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					showMusicHistory();

			}
		});
		
		
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	
}
