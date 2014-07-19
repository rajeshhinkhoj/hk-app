
package HinKhoj.Dictionary.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.DrawerItemClickAsyncTask;
import HinKhoj.Dictionary.AsyncTasks.LaunchExternalDictSearchTask;
import HinKhoj.Dictionary.AsyncTasks.MainActivityLoadTaskAsync;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.HinKhojLogger;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.Helpers.AdvanceHindiTextWatcher;
import HinKhoj.Dictionary.Helpers.Text2SpeechHandler;
import HinKhoj.Dictionary.Listeners.FindMeaningEditorHelper;
import HinKhoj.Dictionary.Marketing.AppRater;
import HinKhoj.Dictionary.Receiver.WODAlarmService;
import HinKhoj.Dictionary.adapters.MyExpandableListAdapter;
import HinKhoj.Dictionary.billing.AppProperties;
import HinKhoj.Dictionary.billing.domain.items.PremiumAccount;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Dictionary.fragments.SearchResultFragment;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import HinKhoj.Dictionary.fragments.SavedWordsFragment;
import HinKhoj.Dictionary.fragments.SavedWordsFragment.OnWordSelectedListener;
import HinKhoj.Dictionary.fragments.SearchHistoryFragment;
import HinKhoj.Dictionary.fragments.SearchHistoryFragment.onWordSelectedFromHistoryListener;
import HinKhoj.Hindi.KeyBoard.HindiCheckBoxHandler;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import HinKhoj.Hindi.KeyBoard.HindiKBHelper;
import HinKhoj.Hindi.KeyBoard.HindiKeyHandler;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DictionaryMainActivity extends CommonBaseActivity implements OnPagerContentChangedListener, OnWordSelectedListener,onWordSelectedFromHistoryListener,OnWordSelectedFromSearchSuccess,OnIabSetupFinishedListener{
	protected static final String TAG = "hinkhoj";
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int fragmentPosition;
	private boolean isDeviceRotate;
	private IabHelper billingHelper=null;
	private Boolean searchOptionsShown=false;
	private HindiEditText SearchET=null;
	private HindiKBHelper hkb=null;
	private boolean isOnline=true;
	private Text2SpeechHandler t2sHandler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try
		{
			DictCommon.initializeSettings(this);
			initializeDrawer();
			initializeSearchHeader();
			initializeFragment(savedInstanceState);
			new MainActivityLoadTaskAsync(this).execute(new Void[]{});
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
			Toast.makeText(this, "Unable to load app", Toast.LENGTH_LONG).show();
		}
	}




	private void initializeDrawer() {
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		mDrawerList.setGroupIndicator(null);
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new MyExpandableListAdapter(this,R.layout.drawer_list_item));
		mDrawerList.setOnGroupClickListener(new DrawerItemClickListener());
		mDrawerList.setOnChildClickListener(new DrawerItemClickListener());
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		//getSupportActionBar().setIcon(R.color.transparent);
		View customTitle = getLayoutInflater().inflate(R.layout.appheader_sb_layout, null);
		getSupportActionBar().setCustomView(customTitle);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
				) {

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
					invalidateOptionsMenu(); 
				}
			}
			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
					invalidateOptionsMenu(); 
				} 
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//UICommon.getOverflowMenu(this); //for forced hardware menu
	}


	private void initializeFragment(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Intent inputIntent=getIntent();
		if(inputIntent!=null )
		{
			handleIntent(inputIntent);
			return;
		}
		if (savedInstanceState == null) {
			selectItem(0);
		} else {
			setTabPosition(savedInstanceState.getInt("fragmentTabPosition"));
			setFragmentPosition(savedInstanceState.getInt("fragmentPosition"));
			isDeviceRotate=true;
			selectItem(getFragmentPosition());
		}
	}



	private void setHomeTitle(String title) {
		// TODO Auto-generated method stub
		/*
		TextView homeTitle = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.hindi_english_dictionary);
		homeTitle.setText(title);
		 */
	}



	@Override
	public void onNewIntent(Intent inputIntent)
	{
		setIntent(inputIntent);

		if(inputIntent!=null )
		{
			handleIntent(inputIntent);
		}
	}

	private void handleIntent(Intent inputIntent) {
		// TODO Auto-generated method stub
		int fragmentIndex=-1;
		int tabIndex=-1;
		String action=inputIntent.getAction();
		if(action!=null && action.equals(Intent.ACTION_VIEW))
		{
			String searchWord="";
			final List<String> segments = inputIntent.getData().getPathSegments();
			if (segments.size() > 1) {
				searchWord = segments.get(1);
			}

			searchWord=DictCommon.ExtractSearchWord(searchWord);
			if(searchWord!=null)
			{
				setSearchText(searchWord);
				LaunchMeaning(searchWord, false);
			}
			return;
		}
		else if(action !=null && action.equals(Intent.ACTION_SEARCH))
		{
			String searchWord = inputIntent.getStringExtra(SearchManager.QUERY);
			LaunchMeaning(searchWord, true);
			return;
		}


		fragmentIndex=inputIntent.getIntExtra(CommonBaseActivity.FRAGMENT_INDEX, -1);
		tabIndex=inputIntent.getIntExtra(CommonBaseActivity.TAB_INDEX, -1);

		if(fragmentIndex!=-1)
		{
			selectItem(fragmentIndex,tabIndex);
		}
		else
		{
			selectItem(0);
		}
	}



	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home: 
			if(mDrawerToggle!=null)
	        {
				if(!mDrawerToggle.isDrawerIndicatorEnabled())
				{
					this.onBackPressed();
				}
				else
				{
					if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
						mDrawerLayout.closeDrawer(mDrawerList);
					} else {
						mDrawerLayout.openDrawer(mDrawerList);
					}
				}
	        }
			break;
		case R.id.search_menu:

			item.expandActionView();
			break;
		case R.id.exit_menu:
			DictCommon.exitApp(this);
			break;
		case R.id.share_menu:
			DictCommon.shareApp(this);
			break;
		case R.id.feedback_menu:
			DictCommon.askFeedback(this);
			break;  


		}
		return super.onOptionsItemSelected(item);
	}




	private void initializeSearchHeader() {
		try
		{
			// TODO Auto-generated method stub
			/** Get the edit text from the action view */
			if(this.SearchET==null)
			{
				this.SearchET = ( HindiEditText ) this.findViewById(R.id.searchButton);
				this.SearchET.setOnTouchListener(new RightDrawableOnTouchListener(SearchET) {
					@Override
					public boolean onDrawableTouch(final MotionEvent event) {
						return onClickSearch(SearchET);
					}

					private boolean onClickSearch(final View view) {
						launchWordSearch();
						return true;
					}

					private void launchWordSearch() {
						// TODO Auto-generated method stub
						String word=SearchET.getText().toString();
						if(word.trim().equalsIgnoreCase(""))
						{
							UICommon.showLongToast(SearchET.getContext(), "Please enter valid input");
						}
						else
						{
							new LaunchExternalDictSearchTask(DictionaryMainActivity.this).execute(new String[]{word});
							//SearchET.clearFocus();
							UICommon.HideInputKeyBoard(SearchET, SearchET.getContext());
						}
					}		
				});

				this.hkb= new HindiKBHelper(this);

				TableLayout helpLayout=(TableLayout)findViewById(R.id.kbhelpId);
				hkb.setHelpLayout(helpLayout);


				SearchET.SetHindiKBHelper(hkb);
				HindiKeyHandler kh= new HindiKeyHandler(SearchET);
				SearchET.setOnKeyListener(kh);
				AdvanceHindiTextWatcher htw=new AdvanceHindiTextWatcher(SearchET);
				SearchET.addTextChangedListener(htw);

				FindMeaningEditorHelper fmEditorHelper = new FindMeaningEditorHelper(this);
				SearchET.setOnEditorActionListener(fmEditorHelper);

				SearchET.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				UICommon.HideInputKeyBoard(SearchET, this);
				CheckBox cb=(CheckBox)findViewById(R.id.cbh);
				cb.setOnCheckedChangeListener(new HindiCheckBoxHandler(SearchET));

			}

			final ImageButton optionsButton=(ImageButton)findViewById(R.id.optionsButton);
			optionsButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					LinearLayout soll=(LinearLayout)findViewById(R.id.search_options);
					if(!searchOptionsShown)
					{

						soll.setVisibility(View.VISIBLE);

						optionsButton.setImageResource(R.drawable.ic_action_collapse);
						searchOptionsShown=true;
					}
					else
					{

						soll.setVisibility(View.GONE);
						optionsButton.setImageResource(R.drawable.ic_action_expand);
						searchOptionsShown=false;
					}

				}
			});

		}
		catch(Exception e)
		{
			HinKhojLogger.Log("Search initialization failed");
			DictCommon.LogException(e);
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


	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
	ExpandableListView.OnGroupClickListener ,ExpandableListView.OnChildClickListener{
		boolean isClicked;
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

			try
			{
				if (MyExpandableListAdapter.isGroupParent(groupPosition)) {

					if(!isClicked){
						v.findViewById(R.id.expandable_image_view_id).setBackgroundResource(R.drawable.arrow_up_button);

					}else{
						v.findViewById(R.id.expandable_image_view_id).setBackgroundResource(R.drawable.arrow_down_button); 
					}
					isClicked=!isClicked;
				} else {
					isDeviceRotate=false;
					mDrawerLayout.closeDrawer(mDrawerList);
					new DrawerItemClickAsyncTask(DictionaryMainActivity.this, groupPosition).execute(new Void[]{});

				}
			}
			catch(Exception e)
			{
				DictCommon.LogException(e);
				if(mDrawerLayout!=null)
				{
					mDrawerLayout.closeDrawer(mDrawerList);
				}

			}

			return false;
		}

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			isDeviceRotate=true;
			setTabPosition(childPosition);
			mDrawerLayout.closeDrawer(mDrawerList);
			new DrawerItemClickAsyncTask(DictionaryMainActivity.this, groupPosition).execute(new Void[]{});
			return false;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("fragmentPosition", getFragmentPosition());
		outState.putInt("fragmentTabPosition", getTabPosition());
		//      super.onSaveInstanceState(outState);

	}

	private void selectItem(int position,int tabIndex) {
		//supportInvalidateOptionsMenu();
		setFragmentPosition(position);
		switch (position) {
		case 0:
			changeFragment(HINDI_ENGLISH_DICTIONARY, tabIndex);
			setHomeTitle(getResources().getString(R.string.hindi_english_dictionary));
			break;
		case 1:
			changeFragment(DICTIONARY_TOOLS,tabIndex );
			setHomeTitle(getResources().getString(R.string.dictionary_tools_drawer));
			break;

		case 2:
			changeFragment(MY_DICTIONARY,tabIndex);
			setHomeTitle(getResources().getString(R.string.my_account));
			break;

		case 3:
			changeFragment(PREMIUM_ACCOUNT,tabIndex);
			setHomeTitle(getResources().getString(R.string.premium_account));
			break;

		case 4:
			changeFragment(SETUP,tabIndex);
			setHomeTitle(getResources().getString(R.string.app_setup));
			break;

		case 5:
			DictCommon.shareApp(this);
			break;

		case 6:
			DictCommon.askFeedback(this);
			break;
		case 7:
			DictCommon.OpenWebApp(this);
			break;
		case 8:
			DictCommon.openAboutUs(this);
			break;


		default:
			changeFragment(HINDI_ENGLISH_DICTIONARY, tabIndex);
			break;
		}
		DictCommon.InitializeAds(this, R.id.ad);
		mDrawerLayout.closeDrawer(mDrawerList);

	}
	public void selectItem(int position) {

		supportInvalidateOptionsMenu();
		setFragmentPosition(position);
		switch (position) {
		case 0:
			changeFragment(HINDI_ENGLISH_DICTIONARY, ONLINE_DICTIONARY);
			setHomeTitle(getResources().getString(R.string.hindi_english_dictionary));
			break;
		case 1:
			changeFragment(DICTIONARY_TOOLS,isDeviceRotate ? getTabPosition() :WORD_OF_DAY );
			setHomeTitle(getResources().getString(R.string.dictionary_tools_drawer));
			break;

		case 2:
			changeFragment(MY_DICTIONARY,isDeviceRotate ? getTabPosition() :SAVED_WORDS);
			setHomeTitle(getResources().getString(R.string.my_account));
			break;

		case 3:
			changeFragment(PREMIUM_ACCOUNT,isDeviceRotate ? getTabPosition() :ADS_SETTINGS);
			setHomeTitle(getResources().getString(R.string.premium_account));
			break;

		case 4:
			changeFragment(SETUP,isDeviceRotate ? getTabPosition() :SETTINGS);
			setHomeTitle(getResources().getString(R.string.app_setup));
			break;


		case 5:
			DictCommon.shareApp(this);
			break;
		case 6:
			DictCommon.askFeedback(this);
			break;
		case 7:
			DictCommon.OpenWebApp(this);
			break;
		case 8:
			DictCommon.openAboutUs(this);
			break;
		case 9:
			DictCommon.exitApp(this);
			break;

		default:
			changeFragment(HINDI_ENGLISH_DICTIONARY, ONLINE_DICTIONARY);
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
		DictCommon.InitializeAds(this, R.id.ad);

	}

	public void LaunchMeaning(String searchWord,boolean isOnline) {
		setFragmentPosition(CommonBaseActivity.MEANING_TAB);
		if(SearchET!=null)
		{
			//SearchET.clearFocus();
			UICommon.HideInputKeyBoard(SearchET, this);
		}
		DictCommon.dictResultData=null;
		if(getResources().getBoolean(R.bool.large_layout))
		{
			Intent i= new Intent(this,WordDetailsActivity.class);
			i.putExtra(DictionarySearchFragment.SEARCH_WORD_KEY, searchWord);
			i.putExtra(DictionarySearchFragment.IS_ONLINE, isOnline);
			startActivity(i);
		}
		else
		{
			Bundle bundle=new Bundle();
			bundle.putInt("selectedTab", 0);
			bundle.putBoolean(DictionarySearchFragment.IS_ONLINE, isOnline);
			bundle.putString(DictionarySearchFragment.SEARCH_WORD_KEY, searchWord);
			SearchResultFragment pageSlidingTabStripFragment = SearchResultFragment.newInstance();
			pageSlidingTabStripFragment.setArguments(bundle);
			replaceFragment(pageSlidingTabStripFragment,pageSlidingTabStripFragment.getClass().getSimpleName());
		}
		this.InitializeAds();
	}

	@Override
	public void onBackPressed() {

		try
		{
			super.onBackPressed();
			if(getSupportFragmentManager().getBackStackEntryCount()==0)
			{
				finish();
			}
			else
			{
				if(SearchET!=null)
				{
					setSearchText("");
				}
		        if(mDrawerToggle!=null)
		        {
		        	mDrawerToggle.setDrawerIndicatorEnabled(true);
		        }
			}
			/*
			if(getFragmentPosition()==0)
			{
				DictCommon.exitApp(this);
			}
			else
			{
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
					invalidateOptionsMenu(); 
				} 
				selectItem(0,0);
			}
			 */
		}
		catch(Exception e2)
		{
			//DictCommon.exitApp(this);
		}

	}

	




	@Override
	public void onWordSelected(int position, String word) {

		setFragmentPosition(HINDI_ENGLISH_DICTIONARY);
		setTabPosition(OFFLINE_DICTIONARY);
		setHomeTitle(getResources().getString(R.string.hindi_english_dictionary));
		setSearchText(word);
		LaunchMeaning(word, false);	
	}
	@Override
	public void WordSelectedFromHistory(int position, String word) {
		setFragmentPosition(HINDI_ENGLISH_DICTIONARY);
		setTabPosition(OFFLINE_DICTIONARY);
		setHomeTitle(getResources().getString(R.string.hindi_english_dictionary));
		setSearchText(word);
		LaunchMeaning(word, false);	  

	}

	@Override
	public void onPagerContentChanged(int tabPosition) {
		setTabPosition(tabPosition);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == DictionarySearchFragment.ONLINE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			if(matches.size()>0)
			{
				String word=matches.get(0);
				if(word!=null && word!="")
				{
					setSearchText(word);
					setFragmentPosition(HINDI_ENGLISH_DICTIONARY);
					setTabPosition(ONLINE_DICTIONARY);
					LaunchMeaning(word, true);
				}
			}
		}

		if (requestCode == DictionarySearchFragment.OFFLINE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			if(matches.size()>0)
			{
				String word=matches.get(0);
				if(word!=null && word!="")
				{
					setSearchText(word);
					setFragmentPosition(HINDI_ENGLISH_DICTIONARY);
					setTabPosition(OFFLINE_DICTIONARY);
					LaunchMeaning(word, false);
				}
			}
		}

		try
		{
				super.onActivityResult(requestCode, resultCode, data);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			// Have we been disposed of in the meantime? If so, quit.
			if (billingHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				DictCommon.InitializeAds(DictionaryMainActivity.this, R.id.ad);
				return;
			}

			

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(PremiumAccount.getPremiumAccountSKU());
			boolean mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
			if(mIsPremium)
			{
				AppAccountManager.setAdsStatus(DictionaryMainActivity.this, false);
			}
			else
			{
				AppAccountManager.setAdsStatus(DictionaryMainActivity.this, true);
			}
			DictCommon.InitializeAds(DictionaryMainActivity.this, R.id.ad);
		}

		private boolean verifyDeveloperPayload(Purchase purchase) {
			// TODO Auto-generated method stub
			return true;
		}
	};


	@Override
	public void onIabSetupFinished(IabResult result) {
		try
		{
			if (result.isSuccess()) {
				if (!billingHelper.subscriptionsSupported()) {
					AppAccountManager.setAdsStatus(DictionaryMainActivity.this, true);
					DictCommon.InitializeAds(this, R.id.ad);
					return;
				}
				else
				{
					if(DictCommon.IsConnected(this) && billingHelper !=null)
					{
						billingHelper.queryInventoryAsync(false,mGotInventoryListener);
					}
				}


			} 
			else {
				DictCommon.InitializeAds(this, R.id.ad);

			}
		}
		catch(Exception e)
		{
			DictCommon.InitializeAds(this, R.id.ad);
		}
	}


	@Override
	public void onSearchClicked() {
		Intent intent=new Intent(this,WordDetailsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	}
	/**
	 * @return the fragmentPosition
	 */
	public int getFragmentPosition() {
		return fragmentPosition;
	}

	/**
	 * @param fragmentPosition the fragmentPosition to set
	 */
	public void setFragmentPosition(int fragmentPosition) {
		this.fragmentPosition = fragmentPosition;
	}

	@Override
	public void onMenuItemClicked(int selectionPosition, int tabPosition) {
		isDeviceRotate=true;
		setTabPosition(tabPosition);
		setFragmentPosition(selectionPosition);
		selectItem(selectionPosition);      
	}


	@Override
	public void onDeleteSelected() {

		SavedWordsFragment savedWordsFragment = (SavedWordsFragment)getSupportFragmentManager().findFragmentById(R.id.saved_words_main_fragments_small);
		savedWordsFragment.selectTodeleteWords();
	}


	@Override
	public void onDeleteButtonPressed() {
		SavedWordsFragment savedWordsFragment = (SavedWordsFragment)getSupportFragmentManager().findFragmentById(R.id.saved_words_main_fragments_small);
		savedWordsFragment.deleteSelectedWords();
	}

	@Override
	public void onDeleteButtonPressedHistory() {
		SearchHistoryFragment searchHistoryFragment = (SearchHistoryFragment)getSupportFragmentManager().findFragmentById(R.id.history_words_main_fragments_small);
		searchHistoryFragment.deleteSelectedWords();
	}

	@Override
	public void onDeleteSelectedFromHistory() {

		SearchHistoryFragment searchHistoryFragment = (SearchHistoryFragment)getSupportFragmentManager().findFragmentById(R.id.history_words_main_fragments_small);
		searchHistoryFragment.selectTodeleteWordsFromHistory();

	}



	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dictionary Voice Input");
		if(this.isOnline)
		{
			startActivityForResult(intent, DictionarySearchFragment.ONLINE_REQUEST_CODE);
		}
		else
		{
			startActivityForResult(intent, DictionarySearchFragment.OFFLINE_REQUEST_CODE);
		}
	}


	//initialize things

	private void InitializeAlarm() {
		// TODO Auto-generated method stub
		if(!DictCommon.isWODAlarmSet(this))
		{
			AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

			// Set the alarm to start at approximately 2:00 p.m.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, 8);


			Intent alarmIntent = new Intent(this, WODAlarmService.class);

			PendingIntent sender = PendingIntent.getBroadcast(this, 12345,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

			// With setInexactRepeating(), you have to use one of the AlarmManager interval
			// constants--in this case, AlarmManager.INTERVAL_DAY.
			alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					AlarmManager.INTERVAL_DAY, sender);

			DictCommon.setWODAlarm(this);
		}
	}

	private void InitializeWODNotification()
	{
		if(!DictCommon.IsNotificationSet(this))
		{
			DictCommon.setNotification(this, true);
		}
	}



	public void delayedInitialization() {
		try
		{
			if(this.SearchET!=null)
			{

				ToggleButton tb=(ToggleButton)findViewById(R.id.onlineToggle);
				tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							isOnline=true;
							DictCommon.setOnlineSearch(buttonView.getContext(), true);
						} else {
							// The toggle is disabled
							isOnline=false;
							DictCommon.setOnlineSearch(buttonView.getContext(), false);
						}
					}
				});

				if(DictCommon.isOnlineSearch(this))
				{
					tb.setChecked(true);
					this.isOnline=true;
				}
				else
				{
					tb.setChecked(false);
					this.isOnline=false;
				}

				ImageButton speakButton = (ImageButton) findViewById(R.id.voiceInputButton);
				PackageManager pm = getPackageManager();
				List<ResolveInfo> activities = pm.queryIntentActivities(
						new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
				if (activities.size() == 0)
				{
					speakButton.setEnabled(false);
					speakButton.setVisibility(View.GONE);
				}
				else
				{
					speakButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							startVoiceRecognitionActivity();

						}
					});
					speakButton.setVisibility(View.VISIBLE);
				}

			}
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Error while loading some components", Toast.LENGTH_LONG).show();
			DictCommon.LogException(e);
		}

		try
		{
			billingHelper = new IabHelper(this, AppProperties.getBase64Key());
			billingHelper.enableDebugLogging(false);
			billingHelper.startSetup(this);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Error while loading some components", Toast.LENGTH_LONG).show();
			DictCommon.LogException(e);
		}

		try
		{
			this.t2sHandler= new Text2SpeechHandler(this);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			InitializeAlarm();
			InitializeWODNotification();
			DictCommon.AddTrackEvent(this);
			AppRater.app_launched(this);				
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Error while loading some components", Toast.LENGTH_LONG).show();
			DictCommon.LogException(e);
		}




	}



	public void InitializeAds() {
		DictCommon.InitializeAds(this, R.id.ad);
	}

	@Override
	public void onDestroy() {
		try
		{
			disposeBillingHelper();
			if(this.t2sHandler!=null)
			{
				this.t2sHandler.onDestroy();
			}
		}
		finally
		{	
		super.onDestroy();
		}
	}

	private void disposeBillingHelper() {
		try
		{
			if (billingHelper != null) {
				billingHelper.dispose();
			}

		}catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		billingHelper = null;
	}

	public String getEditText() {
		// TODO Auto-generated method stub
		if(SearchET!=null)
		{
			return SearchET.getText().toString();
		}
		return "";
	}

	public void clearFocus() {
		// TODO Auto-generated method stub
		SearchET.clearFocus();
	}

	@Override
	public void onWordClick(int meaning_id, String word) {
		mDrawerToggle.setDrawerIndicatorEnabled(false);
		showWordDetailsMobile(meaning_id,word);
	}
	@Override
	public void onWordSpeak(String word) {
		if(t2sHandler!=null)
		{
			t2sHandler.speakOut(word);
		}

	}

	@Override
	public void hideHelp() {
		if(this.hkb!=null)
		{
			this.hkb.HideHelp();
			//this.SearchET.clearFocus();
			UICommon.HideInputKeyBoard(SearchET, this);
		}
	}

	public boolean getIsOnline() {
		// TODO Auto-generated method stub
		return isOnline;
	}
	
	private void setSearchText(String inpt) {
		// TODO Auto-generated method stub
		if(this.SearchET!=null)
		{
			this.SearchET.setFocusable(false);
			this.SearchET.setFocusableInTouchMode(false);
			this.SearchET.setText(inpt);
			
			this.SearchET.setFocusable(true);
			this.SearchET.setFocusableInTouchMode(true);			
		}
	}

}