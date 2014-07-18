package HinKhoj.Dictionary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class CommonBaseFragment extends Fragment{


   private View view;


   public View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layoutId) {
      view = inflater.inflate(layoutId, container, false);
      return view;
   }

   public <T> void processResponse(T response, int reqId) {

   }


   public void changeFragment(int container, Fragment fragment) {
      getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      try {
         getActivity().getSupportFragmentManager().beginTransaction().replace(container, fragment)
         .addToBackStack(null).commit();
    	  
       } catch (Throwable e) {
      }
   }


}
