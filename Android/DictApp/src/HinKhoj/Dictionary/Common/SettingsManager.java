package HinKhoj.Dictionary.Common;

import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SettingsManager {
	
	public static String HINDI_SUPPORT="hindi_support_key";
	public static String SEARCH_HISTORY="search_history_key";

	public static boolean IsHindiSupported(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences prefs= context.getSharedPreferences(HINDI_SUPPORT, 0);
		Boolean hindiSupport= prefs.getBoolean(HINDI_SUPPORT, !GetDefaultNeedHindiSupport());
	    return hindiSupport;
	}
	
	public static void setHindiSupported(Context context,Boolean hindiSupport) {
		// TODO Auto-generated method stub
		SharedPreferences prefs= context.getSharedPreferences(HINDI_SUPPORT, 0);
		SharedPreferences.Editor edit= prefs.edit();

		edit.putBoolean(HINDI_SUPPORT, hindiSupport);
		edit.commit();
		HindiCommon.HindiNotSupported=!hindiSupport;

	}


	public static boolean IsSearchHistoryEnabled(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences prefs= context.getSharedPreferences(SEARCH_HISTORY, 0);
		Boolean searchHistory= prefs.getBoolean(SEARCH_HISTORY, true);
	    return searchHistory;
	}
	
	public static void setSearchHistoryEnabled(Context context,boolean searchHistory) {
		// TODO Auto-generated method stub
		SharedPreferences prefs= context.getSharedPreferences(SEARCH_HISTORY, 0);
		SharedPreferences.Editor edit= prefs.edit();

		edit.putBoolean(SEARCH_HISTORY, searchHistory);
		edit.commit();
		DictCommon.SaveSearchHistory=searchHistory;
	}
	
	public static boolean GetDefaultNeedHindiSupport()
	{
		if(Build.VERSION.RELEASE.equalsIgnoreCase("4.0.4") || Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		{
				return false;
		}
		
		return true;	
	}

	public static void Initialize(Context context) {
		// TODO Auto-generated method stub

		HindiCommon.HindiNotSupported=!IsHindiSupported(context);
	
		DictCommon.SaveSearchHistory=IsSearchHistoryEnabled(context);
				
	}
	
	

}
