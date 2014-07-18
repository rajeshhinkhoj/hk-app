package HinKhoj.Dictionary.Listeners;

import android.view.View;
import android.view.View.OnClickListener;

public class HangmanCloseButton implements OnClickListener
{

    @Override
    public void onClick(View v)
    {
	// TODO Auto-generated method stub
	android.os.Process.killProcess(android.os.Process.myPid());
    }

}
