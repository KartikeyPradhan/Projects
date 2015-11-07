package edu.uic.cs478.KartikeyPradhan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SongList extends ListFragment {
	private ListSelectionListener mListener = null;
	private int mCurrIdx = -1;
	
			public interface ListSelectionListener {
			public void onListSelection(int index);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			try {
				
				//setting the ListSelectionListener
				mListener = (ListSelectionListener) activity;
				
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setRetainInstance(true);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return super.onCreateView(inflater, container, savedInstanceState);

		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onActivityCreated(Bundle savedState) {
			super.onActivityCreated(savedState);

			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			//setting the correct layout for displaying the list view items
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.listitem, MainActivity.mSongList));

			if (-1 != mCurrIdx)
				getListView().setItemChecked(mCurrIdx, true);
		}
		
		// Called when the user selects an item from the List
		@Override
		public void onListItemClick(ListView l, View v, int pos, long id) {
			if (mCurrIdx != pos) {
				mCurrIdx = pos;
				// Inform the MainActivity that the item in position pos has been selected
				mListener.onListSelection(pos);
				mCurrIdx = -1;
				
			}
			// Indicates the selected item has been checked
			l.setItemChecked(mCurrIdx, true);
		}
}
