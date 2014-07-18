package HinKhoj.Dictionary.Common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import HinKhoj.Dictionary.Constants.AppConstants;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;

public class OfflineDictCommon {
	

	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}


	@SuppressLint("NewApi")
	public static boolean isValidDownloadId(Context context,long download_id)
	{
		try
		{
			DownloadManager.Query query = new DownloadManager.Query();
			SharedPreferences prefs= context.getSharedPreferences(AppConstants.DICT_DOWNLOAD_ID, 0);
			long enqueue =prefs.getLong(AppConstants.DICT_DOWNLOAD_ID, 0);
			query.setFilterById(enqueue);
			DownloadManager dm=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor cursor = dm.query(query);
			if(cursor.getCount()<1)
			{
				cursor.close();
				return false;
			}


			if(cursor.moveToFirst()){
				int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int status = cursor.getInt(columnIndex);
				int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
				int reason = cursor.getInt(columnReason);

				if(status == DownloadManager.STATUS_PAUSED || status==DownloadManager.STATUS_PENDING 
						|| status==DownloadManager.STATUS_RUNNING){
					cursor.close();
					return true;
				}
			}
			cursor.close();


		}
		catch(Exception e)
		{
			//
		}
		return false;

	}
	
	public static boolean isDownloadManagerSupported()
	{
		if(Build.VERSION.SDK_INT >8)
		{
			//Log.v("hinkhoj","download manager supported");
			return true;
		}

		//Log.v("hinkhoj","download manager not supported");
	
		return false;
	}
}
