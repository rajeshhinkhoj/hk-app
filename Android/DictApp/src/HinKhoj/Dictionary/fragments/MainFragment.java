package HinKhoj.Dictionary.fragments;


import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.GetWordOfDayMainAsyncTask;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.CommonBaseActivity;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainFragment extends Fragment {

	private OnPagerContentChangedListener mCallBack;
	private HindiEditText SearchET=null;
	private boolean isPageLoaded=false;
	private static final String ARG_POSITION = "position";
	private static final String ARG_SELECT_POSITION = "select_position";
    private View view=null;
	private int position;
	private int selectedPosition;
	private boolean isTablet=false;
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
		
		if(getArguments().getBoolean(DictionarySearchFragment.ARG_TABLET))
		{
			isTablet=true;
		}
		else
		{
			isTablet=false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.online_dictionary_main_fragment, container, false);
		//view.setBackgroundDrawable(new BitmapDrawable(getResources(),UICommon.GetBackgroundImage(view.getContext())));
		isPageLoaded=false;
		if(position==selectedPosition)
		{
			LoadContent();
		}
		return view;
	}

	private void LoadContent() {
		if(!isPageLoaded)
		{
			isPageLoaded = true;
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
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && view !=null ) {
                 LoadContent();

		}
	}

	public Context getFragmentContext()
	{
		return view.getContext();
	}

	private void  initializeDictionaryFunctionalityLayout(){

		RelativeLayout   word_of_day_layout=(RelativeLayout)view.findViewById(R.id.word_of_day_layout);
	
		if(!isTablet)
		{
		RelativeLayout   dictionary_tools=(RelativeLayout)view.findViewById(R.id.dictionary_tools);
		RelativeLayout   my_dictionary=(RelativeLayout)view.findViewById(R.id.my_dictionary);
		RelativeLayout   premium_dictionary_layout=(RelativeLayout)view.findViewById(R.id.premium_dictionary);
		RelativeLayout   saved_word=(RelativeLayout)view.findViewById(R.id.saved_word);
		RelativeLayout   search_history=(RelativeLayout)view.findViewById(R.id.search_history);
		RelativeLayout   ads_settings=(RelativeLayout)view.findViewById(R.id.ads_settings);

		RelativeLayout   word_of_the_day=(RelativeLayout)view.findViewById(R.id.word_of_the_day);
		RelativeLayout   word_guess_game=(RelativeLayout)view.findViewById(R.id.word_guess_game);
		RelativeLayout   pronunciation_english=(RelativeLayout)view.findViewById(R.id.pronunciation_english);
		RelativeLayout   spell_checker=(RelativeLayout)view.findViewById(R.id.spell_checker);



		premium_dictionary_layout.setOnClickListener(onClickListener);
		my_dictionary.setOnClickListener(onClickListener);
		dictionary_tools.setOnClickListener(onClickListener);
		word_of_day_layout.setOnClickListener(onClickListener);

		saved_word.setOnClickListener(onClickListener);
		search_history.setOnClickListener(onClickListener);
		ads_settings.setOnClickListener(onClickListener);

		word_of_the_day.setOnClickListener(onClickListener);
		word_guess_game.setOnClickListener(onClickListener);
		pronunciation_english.setOnClickListener(onClickListener);
		spell_checker.setOnClickListener(onClickListener);

		}
		else
		{
			Button searchHistoryBtn=(Button)view.findViewById(R.id.search_history_btn);
			searchHistoryBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SEARCH_HISTORY);
					
					
				}
			});
			
			
			Button savedWordsBtn=(Button)view.findViewById(R.id.saved_word_btn);
			savedWordsBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SAVED_WORDS);
					
					
				}
			});
			
			Button wordGuessBtn=(Button)view.findViewById(R.id.word_guess_btn);
			 wordGuessBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.WORD_GUESS_GAME);
					
					
				}
			});
			
			Button pronBtn=(Button)view.findViewById(R.id.pronunciation_english_btn);
			pronBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.PRONUNCIATION);
					
					
				}
			});
			
			
			Button wodBtn=(Button)view.findViewById(R.id.word_of_the_day_btn);
			wodBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.WORD_OF_DAY);
					
					
				}
			});


			Button spellBtn=(Button)view.findViewById(R.id.spell_checker_btn);
			spellBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.SPELL_CHECKING);
					
					
				}
			});

			
			RelativeLayout   ads_settings=(RelativeLayout)view.findViewById(R.id.ads_settings);
			ads_settings.setOnClickListener(onClickListener);
			
		}

   		new GetWordOfDayMainAsyncTask(this).execute(new Void[]{});

	}


	public static MainFragment newInstance(Object[] args) {
		MainFragment f = new MainFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]);
		b.putString(DictionarySearchFragment.SEARCH_WORD_KEY, (String)args[2]);
		b.putBoolean(DictionarySearchFragment.ARG_TABLET, (Boolean)args[3]);
		f.setArguments(b);
		return f;
	}
	OnClickListener onClickListener =new OnClickListener() {

		private boolean isMYDictionaryVisible=false;
		private boolean isPremiumDictionaryVisible=false;

		private boolean isDictionaryToolsVisible=false;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.word_of_day_layout:
				mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.WORD_OF_DAY);
				break;
			case R.id.ads_settings:
				mCallBack.onMenuItemClicked(CommonBaseActivity.PREMIUM_ACCOUNT, CommonBaseActivity.ADS_SETTINGS);
				break;
			case R.id.dictionary_tools:
				LinearLayout   dictionary_tools_childs=(LinearLayout)view.findViewById(R.id.dictionary_tools_childs);
				RelativeLayout   dictionary_tools=(RelativeLayout)view.findViewById(R.id.dictionary_tools);
				dictionary_tools.findViewById(R.id.dictionary_tools_indictor).setBackgroundResource(0);
				if (!isDictionaryToolsVisible) {
					dictionary_tools.findViewById(R.id.dictionary_tools_indictor).setBackgroundResource(R.drawable.arrow_up_button);
					dictionary_tools_childs.setVisibility(View.VISIBLE);
					isDictionaryToolsVisible=true;
				} else {
					dictionary_tools.findViewById(R.id.dictionary_tools_indictor).setBackgroundResource(R.drawable.arrow_down_button);
					dictionary_tools_childs.setVisibility(View.GONE);
					isDictionaryToolsVisible=false;
				}
				break;
			case R.id.my_dictionary:
				LinearLayout   my_dictionary_childs=(LinearLayout)view.findViewById(R.id.my_dictionary_childs);
				RelativeLayout   my_dictionary=(RelativeLayout)view.findViewById(R.id.my_dictionary);
				my_dictionary.findViewById(R.id.my_dictionary_indictor).setBackgroundResource(0);
				if (!isMYDictionaryVisible) {
					my_dictionary.findViewById(R.id.my_dictionary_indictor).setBackgroundResource(R.drawable.arrow_up_button);
					my_dictionary_childs.setVisibility(View.VISIBLE);
					isMYDictionaryVisible=true;
				} else {
					my_dictionary.findViewById(R.id.my_dictionary_indictor).setBackgroundResource(R.drawable.arrow_down_button);
					my_dictionary_childs.setVisibility(View.GONE);
					isMYDictionaryVisible=false;
				}
				break;

			case R.id.premium_dictionary:
				LinearLayout   premium_dictionary_childs=(LinearLayout)view.findViewById(R.id.premium_dictionary_childs);
				RelativeLayout   premium_dictionary=(RelativeLayout)view.findViewById(R.id.premium_dictionary);
				premium_dictionary.findViewById(R.id.premium_dictionary_indictor).setBackgroundResource(0);
				if (!isPremiumDictionaryVisible) {
					premium_dictionary.findViewById(R.id.premium_dictionary_indictor).setBackgroundResource(R.drawable.arrow_up_button);
					premium_dictionary_childs.setVisibility(View.VISIBLE);
					isPremiumDictionaryVisible=true;
				} else {
					premium_dictionary.findViewById(R.id.premium_dictionary_indictor).setBackgroundResource(R.drawable.arrow_down_button);
					premium_dictionary_childs.setVisibility(View.GONE);
					isPremiumDictionaryVisible=false;
				}
				break;
			case R.id.saved_word:
				mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SAVED_WORDS);
				break;
			case R.id.search_history:
				mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SEARCH_HISTORY);
				break;
			case R.id.word_of_the_day:
				mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.WORD_OF_DAY);
				break;
			case R.id.pronunciation_english:
				mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.PRONUNCIATION);
				break;
			case R.id.spell_checker:
				mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.SPELL_CHECKING);
				break;
			case R.id.word_guess_game:
				mCallBack.onMenuItemClicked(CommonBaseActivity.DICTIONARY_TOOLS, CommonBaseActivity.WORD_GUESS_GAME);
				break;



			default:
				break;
			}

		}
	};

	
	@Override 
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.common, menu);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        super.onOptionsItemSelected(item);
	        DictCommon.HandleCommonMenuItems(getActivity(),item);
	        return true;
	    }

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	public  void initializeWOD(String[][] worddata) {
		// TODO Auto-generated method stub

		try
		{

			View localView=view;
			String eng_word=worddata[0][0];
			String hin_word=worddata[0][2];
			hin_word=HindiCommon.ShiftLeftSmallE(hin_word);
			String wod_display="Word of the day not available.";
			if(eng_word!="")
			{
				wod_display=eng_word+" = "+hin_word;
			}
			TextView wod_tv=(TextView)localView.findViewById(R.id.wod_display);
			DictCommon.setHindiFont(view.getContext(), wod_tv);
			wod_tv.setText(wod_display);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}
}
