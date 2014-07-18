package HinKhoj.Dictionary.fragments;

import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.adapters.WordDetailAdapter;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
public class SynonymFragment extends Fragment  {

	private View view;
	private OnWordSelectedFromSearchSuccess mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnWordSelectedFromSearchSuccess)activity;
		} catch (Exception e) {
		}

	}

	public SynonymFragment(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	private static String ARG_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dictionary_word_details_synonyms, container, false);
         refreshView();
		return view;
	}

	   @Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			super.setUserVisibleHint(isVisibleToUser);
			if(view!=null)
			{
				refreshView();
			}
		}



	public static SynonymFragment newInstance(int position) {
		SynonymFragment f = new SynonymFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	public void refreshView() {
		this.clearAllViews();
		TextView message_tv=(TextView)view.findViewById(R.id.message_tv);
		
		DictResultData drd=DictCommon.dictResultData;
		if(drd!=null)
		{


			if(!drd.IsHindi)
			{
	
					if(drd.eng_synonym_list!=null && drd.eng_synonym_list.size()>0)
					{
						showSynonyms(drd.eng_synonym_list);
					}
					else
					{
						message_tv.setVisibility(View.VISIBLE);
					}
					
			}
			else
			{
					if(drd.hin_synonym_list!=null && drd.hin_synonym_list.size()>0)
					{
						showSynonyms(drd.hin_synonym_list);	 
					}
					else
					{
						message_tv.setVisibility(View.VISIBLE);
					}
			}	

		}
		else
		{
			message_tv.setVisibility(View.VISIBLE);
		}
	}

	private void clearAllViews() {
		// TODO Auto-generated method stub
		resetSynonyms();
	}

	private void showSynonyms(List<String> synonymList)
	{
		ListView  synonymsList = ( ListView )view.findViewById(R.id.synonyms_list);
		synonymsList.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,synonymList));
		synonymsList.setVisibility(View.VISIBLE);
		synonymsList.setDivider(getResources().getDrawable(R.drawable.list_divider));
		//Utils.updateListViewHeight(synonymsList);
	}

	private void resetSynonyms()
	{
		ListView synonymsList = (ListView)view.findViewById(R.id.synonyms_list);	   
		synonymsList.setVisibility(View.GONE);
		TextView message_tv=(TextView)view.findViewById(R.id.message_tv);
		message_tv.setVisibility(View.GONE);
	}
}
