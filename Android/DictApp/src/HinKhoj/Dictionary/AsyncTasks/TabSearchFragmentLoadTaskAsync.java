package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import HinKhoj.Dictionary.fragments.TabResultFragment;
import android.os.AsyncTask;

public class TabSearchFragmentLoadTaskAsync extends AsyncTask<Void,Integer,Void> {
	private TabResultFragment dictMain=null;
	public TabSearchFragmentLoadTaskAsync(TabResultFragment dictMain)
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

