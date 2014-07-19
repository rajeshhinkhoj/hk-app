package HinKhoj.Dictionary.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import HinKhoj.Dictionary.Helpers.OfflineZipDecompress;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.ProgressBar;

public class OfflineDatabaseSetupManager {

	public synchronized static void UnCompressDictionary(ProgressBar progressManager,Context context) throws IOException {
		// TODO Auto-generated method stub
		try
		{
			String copyzipPath=OfflineDatabaseFileManager.GetDatabaseFilePath();

			File hkDictPath= new File(copyzipPath);
			if(!hkDictPath.exists())
			{
				if(!hkDictPath.mkdirs())
				{

					
					if(progressManager!=null)
					{
						progressManager.setProgress(progressManager.getMax());
					}
					return;		

				}		
			}

		//	Log.v("hinkhoj","Directory successfully created.."+hkDictPath.getAbsolutePath());
			//Log.v("copy","Start copy assets..");
			OfflineDatabaseSetupManager.CopyAssetsDictDb(context);
			//Log.v("copy","End copy assets..");
			String unzipPath=OfflineDatabaseFileManager.GetSqlLiteDatabaseFolderPath();
			File unzipPathDir= new File(unzipPath);
			if(!unzipPathDir.exists())
			{
				if(!unzipPathDir.mkdirs())
				{
					//Log.v("hinkhoj","not able to create "+unzipPath);
				}
			}
			//uncompress
			OfflineZipDecompress decompressor= new OfflineZipDecompress(OfflineDatabaseFileManager.GetZipFilePath(), unzipPath,progressManager);
			decompressor.unzip();
			if(progressManager!=null)
			{
				progressManager.setProgress(50);
			}

		}
		catch(Exception e)
		{
			if(progressManager!=null)
			{

				DictCommon.LogException(e);
				progressManager.setProgress(50);

			}
		}

	}
	public static void UnCompressHangman(Context context) throws IOException {
		// TODO Auto-generated method stub
		try
		{
			String copyzipPath=OfflineDatabaseFileManager.GetDatabaseFilePath();

			File hkDictPath= new File(copyzipPath);
			if(!hkDictPath.exists())
			{
				if(!hkDictPath.mkdirs())
				{

					//Log.v("hinkhoj","Not able to create "+hkDictPath.getAbsolutePath());
					return;		

				}		
			}

			//		Log.v("copy","Start copy assets..");
			OfflineDatabaseSetupManager.CopyAssets(context);
			//		Log.v("copy","End copy assets..");
			String unzipPath=OfflineDatabaseFileManager.GetSqlLiteDatabaseFolderPath();
			File unzipPathDir= new File(unzipPath);
			if(!unzipPathDir.exists())
			{
				if(!unzipPathDir.mkdirs())
				{
					//Log.v("hinkhoj","not able to create "+unzipPath);
				}
			}
			//uncompress
			OfflineZipDecompress decompressor= new OfflineZipDecompress(hkDictPath+"/hm.db.zip", unzipPath,null);
			decompressor.unzip();

		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}


	public static boolean IsDictionaryUnCompressed() {

		File hkDictPath= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath());
		if(!hkDictPath.exists())
		{
			return false;
		}
		if(hkDictPath.length()<AppConstants.DBFileSize)
		{
			return false;
		}
		//check if dict uncompress success file present
		return true;
	}
	public static void CopyAssets(Context context) {
		AssetManager assetManager = context.getAssets();
		String zipCopyPath=OfflineDatabaseFileManager.GetDatabaseFilePath();


		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open("games/hm.db.zip");
			out = new FileOutputStream(zipCopyPath+"/" + "hm.db.zip");
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch(Exception e) {
			DictCommon.LogException(e);
		}       
	}

