package HinKhoj.Dictionary.Listeners;

import HinKhoj.Dictionary.AsyncTasks.LaunchExternalDictSearchTask;
import HinKhoj.Dictionary.UI.AndroidUIHelper;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
public class FindMeaningEditorHelper implements OnEditorActionListener  {

	private DictionaryMainActivity dictMain=null;
	public  FindMeaningEditorHelper(DictionaryMainActivity dictMain)
	{
		this.dictMain=dictMain;
	}
	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
		boolean performSearch=AndroidUIHelper.IsEnterKeyPressed(actionId,event);
		if(performSearch)
		{
			performSearch();
			return true;
		}
		return false;
	}
	private void performSearch() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String word=dictMain.getEditText();
		if(word!=null && word!="")
		{
			dictMain.clearFocus();
			new LaunchExternalDictSearchTask(dictMain).execute(new String[]{word});
		}
	}


}
