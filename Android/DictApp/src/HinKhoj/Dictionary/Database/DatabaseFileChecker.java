package HinKhoj.Dictionary.Database;

import java.io.File;

import HinKhoj.Dictionary.Common.DictCommon;

public class DatabaseFileChecker {
public static boolean IsDictionaryFileAvailable() {
		
		File hkDictPath= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath());
		if(!hkDictPath.exists())
		{
			return false;
		}
		return true;
    }

public static void DeleteDictionaryDb() {
	
	File hkDictPath= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath());
	if(hkDictPath.exists())
	{
		hkDictPath.delete();
	}
	
	
	try
	{
	
		File hkDictPathWal= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath()+"-wal");
		if(hkDictPathWal.exists())
		{
			hkDictPathWal.delete();
		}
		

		File hkDictPathShm= new File(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath()+"-shm");
		if(hkDictPathShm.exists())
		{
			hkDictPathShm.delete();
		}
		
	}
	catch(Exception e)
	{
		DictCommon.LogException(e);
	}
	
	
}

}
