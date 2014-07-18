package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.activity.ConfigureActivity;
import android.os.AsyncTask;

public class IniatializeConfigTaskAsync extends AsyncTask<Void,Integer,Void> {
	private ConfigureActivity activity=null;
	public IniatializeConfigTaskAsync(ConfigureActivity activity)
	{
		this.activity=activity;

	}
	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			Thread.sleep(500);
			DictCommon.setupOfflineDb();
			UICommon.GetBackgroundImage(activity);
			DictCommon.setupDatabase(activity);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		//
	}

	@Override
	protected void onPostExecute(Void args) {
		try
		{
			activity.startConfigure();
			

		}
		catch(Exception e)
		{
		      DictCommon.LogException(e);
		}
	}

}

