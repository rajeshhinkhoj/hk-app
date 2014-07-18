package HinKhoj.Dictionary.fragments;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.fragments.SavedWordsFragment.OnWordSelectedListener;
import HinKhoj.Dictionary.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 * 
 * @author Dani Lao (@dani_lao)
 * 
 */
public class SavedWordsRootFragment extends Fragment {

   private static final String TAG = "RootFragment";
   private static final String ARG_POSITION = "position";
   private View view;
   private OnWordSelectedListener mCallBack;
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
     view = inflater.inflate(R.layout.saved_words_main_fragment, container, false);
   
      return view;
   }
@Override
public void onResume() {
   super.onResume();
   Utils.disableButton(view.findViewById(R.id.delete));
   
   select_to_delete= (CheckBox)view.findViewById(R.id.select_to_delete);
   
   select_to_delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         if (isChecked) {
            mCallBack.onDeleteSelected();
            Utils.enableButton(view.findViewById(R.id.delete));
         }else{
            mCallBack.onDeleteSelected();
            Utils.disableButton(view.findViewById(R.id.delete));
         }
         
      }
   });
   
   
   //raj  starts
   view.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View arg0) {
         mCallBack.onDeleteButtonPressed();
         select_to_delete.setChecked(false);
      }
   });  //raj  ends
   
      SavedWordsFragment savedWordsFragment = new SavedWordsFragment();
      getParentFragment().getActivity().getSupportFragmentManager().beginTransaction()
              .replace(R.id.saved_words_main_fragments_small, savedWordsFragment).commit();
  
}

public static SavedWordsRootFragment newInstance(int position) {
   SavedWordsRootFragment f = new SavedWordsRootFragment();
   Bundle b = new Bundle();
   b.putInt(ARG_POSITION, position);
   f.setArguments(b);
   return f;
}

}
