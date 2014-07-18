package HinKhoj.Dictionary.Common;

import HinKhoj.Dictionary.Constants.AppConstants;

public class HinKhojLogger {
	public static void Log(String message)
	{
		if(AppConstants.EnableLogging)
		{
			//android.util.Log.v("hinkhoj",message);
		}
	}

}
