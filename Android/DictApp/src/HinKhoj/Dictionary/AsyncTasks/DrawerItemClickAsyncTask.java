package HinKhoj.Dictionary.AsyncTasks;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class DrawerItemClickAsyncTask extends  AsyncTask<Void,Void,Void>
{

	private DictionaryMainActivity dictMain=null;
	private String errorInfo="";
	private int groupPosition;
	protected static ProgressDialog dialog=null;
	public DrawerItemClickAsyncTask(DictionaryMainActivity dictMain,int groupPosition)
	{
		this.dictMain=dictMain;
		this.groupPosition=groupPosition;
	}
	@Override
	protected Void doInBackground(Void... groupPosition) {
		

		try
		{
			/*
			Looper.prepare();
			dialog=new ProgressDialog(dictMain);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setMessage("Loading ...");
			dialog.show();
			*/
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
			
			if(dialog!=null && dialog.isShowing())
			{
				dialog.dismiss();
				dialog=null;
			}
		}
		
		try {
			 Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			 Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		try
		{
			dictMain.selectItem(this.groupPosition);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		
		try {
			if(dialog!=null && dialog.isShowing())
			{
				dialog.dismiss();
				dialog=null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void onCancelled()
	{
		super.onCancelled();
	}

}
