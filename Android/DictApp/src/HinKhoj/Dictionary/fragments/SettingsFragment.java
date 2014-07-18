package HinKhoj.Dictionary.fragments;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.SettingsManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;


public class SettingsFragment extends Fragment {

	public static final String KEY_PREF_HINDi_SUPPORT = "pref_hindi_support";
	public static final String KEY_PREF_SEARCH_HISTORY = "pref_search_history";
	private static final String ARG_SELECT_POSITION = "select_position";

	private int selectedPosition;
    private static final String ARG_POSITION = "position";
    private int position;
    private View view;
	private boolean isPageLoaded=false;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      position = getArguments().getInt(ARG_POSITION);
      selectedPosition=getArguments().getInt(ARG_SELECT_POSITION);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.my_dictionary_settings, container, false);
       isPageLoaded=false;
      if(selectedPosition==position)
      {
    	  LoadContent();
      }   
      
      return view;
   }
   
   @Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && view !=null ) {
               LoadContent();

		}
	}

   private void LoadContent() {
	
	   if(!isPageLoaded && view!=null)
	   {
		   Log.v("hinkhoj","Loading setting page");
		   isPageLoaded=true;
	   CheckBox hindi_support=(CheckBox)view.findViewById(R.id.hindi_support_cb);
	      if(SettingsManager.IsHindiSupported(view.getContext()))
	      {
	    	  hindi_support.setChecked(true);
	      }
	      else
	      {
	    	  hindi_support.setChecked(false);
	      }
	      
	      hindi_support.setOnClickListener(new OnClickListener() {
	    	  
	    	  @Override
	    	  public void onClick(View v) {
	                    //is chkIos checked?
	    		if (((CheckBox) v).isChecked()) {
	    			SettingsManager.setHindiSupported(v.getContext(), true);
	    		}
	    		else
	    		{
	    			SettingsManager.setHindiSupported(v.getContext(), false);
	    		}
	     
	    	  }
	    	});
	      
	      
	      CheckBox search_history_cb=(CheckBox) view.findViewById(R.id.save_search_cb);
	      if(SettingsManager.IsSearchHistoryEnabled(view.getContext()))
	      {
	    	  search_history_cb.setChecked(true);
	      }
	      else
	      {
	    	  search_history_cb.setChecked(false);
	      }
	      
	      search_history_cb.setOnClickListener(new OnClickListener() {
	    	  
	    	  @Override
	    	  public void onClick(View v) {
	                    //is chkIos checked?
	    		if (((CheckBox) v).isChecked()) {
	    			SettingsManager.setSearchHistoryEnabled(v.getContext(), true);
	    		}
	    		else
	    		{
	    			SettingsManager.setSearchHistoryEnabled(v.getContext(), false);
	    		}
	     
	    	  }
	    	});
	   }
	
}


   public static SettingsFragment newInstance(Object[] args) {
      SettingsFragment f = new SettingsFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, (int)args[0]);
      b.putInt(ARG_SELECT_POSITION, (int)args[1]); 
      f.setArguments(b);
      return f;
   }

}
