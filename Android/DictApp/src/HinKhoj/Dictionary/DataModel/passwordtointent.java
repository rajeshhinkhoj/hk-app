package HinKhoj.Dictionary.DataModel;

import java.io.Serializable;

import android.app.Activity;

public class passwordtointent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String[][] data=null;
	public static Activity mainobj=null;
	public void setdata(String[][] data)
	{
		this.data=data;
	}
	public String[][] getdata()
	{
		return this.data;
	}
	public static void setmainactivityobject(Activity mainobj)
	{
	 passwordtointent.mainobj=mainobj;	
	}
	public static Activity getmainactivityobject()
	{
		return passwordtointent.mainobj;
	}
}
