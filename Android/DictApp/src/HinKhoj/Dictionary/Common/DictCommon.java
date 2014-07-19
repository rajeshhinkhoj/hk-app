package HinKhoj.Dictionary.Common;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.internal.aw;
import com.google.gson.Gson;

import HinKhoj.Hindi.Android.Common.AndroidHelper;
import HinKhoj.Hindi.Android.Common.Hindi2EngUtils;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import HinKhoj.Hindi.KeyBoard.HindiLiveTranslitate;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.DataModel.*;
import HinKhoj.Dictionary.Database.*;
import HinKhoj.Dictionary.activity.AboutUsActivity;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import HinKhoj.Dictionary.Application.DictApp;
import HinKhoj.Dictionary.Application.DictApp.TrackerName;
import HinKhoj.Dictionary.Config.HKSettings;
import HinKhoj.Dictionary.Constants.*;


public class DictCommon {
	public static DictResultData dictResultData=null;
	public static DatabaseDoor dbHelper=null;
	public static SQLiteDatabase offlinedb=null;
	public static SQLiteDatabase hmdb=null;
	public static Boolean IsOfflineDbAvailable=false;
	public static Map<String,List<String>> acMap= new HashMap<String,List<String>>();
	public static List<String> searchHistoryList= new ArrayList<String>();
	public static Boolean SaveSearchHistory=true;
	public static void initializeSettings(Context context)
	{
		try
		{
			SettingsManager.Initialize(context);

		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}

	/* database initialization */

	//Local database to be always present
	public static void setupDatabase(Context context)
	{
		try
		{
			if(dbHelper !=null)
			{
				return;
			}
			
			dbHelper= new DatabaseDoor(context);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	//offline dictionary database
	public static void setupOfflineDb()
	{
		try
		{

			setOfflineDbAvailability();
			if(!IsOfflineDbAvailable)
			{
				return;
			}
			if(offlinedb !=null && offlinedb.isOpen())
			{
				return;
			}
			offlinedb= SQLiteDatabase.openOrCreateDatabase(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath(), null); 	
		}
		catch(Exception  e)
		{
			DictCommon.LogException(e);
		}
	}

	public static void tryCloseOfflineDb()
	{
		try
		{
			if(offlinedb !=null)
			{
				if(offlinedb.isOpen())
				{
					offlinedb.close();
					offlinedb=null;
				}
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}

	public static void tryCloseMyDb() {
		// TODO Auto-generated method stub
		try
		{
			if( dbHelper !=null)
			{
				dbHelper.close();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	public static void setOfflineDbAvailability() {
		// TODO Auto-generated method stub
		String databaseFilePath=OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath();
		File dbFile= new File(databaseFilePath);
		if(dbFile.exists())
		{
			IsOfflineDbAvailable=true;
		}
	}

	//hangman database 
	public static void initializeHangmanDB() {
		// TODO Auto-generated method stub
		if(hmdb !=null && hmdb.isOpen())
		{
			return;
		}
		String databaseFilePath=OfflineDatabaseFileManager.GetHMSqlLiteDatabaseFilePath();
		File dbFile= new File(databaseFilePath);
		if(dbFile.exists())
		{
			//dbHelper= new SqliteDBHelper(this);
			hmdb=SQLiteDatabase.openOrCreateDatabase(OfflineDatabaseFileManager.GetHMSqlLiteDatabaseFilePath(), null);  
		}	
	}

	public static void tryCloseHmDb()
	{
		try
		{
			if(hmdb !=null)
			{
				if(hmdb.isOpen())
				{
					hmdb.close();
					hmdb=null;
				}
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

	}



	public static void LogException(Exception e) {
		
		 //Log.e("hinkhoj","Exception received",e);

	}

	public static DictResultData GetDictResult(Context context,String word) throws IOException, JSONException
	{
		if(dbHelper==null)
		{
			setupDatabase(context);
		}
		String resultJson="";
		//check in cache
		if(dbHelper !=null)
		{
			try
			{
				resultJson=dbHelper.searchByWordOnline(word);
			}
			catch(Exception e)
			{
				DictCommon.LogException(e);
			}
		}
		if(resultJson==null || resultJson=="")
		{
			if(!DictCommon.IsConnected(context) && DictCommon.IsOfflineDbAvailable)
			{
				try
				{
					resultJson=dbHelper.searchByWord(word);
				}
				catch(Exception e)
				{
					DictCommon.LogException(e);
				}
				if(resultJson==null || resultJson=="")
				{
					DictCompleteResult dcr=OfflineSearch(word);
					if(dcr!=null && dcr.main_result!=null && dcr.main_result.length>0)
					{
						try
						{
							resultJson=new Gson().toJson(dcr);
							if(SaveSearchHistory)
							{
								dbHelper.insertMeaningCache(word, resultJson,0);
							}
						}
						catch(Exception e)
						{
							DictCommon.LogException(e);
						}
						return new DictResultData(dcr);
					}
				}

			}
			else if(DictCommon.IsConnected(context))
			{


				String encodeWord=URLEncoder.encode(word,"UTF-8");
				String seekUrl=AppConstants.DICT_SEARCH_API+encodeWord;
				try
				{
					resultJson=NetworkCommon.ReadURLContent(seekUrl);
					if(resultJson!=null && resultJson!="")
					{
						try
						{
							if(SaveSearchHistory)
							{
								dbHelper.insertMeaningCache(word, resultJson,1);
							}
						}
						catch(Exception e)
						{
							DictCommon.LogException(e);
						}
					}
				}
				catch(Exception e)
				{
					DictCommon.LogException(e);
					try
					{
						resultJson=dbHelper.searchByWord(word);
					}
					catch(Exception e2)
					{
						DictCommon.LogException(e2);
					}
					if(resultJson==null || resultJson=="")
					{
						if(IsOfflineDbAvailable)
						{
							DictCompleteResult dcr=OfflineSearch(word);
							if(dcr!=null && dcr.main_result!=null && dcr.main_result.length>0)
							{
								try
								{
									resultJson=new Gson().toJson(dcr);
									if(SaveSearchHistory)
									{
										dbHelper.insertMeaningCache(word, resultJson,0);
									}
								}
								catch(Exception e2)
								{
									DictCommon.LogException(e2);
								}
								return new DictResultData(dcr);
							}
						}
					}

				}


			}

		}
		if(resultJson!=null && resultJson !="")
		{
			DictCompleteResult dwd=DictCommon.GetDictResultDataFromJson(resultJson);

			return new DictResultData(dwd);
		}
		else
		{
			return null;
		}
	}

	public static DictResultData GetDictResultOffline(Context context,String word,SQLiteDatabase database) throws IOException, JSONException
	{
		if(dbHelper==null)
		{
			setupDatabase(context);
		}
		String resultJson="";
		DictCompleteResult dcr=null;
		//check in cache
		if(dbHelper !=null)
		{
			try
			{
				resultJson=dbHelper.searchByWord(word);
			}
			catch(Exception e)
			{
				DictCommon.LogException(e);
			}
		}
		if(resultJson==null || resultJson=="")
		{
			dcr=OfflineSearch(word);
			if(dcr!=null && dcr.main_result!=null && dcr.main_result.length>0)
			{
				try
				{
					resultJson=new Gson().toJson(dcr);
					if(SaveSearchHistory)
					{
						dbHelper.insertMeaningCache(word, resultJson,0);
					}
				}
				catch(Exception e)
				{
					DictCommon.LogException(e);
				}
			}
			else
			{
				if(IsConnected(context))
				{
					String encodeWord=URLEncoder.encode(word,"UTF-8");
					String seekUrl=AppConstants.DICT_SEARCH_API+encodeWord;
					resultJson=AndroidHelper.ReadURLContent(seekUrl);
					if(resultJson!=null && resultJson!="")
					{
						try
						{
							if(SaveSearchHistory)
							{
								dbHelper.insertMeaningCache(word, resultJson,1);
							}
						}
						catch(Exception e)
						{
							DictCommon.LogException(e);
						}
						dcr=DictCommon.GetDictResultDataFromJson(resultJson);
					}
				}

			}
		}
		else
		{
			dcr=DictCommon.GetDictResultDataFromJson(resultJson);
		}
		return new DictResultData(dcr);
	}


	public static DictCompleteResult OfflineSearch(String word)
	{

		word=word.toLowerCase();
		String sql="";
		DictCompleteResult dcr= new DictCompleteResult();
		dcr.main_word=word;
		dcr.IsHindi=HindiCommon.IsHindi(word);
		// exact search...
		if(!HindiCommon.IsHindi(word))
		{

			sql="select hword,eword,egrammar,hep.rid from englishwordinfo as ewi inner join hindienglishpair as hep on hep.eid=ewi.eid inner join hindiwordinfo as hwi on hwi.hid=hep.hid inner join meaningattributes as ma on ma.rid=hep.rid where LOWER(ewi.eword) like '"+word+"%' order by LENGTH(eword) asc, hep.rating desc LIMIT 0,25";
			FillResult(sql,dcr);

		}
		else
		{

			sql="select hword,eword,egrammar,hep.rid from hindiwordinfo as hwi inner join hindienglishpair as hep on hep.hid=hwi.hid inner join englishwordinfo as ewi on ewi.eid=hep.eid inner join meaningattributes as ma on ma.rid=hep.rid where hwi.hword like '"+word+"%' order by LENGTH(hword) asc, hep.rating desc LIMIT 0,25";
			FillResult(sql,dcr);

		}

		return dcr;
	}

	private static void FillResult(String sql,
			DictCompleteResult dcr) {
		if(offlinedb==null)
		{
			setupOfflineDb();
		}
		Cursor cursor=null;
		try
		{ 
			cursor=offlinedb.rawQuery(sql,new String[]{});

			if(cursor.getCount()<1)
			{
				cursor.close();
				return;
			}
			dcr.main_result= new DictionaryWordData[cursor.getCount()];
			cursor.moveToFirst();
			int i=0;
			do
			{
				String hword=cursor.getString(0);
				String eword=cursor.getString(1);
				String egrammar=cursor.getString(2);
				int rid=cursor.getInt(3);
				cursor.moveToNext();
				DictionaryWordData dwd= new DictionaryWordData();
				dwd.hin_word=hword;
				dwd.eng_word=eword;
				dwd.eng_grammar=egrammar;
				dwd.rid=rid;
				dwd.htraslitate=Hindi2EngUtils.TranslateFromHindi2EngPractical(hword); 
				dcr.main_result[i++]=dwd;

			}while(!cursor.isAfterLast());
		}
		catch(Exception e)
		{
			LogException(e);
		}
		if(cursor!=null)
		{
			cursor.close();
		}
	}

	private static DictCompleteResult GetDictResultDataFromJson(
			String dictResultJSon) throws JSONException {		
		DictCompleteResult dwd=new Gson().fromJson(dictResultJSon, DictCompleteResult.class);
		return dwd;
	}


	public static DictionaryWordData GetDictionaryWordData(int meaningId)
	{
		if(dictResultData!=null && dictResultData.dictDataList !=null)
		{
			for(int i=0;i<dictResultData.dictDataList.size();i++)
			{

				DictionaryWordData dwd=dictResultData.dictDataList.get(i);
				if(dwd!=null && dwd.rid==meaningId)
				{
					return dwd;
				}
			}
		}
		return new DictionaryWordData();
	}

	public static List<DictionaryWordData> GetEng2EngResult(int meaningId)
	{
		if(dictResultData !=null)
		{
			return dictResultData.eng2eng_list;
		}
		return new ArrayList<DictionaryWordData>();
	}







	public static void setHindiFont(Context context, TextView tv) {
		// TODO Auto-generated method stub

		if(AndroidHelper.NeedHindiDisplay())
		{
			Typeface tf=HindiCommon.GetGargiTypeface(context);
			tv.setTypeface(tf);
		}

	}

	public static Boolean NeedHack404()
	{
		if(VERSION.SDK_INT==15)
		{
			return false;
		}
		return false;
	}

	public static String toCodeString(String line)
	{

		line=HindiCommon.ShiftLeftSmallE(line);
		String op="";
		if(line==null)
		{
			return "";
		}
		for (int index = 0; index < line.length(); index++) {

			char c=line.charAt(index);
			//op+=("&#" + hexCodeWithLeadingZeros+";");
			op+=("&#"+Character.codePointAt(line,index)+";");
			//op+=("&#2335;&#2376;&#2306;&#2325;&#2352;");
		}
		return op+"&#32;";
	}

	public static void PRateMeaning(Integer rid) throws IOException {
		// TODO Auto-generated method stub
		String seekUrl="http://dict.hinkhoj.com/WebServices/PRating.php?rid="+rid;
		AndroidHelper.ReadURLContent(seekUrl);
	}

	public static void NRateMeaning(Integer rid) throws IOException {
		// TODO Auto-generated method stub
		String seekUrl="http://dict.hinkhoj.com/WebServices/NRating.php?rid="+rid;
		AndroidHelper.ReadURLContent(seekUrl);
	}
	public static List<String> getSearchHistory() {
		// TODO Auto-generated method stub
		List<String> searchHistory=new ArrayList<String>();
		try
		{
			if(dbHelper !=null)
			{
				searchHistory=dbHelper.getAllSearchWords();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return  searchHistory;
	}

	public static List<String> getRecentSearchHistory() {
		// TODO Auto-generated method stub
		List<String> searchHistory=new ArrayList<String>();
		try
		{
			if(dbHelper !=null)
			{
				searchHistory=dbHelper.getRecentSearchWords(5);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return  searchHistory;
	}

	public static List<String> getSavedWords() {
		// TODO Auto-generated method stub
		List<String> searchHistory=new ArrayList<String>();
		try
		{
			if(dbHelper !=null)
			{
				searchHistory=dbHelper.getAllSavedWords();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return  searchHistory;
	}

	public static void SaveWord(String word,Boolean isHindi) {
		// TODO Auto-generated method stub
		try
		{
			if(dbHelper !=null)
			{
				dbHelper.insertSavedWord(word,isHindi);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	public static String[] GetDefaultAutoCompleteList()
	{
		searchHistoryList=getSearchHistory();
		if(searchHistoryList.size()>0)
		{
			return searchHistoryList.toArray(new String[searchHistoryList.size()]);
		}

		return new String[]{};

	}

	public static void cleanAcList()
	{
		acMap.clear();
	}

	public static String[] GetEngAutoCompleteList(String initialLetters)
	{
		initialLetters=initialLetters.toLowerCase();
		for(String key: acMap.keySet())
		{
			if(key!=null && key.startsWith(initialLetters))
			{
				return createAcList(key);
			}
		}
		String[] acList= new String[]{};
		try
		{
			if(offlinedb!=null)
			{
				List<String> acWords= new ArrayList<String>();
				Cursor c=null;
				try
				{
					c=offlinedb.rawQuery("SELECT eword FROM englishwordinfo where eword like '"+HindiCommon.ShiftRightSmallE(initialLetters)+"%'  order by length(eword) asc limit 0,25",null);
					if(c.getCount()>0)
					{
						c.moveToFirst();
						do
						{

							String word=c.getString(0);
							word=HindiCommon.ShiftLeftSmallE(word);
							acWords.add(word);
							c.moveToNext();
						}
						while(!c.isAfterLast());
					}
				}
				finally
				{
					if(c!=null)
					{
						c.close();
					}
				}

				if(acWords.size() >0)
				{
					acMap.put(initialLetters,acWords);
				}

				return createAcList(initialLetters);
			}
			else if(hmdb!=null)
			{
				List<String> acWords= new ArrayList<String>();
				Cursor c=null;
				try
				{
					c=hmdb.rawQuery("SELECT word FROM hangman_advance_game where word like '"+HindiCommon.ShiftRightSmallE(initialLetters)+"%'  order by length(word) asc limit 0,25",null);

					if(c.getCount()>0)
					{
						c.moveToFirst();
						do
						{

							String word=c.getString(0);
							word=HindiCommon.ShiftLeftSmallE(word);
							acWords.add(word);
							c.moveToNext();
						}
						while(!c.isAfterLast());
					}
				}
				finally
				{
					if(c!=null)
					{
						c.close();
					}
				}
				if(acWords.size() >0)
				{
					acMap.put(initialLetters,acWords);
				}
				return createAcList(initialLetters);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return acList;

	}


	public  static String GetFromEngToHindi(String s)
	{
		if(s!=null && s!="" && s.length()>2)
		{
			HindiLiveTranslitate hlt= new HindiLiveTranslitate();
			for(int i=0;i<s.length();i++)
			{
				hlt.ProcessKeyEvent(s.charAt(i));
			}
			return hlt.GetCurrentString();
		}

		return s;

	}
	private static String[] createAcList(String keyMain) {
		// TODO Auto-generated method stub
		List<String> globalList= new ArrayList<String>();
		try
		{
			List<String> keyRelated=acMap.get(keyMain);
			for(String word: keyRelated)
			{
				globalList.add(word);
			}
			
			for(String word: searchHistoryList)
			{

				globalList.add(word);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

		return globalList.toArray(new String[globalList.size()]);
	}

	public static String[] GetHinAutoCompleteList(String initialLetters)
	{
		initialLetters=initialLetters.toLowerCase();
		for(String key: acMap.keySet())
		{
			if(key!=null && key.startsWith(initialLetters))
			{
				return createAcList(key);
			}
		}
		String[] acList= new String[]{};
		try
		{
			if(offlinedb!=null)
			{
				List<String> acWords= new ArrayList<String>();
				Cursor c=null;
				try
				{
					c=offlinedb.rawQuery("SELECT hword FROM hindiwordinfo where hword like '"+initialLetters+"%' order by length(hword) asc limit 0,25  ",null);

					if(c.getCount()>0)
					{
						c.moveToFirst();
						do
						{

							String word=c.getString(0);
							word=HindiCommon.ShiftLeftSmallE(word);
							acWords.add(word);
							c.moveToNext();
						}
						while(!c.isAfterLast());
					}
				}
				finally
				{
					if(c!=null)
					{
						c.close();
					}
				}
				
				if(acWords.size() >0)
				{
					acMap.put(initialLetters,acWords);
				}
				return createAcList(initialLetters);
			}
			else if(hmdb!=null)
			{
				List<String> acWords= new ArrayList<String>();
				Cursor c=hmdb.rawQuery("SELECT hint FROM hangman_advance_game where hint like '"+initialLetters+"%' order by length(hint) asc limit 0,25  ",null);
				if(c.getCount()>0)
				{
					c.moveToFirst();
					do
					{

						String word=c.getString(0);
						word=HindiCommon.ShiftLeftSmallE(word);
						acWords.add(word);
						c.moveToNext();
					}
					while(!c.isAfterLast());
				}
				c.close();
				if(acWords.size() >0)
				{
					acMap.put(initialLetters,acWords);
				}
				return createAcList(initialLetters);
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return acList;

	}

	public static Boolean IsConnected(Context context)
	{
		try
		{
			final ConnectivityManager conMgr =  (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			} else {
				return false;
			}
		}
		catch(Exception e)
		{
			//
		}
		return true;
	}

	public static void DeleteSavedWord(String title) {
		// TODO Auto-generated method stub
		if(dbHelper!=null)
		{
			dbHelper.deleteSavedWord(title);
		}
	}

	public static void ExitApp() {
		// TODO Auto-generated method stub
		DictCommon.tryCloseOfflineDb();
		DictCommon.tryCloseHmDb();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void askFeedback(Activity innerActivity) {
		try
		{
			if(HKSettings.EnableFeedback)
			{
				Uri uri = Uri.parse(HKSettings.FeedbackUrl);
				Intent intent = new Intent (Intent.ACTION_VIEW, uri); 
				innerActivity.startActivity(intent);
			}
			else
			{
				Toast.makeText(innerActivity, "Please send your feedback to feedback@hinkhoj.com !!", Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(innerActivity, "Please send feedback to feedback@hinkhoj.com !!", Toast.LENGTH_LONG).show();
		}
	}

	public static void cleanAllSavedWords() {
		// TODO Auto-generated method stub
		if(dbHelper!=null)
		{
			dbHelper.removeAllSavedWords();
		}
	}

	public static void cleanAllSearchedWords() {
		// TODO Auto-generated method stub
		if(dbHelper!=null)
		{
			dbHelper.removeAllSearchedWords();
		}
	}

	public static void HandleException(Context context,
			Exception e, String opMsg) {
		// TODO Auto-generated method stub
		
		Toast.makeText(context, opMsg+" send mail to info@hinkhoj.com", Toast.LENGTH_LONG).show();

	}

	public static void DeleteSearchedWord(String title) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(dbHelper!=null)
		{
			dbHelper.deleteSearchedWord(title);
		}
	}


	public static wordofthedayresultdata GetWordOfDay(Context context) throws JSONException {
		// TODO Auto-generated method stub
		SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
		d.setTimeZone(TimeZone.getTimeZone("asia/calcutta"));
		String currentdate=d.format(new Date());
		boolean needDownload=false;
		String WordResultJSon=null; 
		try
		{
			if(dbHelper!=null)
			{
				WordResultJSon= dbHelper.getWordOfDay(currentdate);
				if(WordResultJSon==null)
				{
					needDownload=true;
				}

			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

		if(needDownload && DictCommon.IsConnected(context))
		{
			try
			{
				String url="http://dict.hinkhoj.com/WebServices/getdailyword.php";
				WordResultJSon=AndroidHelper.ReadURLContent(url);
				if(WordResultJSon.toString()!=null&&WordResultJSon.toString()!="")
				{
					if(dbHelper!=null)
					{
						dbHelper.saveWordOfDay(currentdate,WordResultJSon);
					}
				}
			}
			catch(Exception e)
			{
				//
			}
		}

		if(WordResultJSon!=null && WordResultJSon.length()>0)
		{
			DictionaryWordofthedayData[] Rd=GetWordResultDataFromJson(WordResultJSon);
			return new wordofthedayresultdata(Rd);
		}
		return null;
	}

	public static DictionaryWordofthedayData[] GetWordResultDataFromJson(String WordResultJSon) 
	{		
		DictionaryWordofthedayData[] Wd=null;
		try
		{
			Wd=new Gson().fromJson(WordResultJSon,DictionaryWordofthedayData[].class);
		}
		catch(Exception e){}
		return Wd;
	}  

	public static String[][] GetDisplayResultForWOD(wordofthedayresultdata worddata)
	{
		String[][] result=new String[1][5];
		if(worddata!=null&&worddata.dictDataList!=null)
		{   
			if(worddata.dictDataList.length>0)
			{
				result[0][0]=worddata.dictDataList[0].word;
				result[0][1]=worddata.dictDataList[0].example;
				result[0][2]=worddata.dictDataList[0].hin_word;
				result[0][3]=worddata.dictDataList[0].hexample;
				result[0][4]=worddata.dictDataList[0].date;
			}
			else
			{
				SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
				d.setTimeZone(TimeZone.getTimeZone("asia/calcutta"));
				String currentdate=d.format(new Date());

				result[0][0]="";
				result[0][1]="No Word of day Today.Please Check Your Internet Connectivity or mail info@hinkhoj.com";
				result[0][2]="";
				result[0][3]="";
				result[0][4]=currentdate;
			}
		}
		else
		{
			SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
			d.setTimeZone(TimeZone.getTimeZone("asia/calcutta"));
			String currentdate=d.format(new Date());

			result[0][0]="";
			result[0][1]="No Word of day Today.Please Check Your Internet Connectivity or mail info@hinkhoj.com";
			result[0][2]="";
			result[0][3]="";
			result[0][4]=currentdate;
		}
		return result;
	}

	public static boolean NeedWODDownload(Context context) {
		// TODO Auto-generated method stub
		if(!IsConnected(context))
		{
			return false;
		}
		if(!getNotification(context))
		{
			return false;
		}
		SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
		d.setTimeZone(TimeZone.getTimeZone("asia/calcutta"));
		String currentdate=d.format(new Date());
		try
		{
			if(dbHelper!=null)
			{
				String wodJson= dbHelper.getWordOfDay(currentdate);
				if(wodJson !=null)
				{
					return false;
				}
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		return true;
	}

	public static void setNotification(Context context,Boolean notify)
	{
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.WOD_NOTIFY_OPTION, 0);
		SharedPreferences.Editor edit= prefs.edit();
		if(notify)
		{
			edit.putLong(AppConstants.WOD_NOTIFY_OPTION, 1);
		}
		else
		{
			edit.putLong(AppConstants.WOD_NOTIFY_OPTION, 0);
		}
		edit.commit();
	}


	public static Boolean getNotification(Context context)
	{
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.WOD_NOTIFY_OPTION, 0);
		Long notify= prefs.getLong(AppConstants.WOD_NOTIFY_OPTION, 0);
		if(notify==1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Boolean isWODAlarmSet(Context context)
	{
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.WOD_ALARM_OPTION, 0);
		Long notify= prefs.getLong(AppConstants.WOD_ALARM_OPTION, -1);
		if(notify==-1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static void setWODAlarm(Context context)
	{
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.WOD_ALARM_OPTION, 0);
		SharedPreferences.Editor edit= prefs.edit();

		edit.putLong(AppConstants.WOD_ALARM_OPTION, 1);
		edit.commit();
	}

	public static boolean IsNotificationSet(Context context) {
		// TODO Auto-generated method stub

		SharedPreferences prefs= context.getSharedPreferences(AppConstants.WOD_NOTIFY_OPTION, 0);
		Long notify= prefs.getLong(AppConstants.WOD_NOTIFY_OPTION, -1);
		if(notify==-1)
		{
			return false;
		}
		return true;
	}

	public static void InitializeAds(Activity activity,int ad_id)
	{
		try
		{
			if(AppAccountManager.isAdsEnabled(activity))
			{

				AdView av= (AdView)activity.findViewById(ad_id);	
				av.setVisibility(View.VISIBLE);

				if(AppConstants.IN_APP_BILLING_TEST)
				{
					AdRequest ar= new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("F25E1C23024A621EF37B04ED579BD517")
					.addTestDevice("7CABB93A0C2B2F2AA3B793479F90B652")
					.addTestDevice("1BA41179458EF54F704409506E764B34")
					.addTestDevice("E4BEE38D7A51DEA3975747B00D7C0B51")
					.build();
					av.loadAd(ar);
				}
				else
				{
					AdRequest ar= new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.build();
					av.loadAd(ar);
				}
				if(!DictCommon.IsConnected(activity))
				{
					av.setVisibility(View.GONE);
				}
				else
				{
					av.setVisibility(View.VISIBLE);
				}
			}
			else
			{
				AdView av= (AdView)activity.findViewById(ad_id);	
				av.setVisibility(View.GONE);
			}
		}
		catch(Exception e)
		{
			LogException(e);
		}
	}




	public static MatchedWordsResultData GetSimilarWordsResult(String word) throws IOException, JSONException
	{   	
		String SpellCheckJSon="";

		@SuppressWarnings("deprecation")
		String encodeWord=URLEncoder.encode(word);
		String seekUrl="http://api.hinkhoj.com/WebServices/.php?word="+encodeWord;
		SpellCheckJSon=HinKhoj.Hindi.Android.Common.AndroidHelper.ReadURLContent(seekUrl);  
		MatchWordsData[] scd=GetMatchedWordsResultDataFromJson(SpellCheckJSon);
		return new MatchedWordsResultData(scd);
	}	    	

	private static MatchWordsData[] GetMatchedWordsResultDataFromJson(String jsonStr) throws JSONException 
	{		
		MatchWordsData[] scd=null;
		//scd=(MatchWordsData[])new Gson().fromJson(jsonStr,(MatchWordsData[]).class);
		return scd;

	}
	public static void InitializeHeaderBar(
			Activity activity) {
		// TODO Auto-generated method stub

	}
	public static void goToHome(Activity activity) {
		// TODO Auto-generated method stub
		Intent i= new Intent(activity,DictionaryMainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(i);
		activity.finish();
	}
	public static String GetGrammarConstant(String eng_grammar) {
		// TODO Auto-generated method stub
		if(eng_grammar.compareToIgnoreCase(StringConstants.NOUN)==0)
		{
			return StringConstants.NOUN;
		}

		if(eng_grammar.compareToIgnoreCase(StringConstants.VERB)==0)
		{
			return StringConstants.VERB;
		}

		if(eng_grammar.compareToIgnoreCase(StringConstants.ADJECTIVE)==0)
		{
			return StringConstants.ADJECTIVE;
		}

		if(eng_grammar.compareToIgnoreCase(StringConstants.PRONOUN)==0)
		{
			return StringConstants.PRONOUN;
		}

		return eng_grammar;
	}
	public static void hideKeyBoard(Context context,View view) {
		// TODO Auto-generated method stub
		InputMethodManager imm =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	public static void createIndexInOfflineDb(ProgressBar progressBar) {
		// TODO Auto-generated method stub

		if(offlinedb!=null)
		{
			offlinedb.close();
		}
		try
		{
			offlinedb= SQLiteDatabase.openDatabase(OfflineDatabaseFileManager.GetSqlLiteDatabaseFilePath(),null,SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS); 	

			String query="CREATE INDEX eword on englishwordinfo(eword)";
			offlinedb.execSQL(query);
			setProgress(progressBar,55);

			query="CREATE INDEX eid on englishwordinfo(eid)";
			offlinedb.execSQL(query);
			setProgress(progressBar,60);

			query="CREATE INDEX hword on hindiwordinfo(hword)";
			offlinedb.execSQL(query);
			setProgress(progressBar,70);

			query="CREATE INDEX hid on hindiwordinfo(hid)";
			offlinedb.execSQL(query);
			setProgress(progressBar,75);

			query="CREATE INDEX hidhep on hindienglishpair(hid)";
			offlinedb.execSQL(query);

			setProgress(progressBar,80);
			query="CREATE INDEX eidhep on hindienglishpair(eid)";
			offlinedb.execSQL(query);
			setProgress(progressBar,85);
			query="CREATE INDEX rid on meaningattributes(rid)";
			offlinedb.execSQL(query);
			setProgress(progressBar,90);
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		finally
		{
			if(offlinedb!=null)
			{
				offlinedb.close();
			}
			offlinedb=null;
		}

	}
	private static void setProgress(ProgressBar progressBar, int progress) {
		try
		{
			progressBar.setProgress(progress);
		}
		catch(Exception e)
		{
			LogException(e);
		}
	}

	public static void OpenWebApp(Activity activity) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String url="http://dict.hinkhoj.com";
		try
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			activity.startActivity(browserIntent);
		}
		catch(Exception e)
		{
			Toast.makeText(activity, "Unable to load web app", Toast.LENGTH_LONG).show();
		}

	}
	public static void openAboutUs(Activity dictionaryMainActivity) {
		// TODO Auto-generated method stub

		Intent i= new Intent(dictionaryMainActivity,AboutUsActivity.class);
		dictionaryMainActivity.startActivity(i);

	}
	public static void shareApp(Activity activity) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT,"HinKhoj Dictionary App Recommended");
			String text="Hi,\n\t I have been using Hinkhoj Dictionary app from long and I like it. I would recommend you to try it. \n Please visit http://dict.hinkhoj.com to install this application.\n\n";
			intent.putExtra(Intent.EXTRA_TEXT,text);

			activity.startActivity(Intent.createChooser(intent, "Share"));

		}catch(Exception e)
		{
			Toast.makeText(activity,"Error while sharing", Toast.LENGTH_LONG).show();
		}
	}

	public static String ExtractSearchWord(String searchWord) {
		if(searchWord==null || searchWord=="")
		{
			return "";
		}
		try
		{
			// TODO Auto-generated method stub
			Pattern p= Pattern.compile("meaning-of-(.*)-in-hindi.html");
			Matcher m=p.matcher(searchWord);
			if(m.find())
			{
				return m.group(1);
			}


			p= Pattern.compile("meaning-of-(.*)-in-english.html");
			m=p.matcher(searchWord);
			if(m.find())
			{
				return m.group(1);
			}

			p= Pattern.compile("meaning-of-(.*).html");
			m=p.matcher(searchWord);
			if(m.find())
			{
				return m.group(1);
			}
		}
		catch(Exception e)
		{
			LogException(e);
		}

		return searchWord;
	}

	public static void HandleCommonMenuItems(Activity activity,MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.share_menu:
			DictCommon.shareApp(activity);
			break;
		case R.id.feedback_menu:
			DictCommon.askFeedback(activity);
			break;	
		}
		return;
	}

	public static void exitApp(Activity activity) {
		// TODO Auto-generated method stub
		try
		{
			ExitApp();
			activity.finish();
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}    

	public static void listen_in_hindi(String word) {
		// TODO Auto-generated method stub
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource("http://dict.hinkhoj.com/pronunciation/get_hindi_tts.php?word="+URLEncoder.encode(word, "UTF-8"));
			mp.prepareAsync();
			mp.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//
		}

	}

	public static boolean isOnlineSearch(
			Context context) {
		Boolean isOnlineSearchDefault=false;
		if(!DictCommon.IsOfflineDbAvailable)
		{
			isOnlineSearchDefault=true;
		}
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.ISONLINE_PREF, 0);
		return prefs.getBoolean(AppConstants.ISONLINE_PREF, isOnlineSearchDefault);

	}

	public static void setOnlineSearch(Context context,Boolean isOnline)
	{
		SharedPreferences prefs= context.getSharedPreferences(AppConstants.ISONLINE_PREF, 0);
		SharedPreferences.Editor edit= prefs.edit();
		if(isOnline)
		{
			edit.putBoolean(AppConstants.ISONLINE_PREF, true);
		}
		else
		{
			edit.putBoolean(AppConstants.ISONLINE_PREF, false);
		}
		edit.commit();
	}

	public static void AddTrackEvent(Activity activity) {
		// TODO Auto-generated method stub
		try
		{
			// Get tracker.
			Tracker t = ((DictApp) activity.getApplication()).getTracker(
					TrackerName.APP_TRACKER);

			// Set screen name.
			// Where path is a String representing the screen name.
			t.setScreenName(activity.getClass().getSimpleName());

			// Send a screen view.
			t.send(new HitBuilders.AppViewBuilder().build());
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}
}
