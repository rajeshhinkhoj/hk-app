package HinKhoj.Dictionary.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppAccountManager {
	public static String ADS_ENABLE="ads_enable";
      public static boolean isAdsEnabled(Context context)
      {
    		SharedPreferences prefs= context.getSharedPreferences(ADS_ENABLE, 0);
    		Boolean adsEnabled= prefs.getBoolean(ADS_ENABLE, true);
    	    return adsEnabled;
      }
      
      public static void setAdsStatus(Context context,boolean ads_status)
      {
    	  SharedPreferences prefs= context.getSharedPreferences(ADS_ENABLE, 0);
  		  SharedPreferences.Editor edit= prefs.edit();
  		  edit.putBoolean(ADS_ENABLE, ads_status);
  		  edit.commit();
      }
}
