package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import android.os.AsyncTask;

public class DictSearchFragmentLoadTaskAsync extends AsyncTask<Void,Integer,Void> {
	private DictionarySearchFragment dictMain=null;
	public DictSearchFragmentLoadTaskAsync(DictionarySearchFragment dictMain)
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
    		//Log.v("hinkhoj","unexpected exception");
    	}
    }
	
}

