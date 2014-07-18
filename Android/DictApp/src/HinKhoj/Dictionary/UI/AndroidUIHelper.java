package HinKhoj.Dictionary.UI;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

public class AndroidUIHelper {

	public static boolean IsEnterKeyPressed(int actionId, KeyEvent event) {
		
		boolean enterPressed=false;
		int result = actionId & EditorInfo.IME_MASK_ACTION;
        switch(result) {
        case EditorInfo.IME_ACTION_DONE:
        	enterPressed=true;
            break;
        case EditorInfo.IME_ACTION_NEXT:
        	enterPressed=true;
            break;
        }
		if (event != null && (event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
			enterPressed=true;
          
        }
		else if (event != null && (event.getKeyCode()== KeyEvent.FLAG_EDITOR_ACTION)) {
			enterPressed=true;
            
        }
		return enterPressed;
	}
	

}
