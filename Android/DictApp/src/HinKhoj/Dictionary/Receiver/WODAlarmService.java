package HinKhoj.Dictionary.Receiver;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Services.WODService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class WODAlarmService extends BroadcastReceiver 
{
 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		 //Log.v("hinkhoj","Alarm received");
		 if(!DictCommon.getNotification(context))
    	 {
    		 return;
    	 }
    	 //check if connected
    	 if(DictCommon.IsConnected(context))
    	 {
    		 Intent startServiceIntent = new Intent(context, WODService.class);
    		 context.startService(startServiceIntent);
    	 }	   
	}
};
