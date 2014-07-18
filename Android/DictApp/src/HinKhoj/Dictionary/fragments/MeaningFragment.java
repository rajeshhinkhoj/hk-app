package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.HinKhojLogger;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MeaningFragment extends DictionarySearchFragment {
	
	private OnPagerContentChangedListener mCallBack;
	private boolean isPageLoaded=false;
	private static final String ARG_POSITION = "position";
	private static final String ARG_SELECT_POSITION = "select_position";

	private int position;
	private int selectedPosition;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnPagerContentChangedListener)activity;
		} catch (Exception e) {
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		
		selectedPosition=getArguments().getInt(ARG_SELECT_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.online_dictionary_search_success, container, false);
	    LoadContent();
		UICommon.HideInputKeyBoard(view, getActivity());
		return view;
	}

	private void LoadContent() {
		
			try
			{
				initializeDictionaryFunctionalityLayout();
			}
			catch(Exception e)
			{
				DictCommon.LogException(e);
				Toast.makeText(view.getContext(), "Unable to load application.", Toast.LENGTH_LONG).show();
			}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && view !=null ) {
                 LoadContent();

		}
	}

	@Override
	public Context getFragmentContext()
	{
		return view.getContext();
	}

	private void  initializeDictionaryFunctionalityLayout(){

		super.initSearchView();
	}


	public static MeaningFragment newInstance(Object[] args) {
		MeaningFragment f = new MeaningFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]);
		b.putString(DictionarySearchFragment.SEARCH_WORD_KEY, (String)args[2]);
		b.putBoolean(ARG_TABLET, false);
		b.putBoolean(DictionarySearchFragment.IS_ONLINE, (Boolean)args[3]);
		f.setArguments(b);
		return f;
	}
}
