package HinKhoj.Dictionary.Common;

import java.io.IOException;
import java.net.URLEncoder;
import org.json.JSONException;

import HinKhoj.Dictionary.DataModel.SpellCheckData;
import HinKhoj.Dictionary.DataModel.SpellCheckResultData;
import com.google.gson.Gson;

public class SpellCheckCommon {

	public static SpellCheckResultData GetSpellCheckResult(String word) throws IOException, JSONException
	{   	
		String SpellCheckJSon="";

		@SuppressWarnings("deprecation")
		String encodeWord=URLEncoder.encode(word);
		String seekUrl="http://dict.hinkhoj.com/WebServices/GetSpellCheckResult.php?word="+encodeWord;
		SpellCheckJSon=HinKhoj.Hindi.Android.Common.AndroidHelper.ReadURLContent(seekUrl);  
		SpellCheckData scd=SpellCheckCommon.GetSpellCheckResultDataFromJson(SpellCheckJSon);
		return new SpellCheckResultData(scd);
	}	    	




	private static SpellCheckData GetSpellCheckResultDataFromJson(String SpellCheckJSon) throws JSONException 
	{		
		SpellCheckData scd=null;
		scd=new Gson().fromJson(SpellCheckJSon,SpellCheckData.class);
		return scd;

	}    


}
