package HinKhoj.Dictionary.Database;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.Common.DictCommon;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

public class DatabaseDoor extends SQLiteOpenHelper {
	public static final String DATABASE_NAME="hkdictsettings.db";
	private static final int SCHEMA_VERSION=1;
	private static final String ENCODING_SETTING = "PRAGMA encoding = 'UTF-8';";
	Context context=null;
	public DatabaseDoor(Context context) {
		super(context,GetDatabaseName(), null, SCHEMA_VERSION);
		this.context = context;	
	}
	private static String GetDatabaseName() {
		// TODO Auto-generated method stub
		String databasname=DATABASE_NAME;
		if(Build.VERSION.SDK_INT>7)
		{
			databasname=OfflineDatabaseFileManager.GetSqlLiteDatabaseFolderPath()+File.separator+ DATABASE_NAME;
		}

		return databasname;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		try
		{
			db.execSQL(ENCODING_SETTING);
			db.execSQL("CREATE TABLE meaningcache (word string PRIMARY KEY DESC,meaning TEXT,access_time datetime default CURRENT_TIMESTAMP,is_complete TINYINT);");
			db.execSQL("CREATE TABLE savedword (word string PRIMARY KEY DESC,saved_time datetime default CURRENT_TIMESTAMP,isHindi TINYINT);");
			db.execSQL("CREATE TABLE word_of_day (wod_date date PRIMARY KEY DESC,wod_data string)");
			//Log.v("database", "before creation new ");
		}
		catch(Exception e)
		{
			//
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if(newVersion > oldVersion)
		{

		}
	}
	public void dropDB()
	{

	}


	public void insertMeaningCache(String word, String meaning,int isComplete) {
		try
		{
			//delete old meaning
			deleteSearchedWord(word);
		}
		catch(Exception e)
		{
			//ignore
		}
		SQLiteDatabase db=null;
		try
		{
			ContentValues cv=new ContentValues();
			cv.put("word", word.toLowerCase());
			cv.put("meaning", meaning);
			cv.put("is_complete", isComplete);
			//Log.v("hinkhoj", "saving word="+word);
			db=getWritableDatabase();
			db.insert("meaningcache", "save", cv);

		}
		catch(Exception e)
		{
			Log.v("hinkhoj","error while saving word meaning"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}

	}
	public void closeDBInstance(SQLiteDatabase db)
	{
		try
		{
			if(db!=null)
			{
				//db.close();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	public void insertSavedWord(String word,Boolean isHindi)
	{
		SQLiteDatabase db=null;
		try
		{
			ContentValues cv=new ContentValues();
			cv.put("word", word.toLowerCase());
			if(isHindi)
			{
				cv.put("isHindi",1);
			}
			else
			{
				cv.put("IsHindi",0);
			}
			//Log.v("hinkhoj", "saving word="+word);
			db=getWritableDatabase();
			db.insert("savedword", "save", cv);
		}
		catch(Exception e)
		{
			Log.v("hinkhoj","error while saving word"+e.toString());
		}

		finally
		{
			closeDBInstance(db);
		}

	}

	public void deleteSavedWord(String word) {
		SQLiteDatabase db=null;

		try
		{
			db=getWritableDatabase();
			db.delete("savedword", "word"+"='"+word.toLowerCase()+"'", null);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			closeDBInstance(db);
		}
	}



	public void closeDB()
	{
		//getWritableDatabase().close();
	}

	public String searchByWord(String word) {
		SQLiteDatabase db=null;
		word=word.toLowerCase();
		// TODO Auto-generated method stub
		String meaningJson="";
		try
		{
			db=getReadableDatabase();
			Cursor c=(db.rawQuery("SELECT meaning FROM meaningcache where word = '"+word+"'",null));
			if(c.getCount()>0)
			{
				c.moveToFirst();
				meaningJson=c.getString(0);
			}
			c.close();
			db.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","error executing query"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return meaningJson;
	}
	

	public String searchByWordOnline(String word) {
		SQLiteDatabase db=null;
		word=word.toLowerCase();
		// TODO Auto-generated method stub
		String meaningJson="";
		try
		{
			db=getReadableDatabase();
			Cursor c=(db.rawQuery("SELECT meaning FROM meaningcache where word = '"+word+"' and is_complete='1'",null));
			if(c.getCount()>0)
			{
				c.moveToFirst();
				meaningJson=c.getString(0);
			}
			c.close();
			db.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","error executing query"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return meaningJson;
	}
	public List<String> getAllSearchWords() {
		SQLiteDatabase db=null;
		// TODO Auto-generated method stub
		List<String> searchWords= new ArrayList<String>();
		try
		{
			db=getReadableDatabase();
			Cursor c=(db.rawQuery("SELECT word FROM meaningcache where 1 order by access_time desc",null));
			if(c.getCount()>0)
			{
				c.moveToFirst();
				do
				{

					String word=c.getString(0);
					searchWords.add(word);
					c.moveToNext();
				}
				while(!c.isAfterLast());
			}
			c.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","search word related error"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return searchWords;
	}
	
	public List<String> getRecentSearchWords(int lim) {
		SQLiteDatabase db=null;
		// TODO Auto-generated method stub
		List<String> searchWords= new ArrayList<String>();
		try
		{
			db=getReadableDatabase();
			Cursor c=(db.rawQuery("SELECT word FROM meaningcache where 1 order by access_time desc limit 0,"+lim,null));
			if(c.getCount()>0)
			{
				c.moveToFirst();
				do
				{

					String word=c.getString(0);
					searchWords.add(word);
					c.moveToNext();
				}
				while(!c.isAfterLast());
			}
			c.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","search word related error"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return searchWords;
	}

	public List<String> getAllSavedWords() {
		SQLiteDatabase db=null;
		// TODO Auto-generated method stub
		List<String> savedWords= new ArrayList<String>();
		try
		{
			db=getReadableDatabase();
			Cursor c=(db.rawQuery("SELECT word FROM savedword where 1 order by saved_time desc ",null));
			if(c.getCount()>0)
			{
				c.moveToFirst();
				do
				{

					String word=c.getString(0);
					savedWords.add(word);
					c.moveToNext();
				}
				while(!c.isAfterLast());
			}
			c.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","saved word related error"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return savedWords;
	}

	public void removeAllSavedWords() {
		SQLiteDatabase db=null;
		try
		{
			// TODO Auto-generated method stub
			db=getWritableDatabase();
			db.delete("savedword",null, null);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			closeDBInstance(db);
		}
	}
	public void deleteSearchedWord(String word) {
		SQLiteDatabase db=null;
		try
		{
			// TODO Auto-generated method stub
			db=getWritableDatabase();
			db.delete("meaningcache", "word"+"='"+word.toLowerCase()+"'", null);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			closeDBInstance(db);
		}
	}
	public void removeAllSearchedWords() {
		SQLiteDatabase db=null;
		try
		{
			// TODO Auto-generated method stub
			db=getWritableDatabase();
			db.delete("meaningcache",null, null);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			closeDBInstance(db);
		}
	}
	public String getWordOfDay(String wod_date) {
		SQLiteDatabase db=null;
		//Log.v("hinkhoj","SELECT wod_data FROM word_of_day where wod_date = '"+wod_date+"'");
		
		String WordResultJSon=null;
		try
		{
			db=getReadableDatabase();
			Cursor c=db.rawQuery("SELECT wod_data FROM word_of_day where wod_date = '"+wod_date+"'",null);

			if(c.getCount()>0)
			{
				c.moveToFirst();
				WordResultJSon=c.getString(0);
			}
			c.close();
		}
		catch(Exception e)
		{
			//Log.v("hinkhoj","error getting wod from db"+e.toString());
		}
		finally
		{
			closeDBInstance(db);
		}
		return WordResultJSon;
	}
	public void saveWordOfDay(String currentdate, String wordResultJSon) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=null;
		try
		{
			ContentValues cv=new ContentValues();
			cv.put("wod_date", currentdate);
			cv.put("wod_data", wordResultJSon);
			db=getWritableDatabase();
			db.insert("word_of_day", "save", cv);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			closeDBInstance(db);
		}
	}
}
