package HinKhoj.Dictionary.activity;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.Helpers.Text2SpeechHandler;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Dictionary.fragments.WordDetailsFragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class WordDetailsActivity extends CommonBaseActivity implements OnWordSelectedFromSearchSuccess{


	private Text2SpeechHandler t2sHandler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_dictionary_search_success_for_tab);
		try
		{
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		View customTitle = getLayoutInflater().inflate(R.layout.appheader_layout, null);
		getSupportActionBar().setCustomView(customTitle);
		getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(),UICommon.GetBackgroundImage(this)));

		
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
				ImageView icon = (ImageView) findViewById(android.R.id.home);
				FrameLayout.LayoutParams iconLp = (FrameLayout.LayoutParams) icon.getLayoutParams();
				iconLp.rightMargin = iconLp.leftMargin = 20;
				icon.setLayoutParams(iconLp);
			}

			DictCommon.setupDatabase(this);      
			this.t2sHandler= new Text2SpeechHandler(this);
			DictCommon.InitializeAds(this, R.id.ad);
		}
		catch(Exception e)
		{
			UICommon.showLongToast(this, "Error loading page."+e.getMessage());
		}

	}





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		finish();
		overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
		return true;
	}





	@Override
	public void onWordClick(int meaning_id,String word) {
		WordDetailsFragment wordDetailsFragment = (WordDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.titles1);
		wordDetailsFragment.refreshView(meaning_id,word);  
	}


	@Override
	public void onWordSpeak(String word) {
		if(t2sHandler!=null)
		{
			t2sHandler.speakOut(word);
		}
	}
	@Override
	public void onDestroy()
	{
		try
		{
			if(t2sHandler!=null)
			{
				t2sHandler.onDestroy();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		super.onDestroy();
	}



	@Override
	public void hideHelp() {
		// TODO Auto-generated method stub
		
	}
	
}
