package HinKhoj.Dictionary.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import android.os.Environment;
import android.util.Log;


public class OfflineDatabaseFileManager {

	public static String ReadFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		String databaseFilePath=GetDatabaseFilePath();
		String dbFilePath=databaseFilePath+"/"+fileName;
		BufferedReader br= new BufferedReader(new FileReader(dbFilePath));
		StringBuilder text= new StringBuilder();
		String line="";
		while((line=br.readLine())!=null)
		{
			text.append(line);
		}

		return text.toString();

	}

	public static String GetDatabaseFilePath() {
		// TODO Auto-generated method stub
		String dataDirectory="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else
		{
			dataDirectory=Environment.getDataDirectory().getAbsolutePath();
		}
		dataDirectory= dataDirectory+"/"+AppConstants.DictionaryunZipFolder;

		File unzipPathDir= new File(dataDirectory);
		if(!unzipPathDir.exists())
		{
			if(!unzipPathDir.mkdirs())
			{
				Log.v("hinkhoj","not able to create "+unzipPathDir);
			}
		}

		return dataDirectory;
	}


	public static String GetSqlLiteDatabaseFilePath()
	{
		String sqllitedbpath="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
			sqllitedbpath=dataDirectory+"/"+AppConstants.SqlLiteSDCardFolder+"/databases/"+AppConstants.DBFileName;
		}
		else
		{
			sqllitedbpath=Environment.getDataDirectory().getAbsolutePath()+"/data/HinKhoj.Dictionary/databases/"+AppConstants.DBFileName;
		}
		return sqllitedbpath;
	}


	private static void CheckOrCreatePath(String dirPath) {
		// TODO Auto-generated method stub
		try
		{
			File pathf= new File(dirPath);
			if(!pathf.exists())
			{
				if(!pathf.mkdirs())
				{
					Log.v("hinkhoj","not able to create "+pathf);
				}
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}

	public static String GetHMSqlLiteDatabaseFilePath()
	{
		String sqllitedbpath="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
			sqllitedbpath=dataDirectory+"/"+AppConstants.SqlLiteSDCardFolder+"/databases/hm.db";
		}
		else
		{
			sqllitedbpath=Environment.getDataDirectory().getAbsolutePath()+"/data/HinKhoj.Dictionary/databases/hm.db";
		}
		return sqllitedbpath;
	}


	public static String GetSqlLiteDatabaseFolderPath()
	{
		String sqllitedbpath="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
			sqllitedbpath=dataDirectory+"/"+AppConstants.SqlLiteSDCardFolder+"/databases";
		}
		else
		{
			sqllitedbpath=Environment.getDataDirectory().getAbsolutePath()+"/data/HinKhoj.Dictionary/databases";
		}
		CheckOrCreatePath(sqllitedbpath);
		return sqllitedbpath;
	}

	public static String GetSqlLiteDatabaseFolderPathv1()
	{
		String sqllitedbpath="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
			sqllitedbpath=dataDirectory+"/HinKhojDictSqlLite";
		}
		else
		{
			sqllitedbpath=Environment.getDataDirectory().getAbsolutePath()+"/data/HinKhoj.Dictionary/databases";
		}
		return sqllitedbpath;
	}

	public static String CreateSqlLiteDatabaseFolderPathv1Backup()
	{
		String sqllitedbpath="";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
			sqllitedbpath=dataDirectory+"/HinKhojDictSqlLiteBKUP";
		}
		CheckOrCreatePath(sqllitedbpath);
		return sqllitedbpath;
	}

	public static String GetZipFilePath()
	{
		return OfflineDatabaseFileManager.GetDatabaseFilePath()+"/"+AppConstants.DBFileName+".zip";
	}

	public static String GetAssetZipPath()
	{
		return "databases/"+AppConstants.DBFileName+".zip";
	}

	public static void MoveOldFiles(int currentVersion) {
		try
		{
			if(isSDCardWritable())
			{
				if(currentVersion==2)
				{
					//move DictLiteFolder.
					//create new directory structure
					String oldDBPath=GetSqlLiteDatabaseFolderPathv1();
					String oldMydbFilePath=oldDBPath+"/hkdictsettings.db";
					File oldMyFile= new File(oldMydbFilePath);
					if(oldMyFile.exists())
					{
						String newDBPath=GetSqlLiteDatabaseFolderPath();

						String oldDBBackupPath=CreateSqlLiteDatabaseFolderPathv1Backup();

						String mydbFilePath=newDBPath+"/"+DatabaseDoor.DATABASE_NAME;

						String oldBKMydbFilePath=oldDBBackupPath+"/hkdictsettings.db";



						Log.v("hinkhoj","old my db file exists at "+oldDBBackupPath);
						try
						{
							FileInputStream finMyOldDB=new FileInputStream(oldMydbFilePath);
							FileOutputStream foutmyDB= new FileOutputStream(mydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);
							foutmyDB.close();
							finMyOldDB.close();
							//create backup
							finMyOldDB=new FileInputStream(oldMydbFilePath);
							foutmyDB= new FileOutputStream(oldBKMydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);

							foutmyDB.close();
							finMyOldDB.close();

							//delete old folder
							cleanAllAppFiles(1);
							Log.v("hinkhoj","old files moved successfully");

						}
						catch(Exception e)
						{
							Log.v("hinkhoj","error copying files.");
							DictCommon.LogException(e);
						}

					}
				}
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}

	private static void cleanAllAppFiles(int appVersion) {

		if(appVersion==1)
		{
			if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			{
				//delete zip files
				String dataDirectory=Environment.getExternalStorageDirectory().getAbsolutePath();
				String zipPath=dataDirectory+"/HinKhojDict_1";
				deleteDirectory(zipPath);				
				deleteDirectory(dataDirectory+"/HinKhojDictSqlLite");
			}
		}

	}

	private static void deleteDirectory(String dirPath) {
		// TODO Auto-generated method stub
		try
		{
			File dirFile=new File(dirPath);
			if (dirFile.isDirectory()) {
				String[] children = dirFile.list();
				for (int i = 0; i < children.length; i++) {
					new File(dirFile, children[i]).delete();
				}
			}
			dirFile.delete();
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	private static boolean isSDCardWritable() {
		// TODO Auto-generated method stub
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}


}