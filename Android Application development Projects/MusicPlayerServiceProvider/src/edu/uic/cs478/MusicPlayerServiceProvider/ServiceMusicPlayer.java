package edu.uic.cs478.MusicPlayerServiceProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uic.cs478.ServiceForMusic.MusicPlayer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

@SuppressLint("SimpleDateFormat")
public class ServiceMusicPlayer extends Service
{
	//Database Helper object
	private DatabaseOpenHelper mDbHelper;
	
	//MusicPlayer object declaration
	private MediaPlayer mPlayer = null;
	
	//Creating an ArrayList for maintaining track numbers from the raw directory
	private ArrayList<Integer> trackNumber = new ArrayList<Integer>();
	
	//Flag set when music paused to enable the facility of stop music when music paused
	private int pauseFlag = 0;
	
	//To get system date in yyyy.MM.dd hh:mm:ss format
	private SimpleDateFormat dateTime= new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
	
	//variable to store sql statement
	private String sqlStatement;
	
	//variable to maintain track name
	private String trackName;
	
	//variable to maintain current request
	private String currentStatus;
	
	//variable to maintain old status of the music
	private String oldStatus = "First Time";
	
	//variable for date-time
	private String dTime;
	
	//ArrayList for displaying the play history when get history button pressed
	private ArrayList<String> playHistory;
	
	//intent for broadcast
	private Intent mIntent;
	
	
	// Implement the Stub for this Object
			private final MusicPlayer.Stub mBinder = new MusicPlayer.Stub()
			{
				
				
				//Play the music
				@SuppressLint("NewApi")
				@Override
				public void playMusic(int trackNumber) throws RemoteException {
					// TODO Auto-generated method stub
					
					//set track name
					trackName = "Track "+ (trackNumber + 1);
					
					//set date-time
					dTime = dateTime.format(new Date());
					
					//set current status
					currentStatus = "Play";
					
					//insert the record in database
					insertSongPlayHistory("\""+dTime.split(" ")[0]+ "\"", "\""+ dTime.split(" ")[1]+ "\"", "\""+trackName+"\"","\""+ currentStatus+"\"", "\""+oldStatus+"\"");
					
					//set old status
					oldStatus = trackName +" "+currentStatus;								
					
					//release old music
					if(mPlayer!= null)
					{
						if(mPlayer.isPlaying())
						{
							mPlayer.release();
							mPlayer = null;
						}
					}
					
					//setting the track
					setTrack(trackNumber);
					
					//starting the music
					mPlayer.start();
								
					if (null != mPlayer) {
						
						//no looping
						mPlayer.setLooping(false);

						// Stop Service when music has finished playing
						mPlayer.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								
								//Broadcast the intent 
								sendBroadcast(mIntent);
								
							}
						});
					
					
				}


				}

				//Pause music
				@Override
				public void pauseMusic() throws RemoteException {
					// TODO Auto-generated method stub
					
					//check if music is playing
					if(mPlayer.isPlaying())
					{
						mPlayer.pause();
						pauseFlag = 1;
					}
					
					
					dTime = dateTime.format(new Date());
					currentStatus = "Pause";
					
					//insert record in the database
					insertSongPlayHistory("\""+dTime.split(" ")[0]+ "\"", "\""+ dTime.split(" ")[1]+ "\"", "\""+trackName+"\"","\""+ currentStatus+"\"", "\""+oldStatus+"\"");
					oldStatus = trackName +" "+currentStatus;
					
					}
				
				//stop music
				@Override
				public void stopMusic() throws RemoteException {
					// TODO Auto-generated method stub
					if(mPlayer != null)
					{
						//check if the music is playing or paused
						if(mPlayer.isPlaying()|| pauseFlag==1)
						{
							mPlayer.stop();
							mPlayer.release();
							mPlayer = null;
						}
					}
					
					dTime = dateTime.format(new Date());
					currentStatus = "Stop";
					insertSongPlayHistory("\""+dTime.split(" ")[0]+ "\"", "\""+ dTime.split(" ")[1]+ "\"", "\""+trackName+"\"","\""+currentStatus+"\"", "\""+oldStatus+"\"");
					oldStatus = trackName +" "+currentStatus;
				}
				
				//resume the song
				@Override
				public void resumeMusic() throws RemoteException {
					// TODO Auto-generated method stub
					
					//restart music
					mPlayer.start();
					pauseFlag = 0;
					dTime = dateTime.format(new Date());
					currentStatus = "Resume";
					
					//insert record in the database
					insertSongPlayHistory("\""+dTime.split(" ")[0]+ "\"", "\""+ dTime.split(" ")[1]+ "\"", "\""+trackName+"\"","\""+currentStatus+"\"", "\""+oldStatus+"\"");
					oldStatus = trackName +" "+currentStatus;
					
				}

				//get the history of played music from the database
				@Override
				public Bundle getHistory() throws RemoteException {
					// TODO Auto-generated method stub

					playHistory = new ArrayList<String>();
					
					//cursor for all records in the database
					Cursor selectCursor =  mDbHelper.getWritableDatabase().query(
													DatabaseOpenHelper.TABLE_NAME,//tableName
													DatabaseOpenHelper.columns,//Columns to be selected
													null,//Where clause
													null,//selectionArgs
													null,//groupBy
													null,//having
													null//orderBy
													);
					
					if(selectCursor != null)
						selectCursor.moveToFirst();
					
					//loop to add records in the array list
					while(selectCursor.isAfterLast() != true)
					{
						playHistory.add(""
										+ selectCursor.getInt(0) + "\t"
										+ selectCursor.getString(1) + "\t"
										+ selectCursor.getString(2) + "\t"
										+ selectCursor.getString(3) + "\t"
										+ selectCursor.getString(4) + "\t "
										+ selectCursor.getString(5));
						selectCursor.moveToNext();
					}
					
					//bundle for passing the records from service to client
					Bundle allRows = new Bundle();
					allRows.putStringArrayList("ALL_ROWS", playHistory);
					
					
					return allRows;
				}
				
			};
			
			//function to set the track to be played
			public void setTrack(int trackNumber)
			{
				mPlayer = MediaPlayer.create(this, this.trackNumber.get(trackNumber));
				
			}
			
			//function to insert the records in the database
			@SuppressWarnings("static-access")
			public void insertSongPlayHistory(String date, String time, String songName, String currentRequest, String oldStatus)
			{
				
				
				sqlStatement = "insert into " + mDbHelper.TABLE_NAME 
								+ "("+ mDbHelper.play_date + ","
								+ mDbHelper.play_time +","
								+ mDbHelper.play_song_name + ","
								+ mDbHelper.play_current_request + ","
								+ mDbHelper.play_old_status
								+")"
								+" values(" 
								+ date + ","
								+ time+ ","
								+ songName + ","
								+ currentRequest + ","
								+ oldStatus
								+")";		
				
				mDbHelper.getWritableDatabase().execSQL(sqlStatement);
				
			}
			
						
			@Override
			public IBinder onBind(Intent intent) {
				// TODO Auto-generated method stub
				
				//add the track in the arraylist
				trackNumber.add(R.raw.lockedoutofheaven);
				trackNumber.add(R.raw.dontyouworrychild);
				trackNumber.add(R.raw.tonightweareyoung);
				trackNumber.add(R.raw.unconditionally);
				trackNumber.add(R.raw.test);
				
				//set the intent to be broadcast
				mIntent = new Intent("MUSIC_COMPLETED");
				
				//set database instance
				mDbHelper = new DatabaseOpenHelper(this);
				
				return mBinder;
			}
}
