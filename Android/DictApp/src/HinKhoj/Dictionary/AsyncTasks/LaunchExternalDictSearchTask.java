package HinKhoj.Dictionary.AsyncTasks;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import android.os.AsyncTask;

public class LaunchExternalDictSearchTask extends  AsyncTask<String,Long,Void>
{

	private DictionaryMainActivity dictMain=null;
	private String errorInfo="";
	private String searchWord="";
	public LaunchExternalDictSearchTask(DictionaryMainActivity dictMain)
	{
		this.dictMain=dictMain;
	}
	@Override
	protected Void doInBackground(String... words) {
		// TODO Auto-generated method stub
		searchWord=words[0];
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		try
		{
			dictMain.LaunchMeaning(searchWord, dictMain.getIsOnline());
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}
	@Override
	public void onCancelled()
	{
		super.onCancelled();
	}

}
