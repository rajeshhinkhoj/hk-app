package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import android.os.AsyncTask;

public class OfflineDictSearchTask extends  AsyncTask<String,Long,DictResultData>
{

	private DictionarySearchFragment dictMain=null;
	private String errorInfo="";
	public OfflineDictSearchTask(DictionarySearchFragment dictMain)
	{
		this.dictMain=dictMain;
	}
	@Override
	protected DictResultData doInBackground(String... words) {
		// TODO Auto-generated method stub
		errorInfo="";
		try
		{
			DictResultData drd=DictCommon.GetDictResultOffline(dictMain.getFragmentContext(),words[0],DictCommon.offlinedb);
			errorInfo=dictMain.BuildDisplayResult(drd);
			return drd;
		}
		catch(Exception e)
		{
			errorInfo=e.toString();
		}
		return null;
	}

	@Override
	protected void onPostExecute(DictResultData drd)
	{
		try
		{
			if(drd!=null)
			{
	
					dictMain.setSearchMessage(errorInfo);
				dictMain.DisplayDictResult(drd);
			}
			else
			{
				dictMain.setSearchMessage("Error while finding meaning."+errorInfo);
			}
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","unexpected exception");
		}
	}

}
