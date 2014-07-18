package HinKhoj.Dictionary.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class NetworkCommon {
	public static String ReadURLContent(String urlString) throws IOException
	{
		URL url= new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		urlConnection.setConnectTimeout(60000);
		urlConnection.setReadTimeout(60000);
		urlConnection.connect();
		String content="";
		try {

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

			BufferedReader in = new BufferedReader(
					new InputStreamReader(inputStream));
			//  System.out.println("Encoding is "+urlConnection.getContentEncoding());
			String inputLine="";
			if(in!=null)
			{
				while ((inputLine = in.readLine()) != null) {
					content+=inputLine;
				}
			}
		} finally {
			urlConnection.disconnect();
		}
		return content;
	}

}
