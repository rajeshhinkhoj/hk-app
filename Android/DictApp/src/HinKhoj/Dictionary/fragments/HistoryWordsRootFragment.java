package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.fragments.SavedWordsFragment.OnWordSelectedListener;
import HinKhoj.Dictionary.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
public class HistoryWordsRootFragment extends Fragment {

   private static final String TAG = "RootFragment";
   private static final String ARG_POSITION = "position";
   private OnWordSelectedListener mCallBack;
   private View view;
   CheckBox select_to_delete=null;
   
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      try {
         mCallBack=(OnWordSelectedListener)activity;
      } catch (Exception e) {
      }
   }
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.history_words_main_fragment, container, false);
      return view;
   }
   @Override
   public void onResume() {
      super.onResume();
      Utils.disableButton(view.findViewById(R.id.delete));
      
      select_to_delete = (CheckBox)view.findViewById(R.id.select_to_delete);
      
      select_to_delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
               mCallBack.onDeleteSelectedFromHistory();
               Utils.enableButton(view.findViewById(R.id.delete));
            }else{
               mCallBack.onDeleteSelectedFromHistory();
               Utils.disableButton(view.findViewById(R.id.delete));
            }
            
         }
      });
      
      //raj  starts
      view.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
         
         @Override
         public void onClick(View arg0) {        	
            mCallBack.onDeleteButtonPressedHistory(); 
            select_to_delete.setChecked(false);
            
         }
      });  //raj  ends
      
         SearchHistoryFragment searchHistoryFragment =SearchHistoryFragment.newInstance(1);
         getParentFragment().getActivity().getSupportFragmentManager().beginTransaction()
         .replace(R.id.history_words_main_fragments_small, searchHistoryFragment).commit();
    
   }
   
   
   public static HistoryWordsRootFragment newInstance(int position) {
      HistoryWordsRootFragment f = new HistoryWordsRootFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, position);
      f.setArguments(b);
      return f;
   }
  
}
