package HinKhoj.Dictionary.fragments;


import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.CommonBaseActivity;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainFragmentOffline extends DictionarySearchFragment {



	private OnPagerContentChangedListener mCallBack;
	private boolean isPageLoaded=false;
	private static final String ARG_POSITION = "position";

	private int position;
	
	private static final String ARG_SELECT_POSITION = "select_position";

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
		if(position==1)
		{
			isOnline=false;
		}
		else
		{
			isOnline=true;
		}
		selectedPosition=getArguments().getInt(ARG_SELECT_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.offline_dictionary_main_fragment, container, false);
	 // 	view.setBackgroundDrawable(new BitmapDrawable(getResources(),UICommon.GetBackgroundImage(view.getContext())));
	  	isPageLoaded = false;
	  	if(selectedPosition==position)
		{
			LoadContent();
		}
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser && view!=null) {
	        	LoadContent();
	        
	    }
	}


	 private void LoadContent() {
		if(!isPageLoaded)
		{
     	isPageLoaded = true;
   		DictCommon.setupOfflineDb();
 		DictCommon.setupDatabase(view.getContext());
 		initializeDictionaryFunctionalityLayout();
		}
	}

	@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			try
			{
				super.onActivityResult(requestCode, resultCode, data);
			}
			catch(Exception e)
			{
				DictCommon.LogException(e);
			}
		}
	
	
	protected void  initializeDictionaryFunctionalityLayout(){

		super.initSearchView();
		RelativeLayout   dictionary_tools=(RelativeLayout)view.findViewById(R.id.dictionary_tools);
		RelativeLayout   my_dictionary=(RelativeLayout)view.findViewById(R.id.my_dictionary);
		RelativeLayout   setup_dictionary=(RelativeLayout)view.findViewById(R.id.setup_dictionary);
		RelativeLayout   saved_word=(RelativeLayout)view.findViewById(R.id.saved_word);
		RelativeLayout   search_history=(RelativeLayout)view.findViewById(R.id.search_history);
		RelativeLayout   settings=(RelativeLayout)view.findViewById(R.id.settings);
		RelativeLayout   configure=(RelativeLayout)view.findViewById(R.id.configure_word);
		
		RelativeLayout   word_of_the_day=(RelativeLayout)view.findViewById(R.id.word_of_the_day);
		RelativeLayout   word_guess_game=(RelativeLayout)view.findViewById(R.id.word_guess_game);
		RelativeLayout   pronunciation_english=(RelativeLayout)view.findViewById(R.id.pronunciation_english);
		RelativeLayout   spell_checker=(RelativeLayout)view.findViewById(R.id.spell_checker);
		
		setup_dictionary.setOnClickListener(onClickListener);
		my_dictionary.setOnClickListener(onClickListener);
		dictionary_tools.setOnClickListener(onClickListener);

		saved_word.setOnClickListener(onClickListener);
		search_history.setOnClickListener(onClickListener);
		settings.setOnClickListener(onClickListener);
		configure.setOnClickListener(onClickListener);

		word_of_the_day.setOnClickListener(onClickListener);
		word_guess_game.setOnClickListener(onClickListener);
		pronunciation_english.setOnClickListener(onClickListener);
		spell_checker.setOnClickListener(onClickListener);

	}


	public static MainFragmentOffline newInstance(Object[] args) {
		MainFragmentOffline f = new MainFragmentOffline();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]);
		b.putString(DictionarySearchFragment.SEARCH_WORD_KEY, (String)args[2]);
		b.putBoolean(ARG_TABLET, (boolean)args[3]);
		
		f.setArguments(b);
		return f;
	}
	OnClickListener onClickListener =new OnClickListener() {

		private boolean isMYDictionaryVisible=false;
		private boolean isSETUPDictionaryVisible=false;
		private boolean isDictionaryToolsVisible=false;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.configure_word:
				mCallBack.onMenuItemClicked(CommonBaseActivity.SETUP, CommonBaseActivity.CONFIGURE);
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
				

			case R.id.setup_dictionary:
				LinearLayout   setup_dictionary_childs=(LinearLayout)view.findViewById(R.id.setup_dictionary_childs);
				RelativeLayout   setup_dictionary=(RelativeLayout)view.findViewById(R.id.setup_dictionary);
				setup_dictionary.findViewById(R.id.setup_dictionary_indictor).setBackgroundResource(0);
				if (!isSETUPDictionaryVisible) {
					setup_dictionary.findViewById(R.id.setup_dictionary_indictor).setBackgroundResource(R.drawable.arrow_up_button);
					setup_dictionary_childs.setVisibility(View.VISIBLE);
					isSETUPDictionaryVisible=true;
				} else {
					setup_dictionary.findViewById(R.id.setup_dictionary_indictor).setBackgroundResource(R.drawable.arrow_down_button);
					setup_dictionary_childs.setVisibility(View.GONE);
					isSETUPDictionaryVisible=false;
				}
				break;
			case R.id.saved_word:
				mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SAVED_WORDS);
				break;
			case R.id.search_history:
				mCallBack.onMenuItemClicked(CommonBaseActivity.MY_DICTIONARY, CommonBaseActivity.SEARCH_HISTORY);
				break;
			case R.id.settings:
				mCallBack.onMenuItemClicked(CommonBaseActivity.SETUP, CommonBaseActivity.SETTINGS);
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
}
