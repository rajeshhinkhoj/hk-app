package HinKhoj.Dictionary.Helpers;

import HinKhoj.Dictionary.AsyncTasks.AutocompleteTaskAsync;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import android.text.Editable;
import android.text.TextWatcher;

public class AdvanceHindiTextWatcher implements TextWatcher {

	HindiEditText het=null;
	private String lastSetText="";
	private String lastText="";

	public AdvanceHindiTextWatcher(HindiEditText te)
	{
		het=te;
	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) 
	{
	
		if(!het.IsHindiTyping)
		{
			this.lastSetText="";
			het.Hlt.Reset("");
			if(lastText.length()<text.length() && text.length()>2 && lastText.length()>1)
			{
				this.updateAutoCompletion(text);
			}
			this.lastText=text.toString();
			return;
		}

		try
		{
			if(text==null || text.length()==0)
			{
				this.lastSetText="";
				//het.Hlt.Reset("");
				
				this.lastText="";
				return ;
			}
			
			if(text.length()>0)
			{
				het.setSelection(text.length());
			}
			else
			{
				het.setSelection(1);
			}

			if(lastSetText.compareTo(text.toString())==0)
			{
				this.lastText=text.toString();
				return;
			}

			char lastChar=text.charAt(text.length()-1);
			// TODO Auto-generated method stub
			if(HindiCommon.IsHindi(lastChar+"") && lastChar!=' ')
			{
				this.lastText=text.toString();
				return;
			}
			if(this.lastSetText=="" && !het.isLastKeyProcessed)
			{
				het.setText("");
				het.Hlt.Reset("");
			}
			String tt="";
			if(!het.isLastKeyProcessed)
			{
				String currentText=het.getText().toString();
				if(currentText.length()>1)
				{
					String preText=currentText.substring(0,currentText.length()-1);
					preText=HindiCommon.ShiftRightSmallE(preText);
					het.Hlt.Reset(preText);
				}
			}
			tt=het.Hlt.ProcessKeyEvent(lastChar);
			//Log.v("hinkhoj","display related words "+lastChar);
			het.KBHelper.displayRelatedWords(lastChar);
			het.isLastKeyProcessed=true;

			if(lastChar !=' ')
			{
				this.lastSetText=HindiCommon.ShiftLeftSmallE(tt);
				het.setText(this.lastSetText);
			}
			if(lastText.length()<text.length() && text.length()>3 && lastText.length()>1)
			{
				this.updateAutoCompletion(this.lastSetText);
			}
			//ic.commitText(text, newCursorPosition);
		}
		catch(Exception e)
		{
			//
		}

	}


	private void updateAutoCompletion(CharSequence text) {
		// TODO Auto-generated method stub
		if(text!=null)
		{
			new AutocompleteTaskAsync(this.het).execute(new String[]{text.toString()});
		}
	}

}
