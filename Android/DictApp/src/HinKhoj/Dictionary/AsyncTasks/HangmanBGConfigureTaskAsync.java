package HinKhoj.Dictionary.AsyncTasks;
import java.io.IOException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;
import android.content.Context;
import android.os.AsyncTask;

public class HangmanBGConfigureTaskAsync extends AsyncTask<Void,Integer,Void> {
	private Context context=null;
	public HangmanBGConfigureTaskAsync(Context context)
	{
		this.context=context;
		
	}
	
	
    @Override
	protected Void doInBackground(Void... arg0) {
    	try {
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
    	DictCommon.initializeHangmanDB();
    }
	
}

