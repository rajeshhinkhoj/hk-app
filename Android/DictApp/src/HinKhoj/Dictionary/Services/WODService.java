package HinKhoj.Dictionary.Services;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.wordofthedayresultdata;
import HinKhoj.Dictionary.activity.CommonBaseActivity;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
public class WODService extends Service
{
	private static final String DEBUG_TAG = "hinkhoj";
	private FetchWOD wodDownloader;
	private static final int NOTIFICATION_EX = 0;
	Boolean new_wod_found=false;
	String eng_word="";
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		new_wod_found=false;
		//Log.v(DEBUG_TAG, "Execute service method ");
		Context context = WODService.this.getApplicationContext();
		try
		{
			DictCommon.setupDatabase(context);
			if(DictCommon.NeedWODDownload(context))
			{
				notifyUser();
			}
			else
			{
				stopSelf();
			}
			DictCommon.tryCloseMyDb();
		}
		catch(Exception e)
		{
			stopSelf();
		}

		return Service.START_NOT_STICKY;

	}

	@Override
	public IBinder onBind(Intent intent) {
		//Log.v(DEBUG_TAG, "WOD service on bind");
		return null;
	}
	@Override   
	public void onCreate() {
		//code to execute when the service is first created
		//Log.v(DEBUG_TAG, "WOD service on create");

	}   
	@Override   
	public void onDestroy() {   

	}

	public void notifyUser()
	{
		final NotificationCompat.Builder mBuilder;

		final NotificationManager notificationManager = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

		Intent notificationIntent = new Intent(this, DictionaryMainActivity.class);
		notificationIntent.putExtra(CommonBaseActivity.FRAGMENT_INDEX, CommonBaseActivity.DICTIONARY_TOOLS);
		notificationIntent.putExtra(CommonBaseActivity.TAB_INDEX, CommonBaseActivity.WORD_OF_DAY);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(this,0, notificationIntent, 0);

		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("Word of the day")
		.setContentText("Fetching Word of the day from hinkhoj.com")
		.setSmallIcon(R.drawable.n_icon);
		mBuilder.setContentIntent(contentIntent);

		new Thread(
				new Runnable() {
					@Override
					public void run() {

						int incr;
						notificationManager.notify(0, mBuilder.build());
						try {
							Log.d("hinkhoj", "updating word of day...");
							wodDownloader = new FetchWOD();
							wodDownloader.asyncFetch();

							// When the loop is finished, updates the notification
							if(new_wod_found)
							{
								mBuilder.setContentText(eng_word+" - Word of the day");
								notificationManager.notify(NOTIFICATION_EX, mBuilder.build());
							}
							else
							{
								notificationManager.cancelAll();							
							}
							stopSelf();

						} 
						catch (Exception e) {
							DictCommon.LogException(e);
							notificationManager.cancelAll();
							stopSelf();

						}
					}
				}
				).start();
	}

	//### sub class fetch rss ###
	public class FetchWOD  
	{
		Context context=null;
		public FetchWOD() {
			//Log.v(DEBUG_TAG, "calling constr!");
		}
		public void execute(Context context) {
			// TODO Auto-generated method stub
			this.context=context;
		}


		public void asyncFetch()
		{
			try
			{
				wordofthedayresultdata wrd=  DictCommon.GetWordOfDay(context);
				if(wrd!=null && wrd.dictDataList!=null && wrd.dictDataList.length>0)
				{
					eng_word=wrd.dictDataList[0].word;
					if(eng_word!=null && eng_word !="")
					{
						new_wod_found=true;
					}
				}
			}
			catch(Exception e)
			{
				//Log.v("hinkhoj","error finding word of day");
			}

		}

		//###ends class fetch rss 
	}

}
