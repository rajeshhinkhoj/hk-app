package HinKhoj.Dictionary.AsyncTasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import HinKhoj.Dictionary.DataModel.DictionaryWordofthedayData;
import HinKhoj.Dictionary.DataModel.wordofthedayresultdata;
import HinKhoj.Dictionary.Helpers.DictionaryUrlReader;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class notificationcontent extends AsyncTask<Void,Long,wordofthedayresultdata> 
{
	private static Activity DictStart=null;
	private static	File extStorageAppBasePath=null;
	private static File extStorageAppCachePath=null;
	private String WordResultJSon="";
	private wordofthedayresultdata WRD=null;
	private boolean flag=true;
	private String[][] result=null;
	public  notificationcontent(Activity DictStart)
	{
		notificationcontent.DictStart=DictStart;  	
	}
	@Override
	protected wordofthedayresultdata doInBackground(Void...Voids)
	{
		String url="http://dict.hinkhoj.com/WebServices/getdailyword.php";
		try 
		{
			if(IsCacheFileAvailable())
			{
				WordResultJSon=CacheFileData();
				if(WordResultJSon.length()>2)
				{
					DictionaryWordofthedayData[] Rd=notificationcontent.GetWordResultDataFromJson(WordResultJSon);
					WRD=new wordofthedayresultdata(Rd);
					SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
					d.setTimeZone(TimeZone.getTimeZone("asia/calcutta"));
					String currentdate=d.format(new Date());
					if(WRD!=null && WRD.dictDataList.length>0)
					{
						for(int i=0;i<WRD.dictDataList.length;i++)
						{
							if(WRD.dictDataList[i].date.equals(currentdate))
							{
								flag=false;
							}
						}
					}    		    
				}
				if(flag)
				{
					DictionaryUrlReader.ReadURLContent(url,notificationcontent.DictStart);
					if(IsCacheFileAvailable())
					{
						WordResultJSon=CacheFileData();
						if(WordResultJSon.length()>2)
						{
							DictionaryWordofthedayData[] Rd=notificationcontent.GetWordResultDataFromJson(WordResultJSon);
							WRD=new wordofthedayresultdata(Rd);
						}
					}
				}
			}
			else
			{

				DictionaryUrlReader.ReadURLContent(url,notificationcontent.DictStart);
				if(IsCacheFileAvailable())
				{
					WordResultJSon=CacheFileData();
					if(WordResultJSon.length()>2)
					{
						DictionaryWordofthedayData[] Rd=notificationcontent.GetWordResultDataFromJson(WordResultJSon);
						WRD=new wordofthedayresultdata(Rd);
					}
				}
			}
		}
		catch (IOException e) {}
		catch(Exception e){}
		return  WRD;		
	}
	@Override
	protected void onPostExecute(wordofthedayresultdata worddata)
	{
		try{
			SharedPreferences prefs = DictStart.getSharedPreferences("hkdictnotify", 0);
			SharedPreferences.Editor editor = prefs.edit();
			Long date_notify = prefs.getLong("date_notify", 0);
			if (System.currentTimeMillis() >= date_notify + 
					(24 * 60 * 60 * 1000)) {
				result=new String[1][5];
				if(worddata!=null && worddata.dictDataList.length>0)
				{
					result[0][0]=worddata.dictDataList[0].word;
					result[0][1]=worddata.dictDataList[0].example;
					result[0][2]=worddata.dictDataList[0].hin_word;
					result[0][3]=worddata.dictDataList[0].hexample;
					result[0][4]=worddata.dictDataList[0].date;
				//	notificationhelper nh=new notificationhelper(notificationcontent.DictStart);   
					//nh.createNotification(result);
				}
				editor.putLong("date_notify", System.currentTimeMillis());
			}
			editor.commit();
		} catch(Exception e){}
	}
	private static DictionaryWordofthedayData[] GetWordResultDataFromJson(String WordResultJSon) throws JSONException 
	{		
		DictionaryWordofthedayData[] Wd=null;
		try
		{
			Wd=new Gson().fromJson(WordResultJSon,DictionaryWordofthedayData[].class);
		}
		catch (JsonSyntaxException e){} 
		catch (JsonIOException e){}
		catch(Exception e){}
		return Wd;
	}  
	public static String CacheFileData()
	{
		String aBuffer = "";	
		StringBuilder total = new StringBuilder();
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			File externalStorageDir=Environment.getExternalStorageDirectory();
			if(externalStorageDir!=null)
			{
				extStorageAppBasePath=new File(externalStorageDir.getAbsolutePath()+File.separator+"Android"+File.separator+"data"+File.separator+"HinKhoj"+File.separator+"Wordoftheday");	  
				if(extStorageAppBasePath!=null)
				{
					extStorageAppCachePath=new File(extStorageAppBasePath.getAbsolutePath()+File.separator+"Cache");    	
					boolean isCachePathAvailable=true;
					if(!extStorageAppCachePath.exists())
					{ 
						isCachePathAvailable=extStorageAppCachePath.mkdirs();  
					}
					if(!isCachePathAvailable)
					{
						extStorageAppCachePath=null;
					}
				}	  
				if(extStorageAppCachePath!=null)
				{		    		
					FileInputStream fIn;
					try 
					{
						String aDataRow = "";
						fIn = new FileInputStream(extStorageAppCachePath+"/wordoftheday.txt");
						BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
						while ((aDataRow = myReader.readLine()) != null)
						{
							//  aBuffer += aDataRow;
							total.append(aDataRow);
						}     
						myReader.close();
						aBuffer=total.toString();
					}
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			try 
			{

				FileInputStream file=notificationcontent.DictStart.getApplicationContext().openFileInput("wordoftheday.txt");
				int size=file.available();
				byte[] buffer=new byte[size];	  
				file.read(buffer);
				file.close();
				aBuffer=new String(buffer);	
			} 
			catch (FileNotFoundException e) {} 
			catch (IOException e) {}		 
		}
		return aBuffer;
	}
	public static boolean IsCacheFileAvailable() throws FileNotFoundException
	{  
		boolean flag=false;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			File externalStorageDir=Environment.getExternalStorageDirectory();
			if(externalStorageDir!=null)
			{
				extStorageAppCachePath=new File(externalStorageDir.getAbsolutePath()+File.separator+"Android"+File.separator+"data"+File.separator+"HinKhoj"+File.separator+"Wordoftheday"+File.separator+"Cache");	  
				if(extStorageAppCachePath!=null)
				{
					File check =new File(extStorageAppCachePath,"wordoftheday.txt");
					if(check.exists())
					{
						flag=true;
					}
				}
			} 	      	   
		}
		else if(!flag)
		{
			File file=notificationcontent.DictStart.getApplicationContext().getFileStreamPath("wordoftheday.txt");
			if(file.exists())
			{
				flag=true;
			}

		}
		if(flag)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
