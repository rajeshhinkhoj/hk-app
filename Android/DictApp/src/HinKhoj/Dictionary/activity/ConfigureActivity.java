package HinKhoj.Dictionary.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.ConfigureTaskAsync;
import HinKhoj.Dictionary.AsyncTasks.HangmanBGConfigureTaskAsync;
import HinKhoj.Dictionary.AsyncTasks.IniatializeConfigTaskAsync;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import HinKhoj.Dictionary.Database.OfflineDatabaseFileManager;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;

public class ConfigureActivity extends Activity {

	protected boolean mbActive;
	public ProgressBar mProgressBar;
	public TextView configureMsgTv=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{	
			if(OfflineDatabaseSetupManager.IsDictionaryUnCompressed())
			{
				onFinishConfigure();
			}
			else
			{
				setContentView(R.layout.configure_offline_dictionary);
				Button cancelBtn=(Button)findViewById(R.id.cancelButton);
				if(AppConstants.IN_APP_BILLING_TEST)
				{
					AppAccountManager.setAdsStatus(this, false);
				}
				cancelBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						onFinishConfigure();
					}
				});
				configureMsgTv= (TextView)findViewById(R.id.configuration_message);
				configureMsgTv.setVisibility(View.VISIBLE);
				mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
				mProgressBar.setMax(100);
				mProgressBar.setProgress(0);
				OfflineDatabaseFileManager.MoveOldFiles(3);
				new ConfigureTaskAsync(this).execute(new Void[]{});
			}
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Error while configuring application. Please report issue to info@hinkhoj.com",Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	public void updateProgress(final int progress) {
		if(null != mProgressBar) {
			mProgressBar.setProgress(progress);
		}
	}

	public void updateProgressMessage(String message)
	{
		if(null!= configureMsgTv)
		{
			configureMsgTv.setText(Html.fromHtml(message));
		}
	}



	public void onFinishConfigure() {
		// TODO Auto-generated method stub	
		Intent launchActivity=new Intent(ConfigureActivity.this,DictionaryMainActivity.class);  
		startActivity(launchActivity);
		finish();
	}
}
