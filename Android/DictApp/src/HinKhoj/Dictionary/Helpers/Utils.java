package HinKhoj.Dictionary.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.HangmanGameInfo;

public class Utils
{
    public static ArrayList<Character> GetAllLetters()
    {
	ArrayList<Character> letterList= new ArrayList<Character>();
	letterList.add('A');
	letterList.add('B');
	letterList.add('C');
	letterList.add('D');
	letterList.add('E');
	letterList.add('F');
	letterList.add('G');
	letterList.add('H');
	letterList.add('I');
	letterList.add('J');
	letterList.add('K');
	letterList.add('L');
	letterList.add('M');
	letterList.add('N');
	letterList.add('O');
	letterList.add('P');
	letterList.add('Q');
	letterList.add('R');
	letterList.add('S');
	letterList.add('T');
	letterList.add('U');
	letterList.add('V');
	letterList.add('W');
	letterList.add('X');
	letterList.add('Y');
	letterList.add('Z');
	return letterList;
    }

    public static HangmanGameInfo GetWordSynonym() throws IOException
    {
	return GetWord("http://dict.hinkhoj.com/learn/games/guessenglishword/HangmanInfoWS.php?Action=RAND_SYN_HANGMAN");
	 
    }
    
    public static HangmanGameInfo GetHangmanGameInfo() throws IOException
    {
    	if(DictCommon.hmdb !=null)
    	{
    		return HangmanGameInfo.GetHangmanInfoFromDB(DictCommon.hmdb);
    	}
	    return GetWord("http://dict.hinkhoj.com/learn/games/guessenglishword/HangmanInfoWS.php?Action=RAND_HINMEAN_HANGMAN");
    }
    
    public static HangmanGameInfo GetWord(String urlString) throws IOException
    {
	URL url= new URL(urlString);
	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	try {
	    
	    BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
	    if(in!=null)
	    {
		inputLine=in.readLine();
		return HangmanGameInfo.GetHangmanInfoFromJSON(inputLine);
	    }
	} finally {
	    urlConnection.disconnect();
	}
	return null;
    }
}