	public static void CopyAssetsDictDb(Context context) {
		AssetManager assetManager = context.getAssets();


		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(OfflineDatabaseFileManager.GetAssetZipPath());
			out = new FileOutputStream(OfflineDatabaseFileManager.GetZipFilePath());
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch(Exception e) {
			DictCommon.LogException(e);
		}       
	}


	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}


	public static void DeleteDictDatabase() {
		// TODO Auto-generated method stub
		try
		{
		
			File hkDbPath= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath());
			hkDbPath.delete();
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

		try
		{
			File hkDbJournalPath= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath()+"-journal");
			hkDbJournalPath.delete();
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		
		try
		{
			File hkZipDbPath= new File(OfflineDatabaseFileManager.GetZipFilePath());
			hkZipDbPath.delete();
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

		
	}
	public static List<String> GetConfigurationTestResult(Context context) {
		// TODO Auto-generated method stub
		List<String> testResults= new ArrayList<String>();

		//test 1: Is SD card available.
		String sdcardState=android.os.Environment.getExternalStorageState();
		if(sdcardState.equals(android.os.Environment.MEDIA_MOUNTED) || sdcardState.equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			testResults.add("SD Card available: <font color='green'> Yes</font>");
		}
		else
		{
			testResults.add("SD Card available: <font color='red'> No</font>");
		}

		//test 2: is SD card writable 

		if(sdcardState.equals(android.os.Environment.MEDIA_MOUNTED))
		{
			testResults.add("SD Card Writable: <font color='green'> Yes</font>");
		}
		else
		{
			testResults.add("SD Card writable: <font color='red'> No</font>");
		}

		//test 3: Is 35MB space available on sd card.

		if(sdcardState.equals(android.os.Environment.MEDIA_MOUNTED) || sdcardState.equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY))
		{
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		int spaceinMB=0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){ 			
			long sdAvailSize = stat.getAvailableBlocksLong() * stat.getBlockSizeLong(); 
			spaceinMB=(int)sdAvailSize/(1024*1024);
		} 		
		else{ 			
			@SuppressWarnings("deprecation") 			
			double sdAvailSize = (double)stat.getAvailableBlocks() * (double)stat.getBlockSize(); 
			spaceinMB=(int)sdAvailSize/(1024*1024);
		}
		
		if(spaceinMB >35)
		{
			testResults.add("Free Space above 35 MB available: <font color='green'> Yes</font>");
		}
		else
		{
			testResults.add("Free Space above 35 MB available: <font color='red'> No</font>");
		}
		}
		else
		{
			testResults.add("Free Space above 35 MB available: <font color='red'> No</font>");
		}

		return testResults;

	}
	
	public static int getTotalInternalMemory() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		int spaceinMB=0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){ 			
			long sdAvailSize = stat.getBlockCountLong()*stat.getBlockSizeLong();
			spaceinMB=(int)sdAvailSize/(1024*1024);
		} 		
		else{ 			
			@SuppressWarnings("deprecation") 			
			double sdAvailSize = (double)stat.getBlockCount() * (double)stat.getBlockSize(); 
			spaceinMB=(int)sdAvailSize/(1024*1024);
		}
		return spaceinMB;
	
	}
	public static int getFreeInternalMemory() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		int spaceinMB=0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){ 			
			long sdAvailSize = stat.getAvailableBlocksLong()*stat.getBlockSizeLong();
			spaceinMB=(int)sdAvailSize/(1024*1024);
		} 		
		else{ 			
			@SuppressWarnings("deprecation") 			
			double sdAvailSize = (double)stat.getAvailableBlocks() * (double)stat.getBlockSize(); 
			spaceinMB=(int)sdAvailSize/(1024*1024);
		}

		return spaceinMB;
	}
	public static int getTotalSDCardSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		int spaceinMB=0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){ 			
			long sdAvailSize = stat.getBlockCountLong()*stat.getBlockSizeLong();
			spaceinMB=(int)sdAvailSize/(1024*1024);
		} 		
		else{ 			
			@SuppressWarnings("deprecation") 			
			double sdAvailSize = (double)stat.getBlockCount() * (double)stat.getBlockSize(); 
			spaceinMB=(int)sdAvailSize/(1024*1024);
		}
		return spaceinMB;
		
	}
}
