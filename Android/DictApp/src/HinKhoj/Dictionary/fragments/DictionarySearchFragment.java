package HinKhoj.Dictionary.fragments;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.drive.internal.k;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.DictSearchFragmentLoadTaskAsync;
import HinKhoj.Dictionary.AsyncTasks.DictSearchTask;
import HinKhoj.Dictionary.AsyncTasks.OfflineDictSearchTask;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.HinKhojLogger;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.Constants.MessageContent;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.DataModel.DictionaryWordData;
import HinKhoj.Dictionary.UIData.StringIdRowItem;
import HinKhoj.Dictionary.activity.WordDetailsActivity;
import HinKhoj.Dictionary.adapters.MeaningListAdapter;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DictionarySearchFragment extends Fragment{

	protected Map<String,List<StringIdRowItem>> exactMeaningMap=null;
	public static String SEARCH_WORD_KEY="searchWord";
	protected List<StringIdRowItem> nearbyMeaning=null;
	protected List<StringIdRowItem> defMeaning=null;
	protected boolean isOnline=true;
	protected boolean isTablet=true;
	protected String searchWord="";
	protected View view;
	protected ProgressBar searchProgressBar=null;
	protected TextView searchMessageTV=null;
	public static final int ONLINE_REQUEST_CODE = 1234;
	public static final int OFFLINE_REQUEST_CODE = 4321;
	protected static final String ARG_TABLET = "tablet";
	LinearLayout search_success=null;
	LinearLayout progress_ll=null;
	private OnWordSelectedFromSearchSuccess mCallBack;
	ListView meaningLV=null;

	public interface OnWordSelectedFromSearchSuccess{

		public void onWordClick(int meaing_id,String word);
		public void onWordSpeak(String word);
		public void hideHelp();
	}

	public interface OnWordSelectedFromMobileSearchSuccess{

		public void onWordClick(int meaing_id,String word);
		public void onWordSpeak(String word);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnWordSelectedFromSearchSuccess)activity;
		} catch (Exception e) {
		}

	}

	/* initialize */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b=getArguments();
		if(b!=null)
		{
			isTablet=b.getBoolean(ARG_TABLET);
			position=b.getInt(ARG_POSITION);
			searchWord="";
			searchWord=b.getString(SEARCH_WORD_KEY);

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
		else if(savedInstanceState!=null)
		{
			searchWord=savedInstanceState.getString(SEARCH_WORD_KEY);
		}
		else 
		{
			Intent i=getActivity().getIntent();
			if(i!=null)
			{
				searchWord=i.getStringExtra(SEARCH_WORD_KEY);
				isOnline=i.getBooleanExtra(IS_ONLINE, false);
				if(getResources().getBoolean(R.bool.large_layout))
				{
					isTablet=true;
				}
			}
		}
		exactMeaningMap= new HashMap<String,List<StringIdRowItem>>();
		nearbyMeaning= new ArrayList<StringIdRowItem>();
		defMeaning= new ArrayList<StringIdRowItem>();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.online_dictionary_search_success, container, false);
		initSearchView();
		return view;
	}

	protected void initSearchView()
	{
		if(view!=null)
		{
			HinKhojLogger.Log("Loading search view: searchWord=="+searchWord);
			meaningLV=(ListView)view.findViewById(R.id.grammar_meaning_list);
			
			progress_ll=(LinearLayout)view.findViewById(R.id.progress_ll);

			searchProgressBar=(ProgressBar)view.findViewById(R.id.searchProgressBar);
			searchMessageTV=(TextView)view.findViewById(R.id.search_message);
			searchMessageTV.setVisibility(View.GONE);
			DictCommon.setHindiFont(getFragmentContext(), searchMessageTV);

			new DictSearchFragmentLoadTaskAsync(this).execute(new Void[]{});

		}


	}

	public void delayedInitialization() {
		
		if(searchWord!=null && searchWord!="")
		{
			HinKhojLogger.Log("Search word inside search fragment is :"+searchWord);
		    launchWordSearch(searchWord);
		}
	}

	public abstract class RightDrawableOnTouchListener implements OnTouchListener {
		Drawable drawable;
		private int fuzz = 10;

		/**
		 * @param keyword
		 */
		public RightDrawableOnTouchListener(TextView view) {
			super();
			final Drawable[] drawables = view.getCompoundDrawables();
			if (drawables != null && drawables.length == 4)
				this.drawable = drawables[2];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(final View v, final MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
				final int x = (int) event.getX();
				final int y = (int) event.getY();
				final Rect bounds = drawable.getBounds();
				if (x >= (v.getWidth() - bounds.width() - fuzz) && x <= (v.getWidth() - v.getPaddingRight() + fuzz)
						&& y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
					return onDrawableTouch(event);
				}
			}
			return false;
		}

		public abstract boolean onDrawableTouch(final MotionEvent event);

	}
	public Context getFragmentContext()
	{
		return view.getContext();
	}

	/* UI handlers */
	OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

			if(!isTablet)
			{
				/*
				StringIdRowItem entry= (StringIdRowItem) parent.getAdapter().getItem(position);
				Intent intent=new Intent(getActivity(),WordDetailsMobileActivity.class);
				intent.putExtra("meaning_id", entry.getRId());
				intent.putExtra("word", entry.getText());
				startActivity(intent);
				*/
				StringIdRowItem entry= (StringIdRowItem) parent.getAdapter().getItem(position);
				showWordDetails(entry);
			}
			else
			{
				StringIdRowItem entry= (StringIdRowItem) parent.getAdapter().getItem(position);
				showWordDetails(entry);
			}

		}
	};

	private void showWordDetails(StringIdRowItem entry) {
		// TODO Auto-generated method stub

		if(mCallBack==null)
		{
			mCallBack=(OnWordSelectedFromSearchSuccess) getActivity();
		}
		mCallBack.onWordClick(entry.getRId(),entry.getText());
	}

	
	public void launchWordSearch(String word)
	{

		this.searchWord=word;
		resetResultView();
		progress_ll.setVisibility(View.VISIBLE);
		this.searchProgressBar.setVisibility(View.VISIBLE);

		if(DictCommon.dictResultData !=null && DictCommon.dictResultData.main_word !=null &&  DictCommon.dictResultData.main_word.equalsIgnoreCase(word))
		{
			String data=BuildDisplayResult(DictCommon.dictResultData);
			setSearchMessage(data);
			DisplayDictResult(DictCommon.dictResultData);
			return;
		}

		this.resultMessage="";
		if(isOnline)
		{
			this.setSearchMessage(String.format(MessageContent.ONLINE_SRC_MSG,searchWord,searchWord));
			new DictSearchTask(this).execute(searchWord);
		}
		else
		{
			this.setSearchMessage(String.format(MessageContent.OFFLINE_SRC_MSG,searchWord,searchWord));
			new OfflineDictSearchTask(this).execute(searchWord);
		}

	}
	
	public void initializeWordSearchSuccess() {

		boolean detailShown=false;

		Map<String,Map<String,List<StringIdRowItem>>> meaningMap= new HashMap<String, Map<String,List<StringIdRowItem>>>();
		if(exactMeaningMap.size()>0)
		{
			meaningMap.put("EXACT", exactMeaningMap);
			if(isTablet && !detailShown)
			{
               for(String key:exactMeaningMap.keySet())
               {
            	   if(exactMeaningMap.get(key).size()>0)
            	   {         		   
            	   
            	   showWordDetails(exactMeaningMap.get(key).get(0));
            	   detailShown=true;
            	   }
               }
			}
		}
		if(nearbyMeaning.size()>0)
		{
			Map<String,List<StringIdRowItem>> nearbyMeaningMap=new HashMap<String, List<StringIdRowItem>>();
			nearbyMeaningMap.put("NEARBY", nearbyMeaning);
			meaningMap.put("NEARBY", nearbyMeaningMap);
			
			if(isTablet && nearbyMeaning.size()>0 && !detailShown)
			{
				showWordDetails(nearbyMeaning.get(0));
				detailShown=true;
			}
		}

		if(defMeaning.size()>0)
		{
			Map<String,List<StringIdRowItem>> defMeaningMap=new HashMap<String, List<StringIdRowItem>>();
			defMeaningMap.put("DEF", defMeaning);
			meaningMap.put("DEF", defMeaningMap);

			if(isTablet && defMeaning.size()>0 && !detailShown)
			{
				showWordDetails(defMeaning.get(0));
				detailShown=true;
			}
		}


		MeaningListAdapter adapter = new MeaningListAdapter(getActivity(),searchWord, meaningMap);

		meaningLV.setAdapter(adapter);
	//	meaningLV.setExpanded(true);
		meaningLV.setOnItemClickListener(itemClickListener);
		


	}



	public String BuildDisplayResult(DictResultData drd)
	{
		String data="";
		try
		{
			DictCommon.dictResultData=drd;
			if(drd==null)
			{	
				data+=MessageContent.WORD_NOT_FOUND;
			}
			else
			{
				if(drd.dictDataList ==null || drd.dictDataList.size()==0)
				{
					if(drd.hin2hin_list==null || drd.hin2hin_list.size()==0 )
					{
						data+=MessageContent.WORD_NOT_FOUND;    		    	
					}
					else
					{
						if(drd.IsHindi && drd.hin2hin_list.size()>0)
						{
							data+=String.format(MessageContent.DEFINITION_MEANING, searchWord,searchWord);
							for(DictionaryWordData dwd : drd.hin2hin_list)
							{
								defMeaning.add(new StringIdRowItem(-1, dwd.eng_word));
							}
						}
					}
				}
				else
				{
					if(drd.suggested_word)
					{
						data+=(String.format(MessageContent.SUGGESTED_MEANING, searchWord,searchWord));
					}
					for(int i=0;i<drd.dictDataList.size();i++)
					{
						DictionaryWordData dwd=drd.dictDataList.get(i);						

						String grammar=DictCommon.GetGrammarConstant(dwd.eng_grammar);

						if(!drd.IsHindi)
						{
							if(dwd.eng_word.compareToIgnoreCase(drd.main_word)==0)
							{

								if(!exactMeaningMap.containsKey(grammar))
								{
									exactMeaningMap.put(grammar,new ArrayList<StringIdRowItem>());
								}

								List<StringIdRowItem> grammarList=exactMeaningMap.get(grammar);
								if(!grammarList.contains(dwd.hin_word))
								{
									grammarList.add(new StringIdRowItem(dwd.rid,HindiCommon.ShiftLeftSmallE(dwd.hin_word)));
								}
							}
							else
							{
								String meaningDisplay=dwd.eng_word+" = "+HindiCommon.ShiftLeftSmallE(dwd.hin_word);
								nearbyMeaning.add(new StringIdRowItem(dwd.rid, meaningDisplay));
							}
						}
						else
						{
							if(dwd.hin_word.compareToIgnoreCase(drd.main_word)==0)
							{							
								if(!exactMeaningMap.containsKey(grammar))
								{
									exactMeaningMap.put(grammar,new ArrayList<StringIdRowItem>());
								}
								List<StringIdRowItem> grammarList=exactMeaningMap.get(grammar);
								if(!grammarList.contains(dwd.eng_word))
								{
									grammarList.add(new StringIdRowItem(dwd.rid,dwd.eng_word));
								}
							}
							else
							{
								String meaningDisplay=HindiCommon.ShiftLeftSmallE(dwd.hin_word)+" = "+dwd.eng_word;
								nearbyMeaning.add(new StringIdRowItem(dwd.rid, meaningDisplay));

							}
						}

					}	

				}
			}
		}
		catch(Exception e)
		{
			data+="There is a problem while showing dictionary result. Please try browser based dictionary at http://m.hinkhoj.com. Unable to display due to error."+e.getMessage();
			DictCommon.LogException(e);
		}

		return data;
	}

	public void DisplayDictResult(DictResultData drd) {
		try
		{
			this.progress_ll.setVisibility(View.GONE);
			if(drd!=null)
			{
				initializeWordSearchSuccess();
			}
			mCallBack.hideHelp();
		}
		catch(Exception e)
		{
			this.setSearchMessage("There is a problem while showing dictionary result. Please try browser based dictionary at http://m.hinkhoj.com. Unable to display due to error."+e.getMessage());
			DictCommon.LogException(e);
		}
	}

	public void setSearchMessage(String data) {
		// TODO Auto-generated method stub
		if(data!=null && data.trim().equalsIgnoreCase(""))
		{
			this.searchMessageTV.setVisibility(View.GONE);
				
		}
		else
		{
			this.searchMessageTV.setVisibility(View.VISIBLE);
			this.searchMessageTV.setText(Html.fromHtml(data));
		}
	}

	/* reset or cleanup */

	public void resetResultView() {
		this.progress_ll.setVisibility(View.GONE);
		this.searchMessageTV.setVisibility(View.GONE);
		exactMeaningMap.clear();
		nearbyMeaning.clear();
		defMeaning.clear();
		meaningLV.setAdapter(new MeaningListAdapter(getActivity(), "",new HashMap<String,Map<String, List<StringIdRowItem>>>()));
	}

	private static final String ARG_POSITION = "position";
	public static final String IS_ONLINE = "isOnline";
	public static final String MEANING_ID = "meaning_id";
	public static final String MAIN_WORD = "main_word";

	private int position;
	private String resultMessage;

	public static DictionarySearchFragment newInstance(int position) {
		DictionarySearchFragment f = new DictionarySearchFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putBoolean(ARG_TABLET, true);
		f.setArguments(b);
		return f;
	}


	@Override
	public void onDestroy()
	{
		if(exactMeaningMap!=null)
		{
			exactMeaningMap.clear();
		}
		if(nearbyMeaning!=null)
		{
			nearbyMeaning.clear();
		}
		super.onDestroy();
	}

	public void setResultMessage(String resultMessage) {
		// TODO Auto-generated method stub
		setSearchMessage(resultMessage);
		this.resultMessage=resultMessage;
	}

}
