package HinKhoj.Dictionary.fragments;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.DataModel.DictionaryWordData;
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



public class DefinitionFragment extends Fragment  {

	private View view;
	private DictionaryWordData dwd=null;
	private boolean isHindi=false;
	private TextView main_tv=null;
	private TextView meaning_tv=null;
	private boolean isPageLoaded=false;
	
	private OnWordSelectedFromSearchSuccess mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnWordSelectedFromSearchSuccess)activity;
		} catch (Exception e) {
		}

	}

	public DefinitionFragment(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private static String ARG_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dictionary_word_details_definition, container, false);
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

	public static DefinitionFragment newInstance(int position) {
		DefinitionFragment f = new DefinitionFragment();
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


			isHindi=drd.IsHindi;
			
			if(!drd.IsHindi)
			{
		
					if(drd.eng2eng_list!=null && drd.eng2eng_list.size()>0)
					{
						List<String> defMeaning=new ArrayList<String>(drd.eng2eng_list.size());
						for(DictionaryWordData dwd: drd.eng2eng_list)
						{
							defMeaning.add(dwd.hin_word);
						}
						showDefinition(defMeaning);	 
					}
					else
					{
						message_tv.setVisibility(View.VISIBLE);
					}

				
			}
			else
			{
					if(drd.hin2hin_list!=null && drd.hin2hin_list.size()>0)
					{
						List<String> defMeaning=new ArrayList<String>(drd.hin2hin_list.size());
						for(DictionaryWordData dwd: drd.hin2hin_list)
						{
							defMeaning.add(dwd.eng_word);
						}
						showDefinition(defMeaning);	 
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
		resetDefinition();
	}


	private void showDefinition(List<String> listItems)
	{
		ListView lv = (ListView)view.findViewById(R.id.definition_list);
		lv.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,listItems));
		//lv.setExpanded(true);
		TextView heading=(TextView)view.findViewById(R.id.definition);
		heading.setVisibility(View.VISIBLE);
		lv.setVisibility(View.VISIBLE);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		//Utils.updateListViewHeight(lv);
	}



	private void resetDefinition()
	{
		ListView listView = (ListView)view.findViewById(R.id.definition_list);
		TextView heading=(TextView)view.findViewById(R.id.definition);
		listView.setVisibility(View.GONE);
		heading.setVisibility(View.GONE);
		TextView message_tv=(TextView)view.findViewById(R.id.message_tv);
		message_tv.setVisibility(View.GONE);
	}

}
