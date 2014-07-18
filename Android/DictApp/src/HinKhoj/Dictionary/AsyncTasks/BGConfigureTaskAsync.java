package HinKhoj.Dictionary.AsyncTasks;
import java.io.IOException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;
import android.content.Context;
import android.os.AsyncTask;

public class BGConfigureTaskAsync extends AsyncTask<Void,Integer,Void> {
	private Context context=null;
	public BGConfigureTaskAsync(Context context)
	{
		this.context=context;
		
	}
    @Override
	protected Void doInBackground(Void... arg0) {
    	try {
    		DictCommon.tryCloseOfflineDb();
			OfflineDatabaseSetupManager.UnCompressDictionary(null,context);
			OfflineDatabaseSetupManager.UnCompressHangman(context);
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
    	//Toast.makeText(context, "Offline Dictionary configured", Toast.LENGTH_LONG).show();
    	DictCommon.tryCloseOfflineDb();
        DictCommon.setupOfflineDb();
    }
	
}

