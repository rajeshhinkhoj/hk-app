package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import android.os.AsyncTask;

public class MainActivityLoadTaskAsync extends AsyncTask<Void,Integer,Void> {
	private DictionaryMainActivity dictMain=null;
	public MainActivityLoadTaskAsync(DictionaryMainActivity dictMain)
	{
		this.dictMain=dictMain;
		
	}
    @Override
	protected Void doInBackground(Void... arg0) {
    	try {
    		DictCommon.setupOfflineDb();
    		DictCommon.setupDatabase(dictMain);
    		
    	} catch (Exception e) {
			DictCommon.LogException(e);
		}
		return null;
    }

    @Override
	protected void onProgressUpdate(Integer... progress) {
        //
    }

    @Override
	protected void onPostExecute(Void result) {
    	try
    	{
    		dictMain.delayedInitialization();
    		
    	}
    	catch(Exception e)
    	{
    		//Log.v("hinkhoj","unexpected exception");
    	}
    }
	
}

