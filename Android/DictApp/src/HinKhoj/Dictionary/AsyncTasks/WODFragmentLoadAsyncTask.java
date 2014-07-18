package HinKhoj.Dictionary.AsyncTasks;

import java.io.File;
import org.json.JSONException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.wordofthedayresultdata;
import HinKhoj.Dictionary.fragments.WordOfDayFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class WODFragmentLoadAsyncTask extends AsyncTask<Void,Long,wordofthedayresultdata> 
{
	private static WordOfDayFragment wodFragment=null;
	private static	File extStorageAppBasePath=null;
	private static File extStorageAppCachePath=null;
	private String WordResultJSon="";
	private wordofthedayresultdata WRD=null;
	private boolean flag=true;
	private String[][] result=null;
	public static ProgressDialog dialog=null;
	public  WODFragmentLoadAsyncTask(WordOfDayFragment wodFragment)
	{
		WODFragmentLoadAsyncTask.wodFragment=wodFragment;  	
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
		DictCommon.setupDatabase(wodFragment.getFragmentContext());
		try {
			WRD=DictCommon.GetWordOfDay(wodFragment.getFragmentContext());
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
			WODFragmentLoadAsyncTask.wodFragment.initializeWOD(result);
		}
		catch(Exception e)
		{
			
		}
		
	}
	

}
