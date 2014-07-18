package HinKhoj.Dictionary.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import android.os.Build;
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


	public static String GetSqlLiteDatabaseFolderPath()
	{
		String sqllitedbpath="";

		Boolean useSDCard=false;
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
		{
			if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			{
				useSDCard=true;
			}
		}
		else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
		{

			if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			{
				int totalInternalMemory=OfflineDatabaseSetupManager.getTotalInternalMemory();
				int freeInternalMemory=OfflineDatabaseSetupManager.getFreeInternalMemory();
				int totalSDCardSize=OfflineDatabaseSetupManager.getTotalSDCardSize();
				if(totalInternalMemory<1024 && freeInternalMemory<500 && totalSDCardSize>250)
				{
					useSDCard=true;
				}
			}

		}

		if(useSDCard)
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

	public static String GetDatabaseFilePath() {
		// TODO Auto-generated method stub
		return GetSqlLiteDatabaseFolderPath();
	}

	public static String GetSqlLiteDatabaseFilePathV2()
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


	public static String GetSqlLiteDatabaseFilePath()
	{
		String sqllitedbpath=GetSqlLiteDatabaseFolderPath()+"/"+AppConstants.DBFileName;
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
		String sqllitedbpath=GetSqlLiteDatabaseFolderPath()+"/hm.db";
		return sqllitedbpath;
	}

	public static String GetSqlLiteDatabaseFolderPathV2()
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

				if(currentVersion==3)
				{
					//move DictLiteFolder.
					//create new directory structure
					String oldDBPathv1=GetSqlLiteDatabaseFolderPathv1();
					String oldMydbFilePathv1=oldDBPathv1+"/hkdictsettings.db";
					File oldMyFilev1= new File(oldMydbFilePathv1);

					String oldDBPathv2=GetSqlLiteDatabaseFolderPathV2();
					String oldMydbFilePathv2=oldDBPathv2+"/hkdictsettings.db";
					File oldMyFilev2= new File(oldMydbFilePathv2);


					String newDBPath=GetSqlLiteDatabaseFolderPath();


					String mydbFilePath=newDBPath+"/"+DatabaseDoor.DATABASE_NAME;

					if(oldMyFilev1.exists())
					{

						String oldDBBackupPath=CreateSqlLiteDatabaseFolderPathv1Backup();

						String oldBKMydbFilePath=oldDBBackupPath+"/hkdictsettings.db";


						Log.v("hinkhoj","old my db file exists at "+oldDBBackupPath);
						try
						{
							FileInputStream finMyOldDB=new FileInputStream(oldMydbFilePathv1);
							FileOutputStream foutmyDB= new FileOutputStream(mydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);
							foutmyDB.close();
							finMyOldDB.close();
							//create backup
							finMyOldDB=new FileInputStream(oldMydbFilePathv1);
							foutmyDB= new FileOutputStream(oldBKMydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);

							foutmyDB.close();
							finMyOldDB.close();

							Log.v("hinkhoj","old files moved successfully");

						}
						catch(Exception e)
						{
							Log.v("hinkhoj","error copying files.");
							DictCommon.LogException(e);
						}

					}
					else if(!newDBPath.equalsIgnoreCase(oldDBPathv2) && oldMyFilev2.exists())
					{

                        Log.v("hinkhoj","Moving files from v2 to v3");
						String oldBKMydbFilePath=oldDBPathv2+"/hkdictsettings-bkup.db";

						try
						{
							DictCommon.tryCloseMyDb();
							FileInputStream finMyOldDB=new FileInputStream(oldMydbFilePathv2);
							FileOutputStream foutmyDB= new FileOutputStream(mydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);
							foutmyDB.close();
							finMyOldDB.close();


							//create backup
							finMyOldDB=new FileInputStream(oldMydbFilePathv2);
							foutmyDB= new FileOutputStream(oldBKMydbFilePath);
							OfflineDatabaseSetupManager.copyFile(finMyOldDB, foutmyDB);

							foutmyDB.close();
							finMyOldDB.close();

							Log.v("hinkhoj","v2: old files moved successfully");

						}
						catch(Exception e)
						{
							Log.v("hinkhoj","v2 upgrade: error copying files.");
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

		try
		{
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
		catch(Exception e)
		{
			DictCommon.LogException(e);
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