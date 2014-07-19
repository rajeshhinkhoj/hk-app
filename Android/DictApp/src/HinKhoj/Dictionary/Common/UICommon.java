package HinKhoj.Dictionary.Common;

import HinKhoj.Dictionary.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerTabStrip;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class UICommon {
	private static Bitmap _backgroundImage=null;
	public static Bitmap GetBackgroundImage(Context context)
	{
		if(_backgroundImage==null)
		{
			_backgroundImage=BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
		}
		return _backgroundImage;
	}
	public static void HideInputKeyBoard(View view,Context context) {
		// TODO Auto-generated method stub
		InputMethodManager	imm=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	public static void showLongToast(
			Context context,
			String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		
	}
	
	public static void getOverflowMenu(Activity pActivity) {

		try {
			ViewConfiguration config = ViewConfiguration.get(pActivity);
			java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	public static void initializePagerTab(Context context,PagerTabStrip tabs) {
		 tabs.setTabIndicatorColor(Color.parseColor("#fc6900"));	
		 tabs.setDrawFullUnderline(true);
		 
	}

}
