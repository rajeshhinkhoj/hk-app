package HinKhoj.Dictionary.fragments;

import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.adapters.SavedWordAdapter;
import HinKhoj.Dictionary.adapters.SavedWordListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class SavedWordsFragment extends Fragment  {

   private static final String ARG_POSITION = "position";
   private OnWordSelectedListener mCallBack;
   private boolean isChecked=true;
   private ListView savedWordList;
   private static final String ARG_SELECT_POSITION = "select_position";
   private int selectedPosition;
   private List<String> savedWords;


   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setHasOptionsMenu(true);
   }

   public SavedWordsFragment(){

   }

   public interface OnWordSelectedListener{

      public void onWordSelected(int position,String word);
      public void onDeleteSelected();
      public void onDeleteSelectedFromHistory();
      public void onDeleteButtonPressed();//raj  starts   raj  ends
      public void onDeleteButtonPressedHistory();//raj  starts   raj  ends
   }

   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      try {
         mCallBack=(OnWordSelectedListener)activity;
      } catch (Exception e) {
      }
   }

   @Override
   public void onResume() {
      super.onResume();
   }
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.my_dictionary_saved_word, container, false);
       savedWordList = (ListView)view.findViewById(R.id.saved_words_list);
      List<String> savedWords=DictCommon.getSavedWords();
      SavedWordListAdapter adapter = new SavedWordListAdapter(getActivity(),savedWords,false);
      savedWordList.setAdapter(adapter);
      savedWordList.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            TextView    tv = (TextView) arg1.findViewById(R.id.saved_word);
            String word = tv.getText().toString();  
            mCallBack.onWordSelected(arg2, word);
         }
      });
      return view;
   }
   
   @Override 
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.saved_words, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.backup_saved_words_menu:
			backupWords();
			break;
		}
		super.onOptionsItemSelected(item);
		return true;
	}


   private void backupWords() {
	   try
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT,"Saved words");
	
			String text="Below is list of saved words. Please save it yourself.\n";
			 List<String> savedWords=DictCommon.getSavedWords();
		     for(String sw: savedWords)
		     {
		    	 text+=(sw+"\n\t");
		     }
		     
		     intent.putExtra(Intent.EXTRA_TEXT,text);

			 this.getActivity().startActivity(Intent.createChooser(intent, "Share"));

		}catch(Exception e)
		{
			Toast.makeText(this.getActivity(),"Error while saving words", Toast.LENGTH_LONG).show();
		}
		
	}

//raj  starts
   public void  selectTodeleteWords(){
	   savedWords=DictCommon.getSavedWords();
      SavedWordListAdapter    adapter1=new SavedWordListAdapter(getActivity(), savedWords, isChecked);
      savedWordList.setAdapter(adapter1);
      isChecked=!isChecked; 
   } //raj  end
  
   //raj  starts
   public void deleteSelectedWords() {
      try {
         for(int i = 0; i < SavedWordAdapter.ArrayId.size(); i++){
            savedWords.remove(SavedWordAdapter.ArrayId.get(i));
            DictCommon.DeleteSavedWord(SavedWordAdapter.ArrayId.get(i));
         }
         SavedWordListAdapter    adapter1=new SavedWordListAdapter(getActivity(), savedWords, isChecked);
         savedWordList.setAdapter(adapter1);
      } catch (Exception e) {
      }
   }
   //raj  end
   public static SavedWordsFragment newInstance(int position) {
      SavedWordsFragment f = new SavedWordsFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, position);
      f.setArguments(b);
      return f;
   }

}
