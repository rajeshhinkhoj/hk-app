package HinKhoj.Dictionary.AsyncTasks;
import java.io.IOException;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;
import android.content.Context;
import android.os.AsyncTask;

public class HangmanConfigureTaskAsync extends AsyncTask<Void,Integer,Void> {
	private Context context=null;
	public HangmanConfigureTaskAsync(Context context)
	{
		this.context=context;
		
	}
	
	@Override
	protected void onPreExecute()
	{
		
	}
    @Override
	protected Void doInBackground(Void... arg0) {
    	try {
			OfflineDatabaseSetupManager.UnCompressHangman(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    @Override
	protected void onProgressUpdate(Integer... progress) {
        //
    }

    @Override
	protected void onPostExecute(Void result) {
    	//Intent online=new Intent(context,HangmanActivity.class);
		//context.startActivity(online);
    }
	
}

