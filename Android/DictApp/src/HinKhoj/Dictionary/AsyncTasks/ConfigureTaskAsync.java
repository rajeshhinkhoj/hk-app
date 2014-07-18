package HinKhoj.Dictionary.AsyncTasks;
import java.io.IOException;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;
import HinKhoj.Dictionary.activity.ConfigureActivity;
import android.os.AsyncTask;
import android.widget.Toast;

public class ConfigureTaskAsync extends AsyncTask<Void,Integer,Void> {
	private ConfigureActivity dictMain=null;
	public ConfigureTaskAsync(ConfigureActivity dictMain)
	{
		this.dictMain=dictMain;
		
	}
    @Override
	protected Void doInBackground(Void... arg0) {
    	try {
    		DictCommon.tryCloseOfflineDb();
    		OfflineDatabaseSetupManager.DeleteDictDatabase();
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try
    	{
    		//Log.v("hinkhoj","Uncompressing dictionary files...");
			OfflineDatabaseSetupManager.UnCompressDictionary(dictMain.mProgressBar,dictMain);
			//Log.v("hinkhoj","Creating database index for better performance...");
    		DictCommon.createIndexInOfflineDb(dictMain.mProgressBar);  
		} catch (IOException e) {
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
	protected void onPostExecute(Void result) {
    	//Log.v("hinkhoj","updating dict message");
    	try
    	{
    	
    		Toast.makeText(dictMain, "Dictionary configuration complete.", Toast.LENGTH_LONG).show();
    		dictMain.onFinishConfigure();
    		
    	}
    	catch(Exception e)
    	{
    		//Log.v("hinkhoj","unexpected exception");
    	}
    }
	
}

