package HinKhoj.Dictionary.AsyncTasks;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

public class AutocompleteTaskAsync extends AsyncTask<String,Integer,String[]> {
	private HindiEditText het=null;
	public AutocompleteTaskAsync(HindiEditText het)
	{
		this.het=het;
		
	}
    @Override
	protected String[] doInBackground(String... texts) {
    	String[] acList= new String[]{};
    	try {
			String text=texts[0];
			if(!het.IsHindiTyping)
			{
				if(text.length()>=2)
				{
					acList=DictCommon.GetEngAutoCompleteList(text.toString());
				}
				else
				{
					DictCommon.cleanAcList();
				}
			}
			else
			{
				if(text.length()>2)
				{
					acList=DictCommon.GetHinAutoCompleteList(text.toString());
				}
				else
				{
					DictCommon.cleanAcList();
				}
			}	
    		
		} 
    	catch (Exception e) {
			// TODO Auto-generated catch block
			//Log.v("hinkhoj","error while getting autocomplete async task"+e.toString());
		}
		return acList;
    }

    @Override
	protected void onProgressUpdate(Integer... progress) {
        //
    }

    @Override
	protected void onPostExecute(String[] acList) {
    	if(acList!=null && acList.length >0)
        {
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.het.getContext(),R.layout.dropdown_item,acList);
        	this.het.setThreshold(2);
        	//this.het.setDropDownHeight(6);
        	this.het.setAdapter(adapter);
        }
    }
	
}

