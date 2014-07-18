package HinKhoj.Dictionary.AsyncTasks;

import java.io.File;
import org.json.JSONException;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.wordofthedayresultdata;
import HinKhoj.Dictionary.fragments.MainFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetWordOfDayMainAsyncTask extends AsyncTask<Void,Long,wordofthedayresultdata> 
{
	private static MainFragment DictStart=null;
	private static	File extStorageAppBasePath=null;
	private static File extStorageAppCachePath=null;
	private String WordResultJSon="";
	private wordofthedayresultdata WRD=null;
	private boolean flag=true;
	private String[][] result=null;
	public static ProgressDialog dialog=null;
	public  GetWordOfDayMainAsyncTask(MainFragment DictStart)
	{
		GetWordOfDayMainAsyncTask.DictStart=DictStart;  	
	}
	@Override
	protected void onPreExecute()
	{
		//
	}
	@Override
	protected wordofthedayresultdata doInBackground(Void...Voids)
	{
		String url="http://dict.hinkhoj.com/WebServices/getdailyword.php";
		DictCommon.setupDatabase(DictStart.getFragmentContext());
		try {
			WRD=DictCommon.GetWordOfDay(DictStart.getFragmentContext());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  WRD;		
	}
	@Override
	protected void onPostExecute(wordofthedayresultdata worddata)
	{
		try{
			result=DictCommon.GetDisplayResultForWOD(worddata);
			GetWordOfDayMainAsyncTask.DictStart.initializeWOD(result);
		}
		catch(Exception e)
		{
			
		}
		
	}
	

}
