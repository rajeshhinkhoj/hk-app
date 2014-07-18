package HinKhoj.Dictionary.UI;
import HinKhoj.Dictionary.Common.DictCommon;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class ConfirmBoxDisplay {

	
	
	public static void ttsconfirm(final Context context,String title,String message)
	{
		try
		{

			final AlertDialog.Builder confirm=new AlertDialog.Builder(context);
			confirm.setTitle(title);
			confirm.setMessage(message);
			confirm.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				
					try
					{
						 Intent installIntent = new Intent();
				            installIntent.setAction(
				                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				         context.startActivity(installIntent);
					}
					catch(Exception e)
					{
						DictCommon.HandleException(context, e, "Unable to download Speech engine");
					}
					
				}
			});

			confirm.setNegativeButton("No",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(context,"You will need speech engine for pronounciation",Toast.LENGTH_LONG).show();		
				}
			});
			confirm.show();  
		}
		catch(Exception e){}
	}


}
