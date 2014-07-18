package HinKhoj.Dictionary.DataModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;

public class HangmanGameInfo
{
    public String Word;
    public String HinType;
    public String Hint;
    
    public static HangmanGameInfo GetHangmanInfoFromJSON(String jsonString)
    {
    	HangmanGameInfo hgi=new Gson().fromJson(jsonString, HangmanGameInfo.class);
    	return hgi;
    }
    
    public static HangmanGameInfo GetHangmanInfoFromDB(SQLiteDatabase db)
    {
    	HangmanGameInfo hgi= new HangmanGameInfo();
    	Cursor c=db.rawQuery("SELECT word,hint FROM hangman_advance_game where 1 order by random() limit 1",null);
		if(c.getCount()>0)
		{
			c.moveToFirst();
			
			hgi.Word=c.getString(0);
			hgi.Hint=c.getString(1);
		}
		c.close();
		
		return hgi;
    }

}
