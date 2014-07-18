package HinKhoj.Dictionary.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Environment;
public class DictionaryUrlReader {
	private static Context DictStart=null;
	private static	File extStorageAppBasePath;
    private static File extStorageAppCachePath;
	public static void ReadURLContent(String urlString,Context DictStart) throws IOException
	    {
		JSONObject json = new JSONObject();
		DictionaryUrlReader.DictStart=DictStart;
		URL url= new URL(urlString);
		StringBuilder data=new StringBuilder();
		final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	    urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
	    urlConnection.setRequestProperty("instanceFollowRedirects", "false");
	    urlConnection.connect();
		try 
		{
			
			InputStream inputStream=null;
			if("gzip".equals(urlConnection.getContentEncoding()))
			{
				inputStream= new GZIPInputStream(urlConnection.getInputStream());
			}
			else if("deflate".equals(urlConnection.getContentEncoding()))
			{
				inputStream= new InflaterInputStream(urlConnection.getInputStream());
			}
			else
			{
				inputStream=urlConnection.getInputStream();
			}
			InputStreamReader ISR=new InputStreamReader(inputStream,"UTF-8");
			
		    BufferedReader in = new BufferedReader(ISR);
		    //System.out.println("Encoding is "+urlConnection.getContentEncoding());
	            String inputLine="";
		    if(in!=null)
		    {
				   while ((inputLine = in.readLine()) != null) 
				   {
		               if(inputLine.length()>2)
					   {
						   data.append(inputLine);
					   }
		           }
				   String tmp=data.toString();
				   tmp=tmp.substring(1);
				   tmp=tmp.substring(0,tmp.length()-1);
				   
				   json=new JSONObject(tmp);
			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    urlConnection.disconnect();
		}
		if(json.toString()!=null&&json.toString()!="")
		{
		  SDCard_Cache_File("["+json.toString()+"]");
		}
	 }
	  
     public static void SDCard_Cache_File(String WordData)
       {
    	   
    	  try{
    	   if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
	       {
	    	   File externalStorageDir=Environment.getExternalStorageDirectory();
	    	   if(externalStorageDir!=null)
	    	   {
	    		 extStorageAppBasePath=new File(externalStorageDir.getAbsolutePath()+File.separator+"Android"+File.separator+"data"+File.separator+"HinKhoj"+File.separator+"Wordoftheday");	  
	    	   }
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
	    	   
	       }
	       if(extStorageAppCachePath!=null)
	       {
	    	 try 
			 {
				if(WordData.length()>4)
				{
			    FileOutputStream fout;		
				File file=new File(extStorageAppCachePath,"wordoftheday.txt");
				if(file.exists())
				{
					file.delete();
				}
				file.createNewFile();
				fout = new FileOutputStream(file);	
				fout.write(WordData.getBytes());
		    	fout.flush();
		    	fout.close();
				}
			 }
			 catch (FileNotFoundException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 catch (IOException ioe) 
			 {
		            ioe.printStackTrace();
		     }		    
           }
	       else
	       {
	    	      String FILENAME = "wordoftheday.txt";
			      FileOutputStream fos;
				  try 
				  {
					  if(WordData.length()>4)
					  {
						File file=DictionaryUrlReader.DictStart.getApplicationContext().getFileStreamPath("wordoftheday.txt");
						if(file.exists())
						{
						  file.delete(); 
						}
						fos = DictionaryUrlReader.DictStart.getApplicationContext().openFileOutput(FILENAME,Context.MODE_PRIVATE);
						fos.write(WordData.getBytes());
						fos.close();
					  }
				   } 
				   catch (FileNotFoundException e) {} 
				   catch (IOException e) {}	    	  
	        }  
	       }catch(Exception e){}
    	 }
     
}