package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import HinKhoj.Dictionary.fragments.WordDetailsFragment;
import android.os.AsyncTask;

public class WordDetailsFragmentLoadTaskAsync extends AsyncTask<Void,Integer,Void> {
	private WordDetailsFragment dictMain=null;
	public WordDetailsFragmentLoadTaskAsync(WordDetailsFragment dictMain)
	{
		this.dictMain=dictMain;
		
	}
    @Override
	protected Void doInBackground(Void... arg0) {
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
    		DictCommon.LogException(e);
    	}
    }
	
}

