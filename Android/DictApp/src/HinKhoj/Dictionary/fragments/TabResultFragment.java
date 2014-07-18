package HinKhoj.Dictionary.fragments;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.TabSearchFragmentLoadTaskAsync;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.MessageContent;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.DataModel.DictionaryWordData;
import HinKhoj.Dictionary.activity.WordDetailsActivity;
import HinKhoj.Dictionary.adapters.WordDetailAdapter;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



public class TabResultFragment extends Fragment  {

	private static final String ARG_SELECT_POSITION = "selectedTab";
	private View view;
	
	protected boolean isOnline=true;
	protected boolean isTablet=true;
	protected String searchWord="";
	protected ProgressBar searchProgressBar=null;
	protected TextView searchMessageTV=null;
	LinearLayout progress_ll=null;
	private OnPagerContentChangedListener mCallBack;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnPagerContentChangedListener)activity;
		} catch (Exception e) {
		}

	}

	public TabResultFragment(){
	
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b=getArguments();
		if(b!=null)
		{
			isTablet=b.getBoolean(DictionarySearchFragment.ARG_TABLET);
			searchWord="";
			searchWord=b.getString(DictionarySearchFragment.SEARCH_WORD_KEY);
			
			if(b.containsKey(DictionarySearchFragment.IS_ONLINE))
			{
				if(b.getBoolean(DictionarySearchFragment.IS_ONLINE))
				{
					isOnline=true;
				}
				else
				{
					isOnline=false;
				}
			}
		}

	}

	private static String ARG_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.search_progress, container, false);

		progress_ll=(LinearLayout)view.findViewById(R.id.progress_ll);
        progress_ll.setVisibility(View.VISIBLE);
		searchProgressBar=(ProgressBar)view.findViewById(R.id.searchProgressBar);
		searchMessageTV=(TextView)view.findViewById(R.id.search_message);
		searchMessageTV.setVisibility(View.VISIBLE);
		DictCommon.setHindiFont(getActivity(), searchMessageTV);
		if(isOnline)
		{
			searchMessageTV.setText(Html.fromHtml(String.format(MessageContent.ONLINE_SRC_MSG,searchWord,searchWord)));
		}
		else
		{
			searchMessageTV.setText(String.format(MessageContent.OFFLINE_SRC_MSG,searchWord,searchWord));
			
		}
        new TabSearchFragmentLoadTaskAsync(this).execute(new Void[]{});
		return view;
	}
	

	 

	public static TabResultFragment newInstance(Object[] args) {
		TabResultFragment f = new TabResultFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]);
		b.putString(DictionarySearchFragment.SEARCH_WORD_KEY, (String)args[2]);
		b.putBoolean(DictionarySearchFragment.ARG_TABLET, false);
		b.putBoolean(DictionarySearchFragment.IS_ONLINE, (Boolean)args[3]);
		f.setArguments(b);
		return f;
	}

	public void delayedInitialization() {
		// TODO Auto-generated method stub
		if(searchWord!=null && searchWord!="")
		{
			 Intent i= new Intent(getActivity(),WordDetailsActivity.class);
		      i.putExtra(DictionarySearchFragment.SEARCH_WORD_KEY, searchWord);
		      i.putExtra(DictionarySearchFragment.IS_ONLINE, isOnline);
		      getActivity().startActivity(i);
		}
	}

	

}
