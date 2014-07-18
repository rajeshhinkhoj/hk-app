package HinKhoj.Dictionary.Marketing;

import HinKhoj.Dictionary.Config.HKSettings;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class FeedbackConfirmBoxDisplay {

	public static void createconfirmbox(final Context context,String title,String message)
	{
		try
		{
			
			final AlertDialog.Builder confirm=new AlertDialog.Builder(context);
			
			confirm.setTitle(title);
			confirm.setMessage(message);
			confirm.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					try
					{
						Uri uri = Uri.parse(HKSettings.FeedbackUrl);
						Intent intent = new Intent (Intent.ACTION_VIEW, uri); 
						context.startActivity(intent);
					}
					catch(Exception e)
					{
						Toast.makeText(context, "Please send feedback to info@hinkhoj.com !!", Toast.LENGTH_LONG).show();
					}
				}
			});

			confirm.setNegativeButton("No",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(context,"Please give feedback to improve dictionary",Toast.LENGTH_LONG).show();		
				}
			});
			confirm.show();
			
		}
		catch(Exception e){}
	}

}
