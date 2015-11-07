package edu.uic.cs478.kartikeypradhan;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ImageCategory extends ListFragment {

	private ListSelectionListener mListener = null;
	private int mCurrIdx = -1;
	
			public interface ListSelectionListener {
			public void onListSelection(int index);
		}

		@SuppressLint("NewApi")
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			try {
				
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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return super.onCreateView(inflater, container, savedInstanceState);

		}

		@Override
		public void onActivityCreated(Bundle savedState) {
			super.onActivityCreated(savedState);

			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			setListAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.item_list, MainActivity.mImageCategory));

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

				
			}
			// Indicates the selected item has been checked
			l.setItemChecked(mCurrIdx, true);
		}


}
