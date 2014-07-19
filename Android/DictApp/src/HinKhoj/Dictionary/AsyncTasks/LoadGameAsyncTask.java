package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.HangmanGameInfo;
import HinKhoj.Dictionary.Helpers.Utils;
import HinKhoj.Dictionary.fragments.WordGuessGameFragment;
import android.os.AsyncTask;

public class LoadGameAsyncTask extends AsyncTask<Void,Long,HangmanGameInfo>
{
	private WordGuessGameFragment wgf=null;
	public LoadGameAsyncTask(WordGuessGameFragment wgf)
	{
		this.wgf=wgf;
	}

	@Override
	protected HangmanGameInfo doInBackground(Void... params)
	{
		HangmanGameInfo hgi=null;
		try
		{
			hgi=Utils.GetHangmanGameInfo();

		}
		catch(Exception e)
		{
			
			DictCommon.LogException(e);
		}
		return hgi;
	}

	@Override
	protected void onPostExecute(HangmanGameInfo hgi) 
	{
		try
		{
			if(hgi!=null)
			{
				this.wgf.showNewGame(hgi);
			}
			else
			{
				this.wgf.ShowHint("Error while getting hangman word. Please Try again");
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}


}
