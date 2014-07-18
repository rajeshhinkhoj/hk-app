package HinKhoj.Dictionary.fragments;

import java.util.List;




import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Database.OfflineDatabaseSetupManager;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
import HinKhoj.Dictionary.activity.ConfigureActivity;
import HinKhoj.Dictionary.adapters.SimpleListAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ConfigureFragment extends Fragment {

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
      view = inflater.inflate(R.layout.configure_fragment, container, false);
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
		   isPageLoaded=true;
		   
	     TextView statusTV=(TextView)view.findViewById(R.id.config_status_tv);
	      if(OfflineDatabaseSetupManager.IsDictionaryUnCompressed())
	      {
	    	  statusTV.setText("Configured");
	    	  statusTV.setTextColor(getResources().getColor(R.color.darkgreen));
	      }
	      else
	      {
	    	  statusTV.setText("Need configuration");
	    	  statusTV.setTextColor(Color.RED);
	      }
	      
	      List<String> testList=OfflineDatabaseSetupManager.GetConfigurationTestResult(view.getContext());
	      
	      
	      ExpandableHeightListView test_lv=(  ExpandableHeightListView )view.findViewById(R.id.config_test_lv);
	     if(test_lv!=null)
	     {
	      test_lv.setAdapter(new SimpleListAdapter(view.getContext(), R.layout.simple_list_item, testList));
	      test_lv.setExpanded(true);
	     }
	      
	      Button cleanBtn=(Button)view.findViewById(R.id.reconfigure_btn);
	      cleanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DictCommon.tryCloseOfflineDb();
				OfflineDatabaseSetupManager.DeleteDictDatabase();
				Intent i= new Intent(v.getContext(),ConfigureActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
	   }
}

public static ConfigureFragment newInstance(Object[] args) {
      ConfigureFragment f = new ConfigureFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, (int)args[0]);
      b.putInt(ARG_SELECT_POSITION, (int)args[1]);      
      f.setArguments(b);
      return f;
   }

}
