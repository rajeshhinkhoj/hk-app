package HinKhoj.Dictionary.AsyncTasks;

import java.net.UnknownHostException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.SpellCheckCommon;
import HinKhoj.Dictionary.DataModel.SpellCheckResultData;
import HinKhoj.Dictionary.fragments.SpellCheckingFragment;
import android.os.AsyncTask;

public class SpellCheckSearchTask extends AsyncTask<String,Long, SpellCheckResultData> {
	private SpellCheckingFragment spellMain=null;
	private String errorInfo="";


	public SpellCheckSearchTask(SpellCheckingFragment spellMain)
	{
		this.spellMain=spellMain;
	}


	@Override
	protected SpellCheckResultData doInBackground(String... data) {
		// TODO Auto-generated method stub
		errorInfo="";
		try
		{
			SpellCheckResultData srd= SpellCheckCommon.GetSpellCheckResult(data[0]);
			return srd;
		}
		catch(UnknownHostException uh)
		{
			errorInfo="Please enable internet connection on your mobile/tablet.Report this issue to info@hinkhoj.com if your internet connection is working fine.<br/> कृपया इन्टरनेट कनेक्शन की जाँच कर ले | ";
		}
		catch(Exception e)
		{
			errorInfo="Error while checking spelling. Please check internet connection. Report it to info@hinkhoj.com. Below is detailed error data: <br/>"+e.toString();
		}
		return null;
	}

	@Override
	protected void onPostExecute(SpellCheckResultData srd)
	{
		try
		{
			if(srd!=null)
			{
				spellMain.DisplaySpellCheckResult(srd);
			}
			else
			{
				spellMain.setSpellCheckResult(errorInfo);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

}
