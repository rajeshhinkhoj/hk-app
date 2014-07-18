package HinKhoj.Dictionary.AsyncTasks;

import java.net.UnknownHostException;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import android.os.AsyncTask;

public class DictSearchTask extends  AsyncTask<String,Long,DictResultData>
{

	private DictionarySearchFragment dictMain=null;
	private String errorInfo="";
	public DictSearchTask(DictionarySearchFragment dictMain)
	{
		this.dictMain=dictMain;
	}
	@Override
	protected DictResultData doInBackground(String... words) {
		// TODO Auto-generated method stub
		errorInfo="";
		try
		{
			DictCommon.setupDatabase(this.dictMain.getFragmentContext());
			DictResultData drd= DictCommon.GetDictResult(this.dictMain.getFragmentContext(),words[0]);
			errorInfo=dictMain.BuildDisplayResult(drd);
			return drd;
		}
		catch(UnknownHostException uh)
		{
			errorInfo="Please enable internet connection on your mobile/tablet. Couldn't connect to http://dict.hinkhoj.com. Report this issue to info@hinkhoj.com if your internet connection is working fine.<br/> कृपया इन्टरनेट कनेक्शन की जाँच कर ले | ";
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
				dictMain.setResultMessage(errorInfo);
				dictMain.DisplayDictResult(drd);
			}
			else
			{
				dictMain.resetResultView();
				if(!DictCommon.IsConnected(dictMain.getActivity()))
				{
					dictMain.setSearchMessage("Please check your internet connection. Unable to detect network");
				}
				else
				{
					dictMain.setSearchMessage("Error while finding meaning."+errorInfo);
				}
			}
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","unexpected exception");
		}
		dictMain=null;
	}
	@Override
	public void onCancelled()
	{
		dictMain=null;
		super.onCancelled();
	}

}
