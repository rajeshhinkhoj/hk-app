package HinKhoj.Dictionary.AsyncTasks;
import com.google.android.gms.ads.AdView;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import android.os.AsyncTask;

import com.google.android.gms.ads.AdRequest;

public class AdRefreshTaskAsync extends AsyncTask<Void,Integer,Void> {
	private AdView av=null;
	public AdRefreshTaskAsync(AdView av)
	{
		this.av=av;
		
	}
    @Override
	protected Void doInBackground(Void... args) {
    	try {
    		if(AppConstants.IN_APP_BILLING_TEST)
			{
				AdRequest ar= new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("F25E1C23024A621EF37B04ED579BD517")
				.addTestDevice("7CABB93A0C2B2F2AA3B793479F90B652")
				.addTestDevice("1BA41179458EF54F704409506E764B34")
				.addTestDevice("E4BEE38D7A51DEA3975747B00D7C0B51")
				.build();
				av.loadAd(ar);
			}
			else
			{
				AdRequest ar= new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();
				av.loadAd(ar);
			}
    		
		} 
    	catch (Exception e) {
			DictCommon.LogException(e);
		}
    	return null;
    }

    @Override
	protected void onProgressUpdate(Integer... progress) {
        //
    }

    @Override
	protected void onPostExecute(Void result ) {
    	//
    }
	
}

