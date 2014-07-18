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


public class AntonymFragment extends Fragment  {

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

	public AntonymFragment(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private static String ARG_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dictionary_word_details_antonyms, container, false);
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


	public static AntonymFragment newInstance(int position) {
		AntonymFragment f = new AntonymFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}
	

	public void refreshView() {
		this.clearAllViews();
		DictResultData drd=DictCommon.dictResultData;
		TextView message_tv=(TextView)view.findViewById(R.id.message_tv);
		
		if(drd!=null)
		{


			if(!drd.IsHindi)
			{
					if(drd.eng_antonym_list!=null && drd.eng_antonym_list.size()>0)
					{
						showAntonyms(drd.eng_antonym_list);	
					}
					else
					{
						message_tv.setVisibility(View.VISIBLE);
					}


			}
			else
			{
					if(drd.hin_antonym_list!=null && drd.hin_antonym_list.size()>0)
					{
						showAntonyms(drd.hin_antonym_list);
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
		resetAntonyms();
		
	}

	private void showAntonyms(List<String> listItems)
	{
		ListView  lv = ( ListView)view.findViewById(R.id.antonyms_list);
		lv.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,listItems));
		
		lv.setVisibility(View.VISIBLE);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
	}


	private void resetAntonyms()
	{
		ListView listView = (ListView)view.findViewById(R.id.antonyms_list);
		TextView message_tv=(TextView)view.findViewById(R.id.message_tv);
		listView.setVisibility(View.GONE);
		message_tv.setVisibility(View.GONE);
	}

}
