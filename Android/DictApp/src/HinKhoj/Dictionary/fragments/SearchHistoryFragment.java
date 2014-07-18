package HinKhoj.Dictionary.fragments;

import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.adapters.SavedWordAdapter;
import HinKhoj.Dictionary.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
public class SearchHistoryFragment extends Fragment {
   private static final String ARG_POSITION = "position";

   private int position;
   private static final String ARG_SELECT_POSITION = "select_position";

	private int selectedPosition;
   private onWordSelectedFromHistoryListener mCallBack;

   private ListView savedWordList;
   private List<String> searchHistory;

   private boolean isChecked=true;
   public interface onWordSelectedFromHistoryListener{

      public void WordSelectedFromHistory(int position,String word);
      public void onDeleteButtonPressedHistory();

   }
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      mCallBack=(onWordSelectedFromHistoryListener)activity;
   }
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //      position = getArguments().getInt(ARG_POSITION);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.my_dictionary_search_history, container, false);
       
      savedWordList = (ListView)view.findViewById(R.id.words_history_list);
    
      searchHistory=DictCommon.getSearchHistory();
      SavedWordAdapter adapter = new SavedWordAdapter(getActivity(), R.layout.saved_word_list_item, false,"",searchHistory);
      savedWordList.setAdapter(adapter);
      savedWordList.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            TextView    tv = (TextView) arg1.findViewById(R.id.saved_word);
            String word = tv.getText().toString();  
            mCallBack.WordSelectedFromHistory(arg2, word);
         }
      });
      Utils.updateListViewHeight(savedWordList);
      return view;
   }

   public void  selectTodeleteWordsFromHistory(){
	   List<String> searchHistory=DictCommon.getSearchHistory();
	   
      SavedWordAdapter adapter = new SavedWordAdapter(getActivity(), R.layout.saved_word_list_item, isChecked,"",searchHistory);
      savedWordList.setAdapter(adapter);
      isChecked=!isChecked; 
   }
   
   //raj  starts
   public void deleteSelectedWords() {
      try {
         for(int i = 0; i < SavedWordAdapter.ArrayId.size(); i++){
            searchHistory.remove(SavedWordAdapter.ArrayId.get(i));
            DictCommon.DeleteSearchedWord(SavedWordAdapter.ArrayId.get(i));
         }
         SavedWordAdapter    adapter1=new SavedWordAdapter(getActivity(), R.layout.saved_word_list_item,isChecked,"",searchHistory);
         savedWordList.setAdapter(adapter1);
      } catch (Exception e) {
      }
   }
   //raj  end


   public static SearchHistoryFragment newInstance(int position) {
      SearchHistoryFragment f = new SearchHistoryFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, position);
      f.setArguments(b);
      return f;
   }
  
}
