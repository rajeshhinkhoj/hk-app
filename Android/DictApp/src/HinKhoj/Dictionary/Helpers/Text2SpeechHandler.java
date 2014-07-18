package HinKhoj.Dictionary.Helpers;

import java.util.Locale;

import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.UI.ConfirmBoxDisplay;
import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class Text2SpeechHandler implements
TextToSpeech.OnInitListener {
	Context context;
	private TextToSpeech tts;
	public boolean enabled=false;
	public boolean needTTSData=false;
	public Text2SpeechHandler(Context context)
	{
		this.context=context;
		tts = new TextToSpeech(context, this);
	}

	public void onDestroy() {
		// Don't forget to shutdown tts!
		try
		{
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				needTTSData=true;
				Log.e("TTS", "This Language is not supported");
			} 
			else
			{
				enabled=true;
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}


	public void speakOut(String text) {
		// TODO Auto-generated method stub
		if(enabled)
		{
			AudioManager am=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),  AudioManager.FLAG_PLAY_SOUND);
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			Toast.makeText(context, "word "+text+" successfully spoken!!!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(needTTSData)
			{
				ConfirmBoxDisplay.ttsconfirm(context, "speech engine install", "You need to install speech engine for pronounciation voice. Press Yes to install?");
			}
		}
	}
}
