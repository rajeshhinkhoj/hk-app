package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment.OnPagerContentChangedListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class SearchResultFragment extends Fragment {

   public static final String TAG = SearchResultFragment.class
            .getSimpleName();

   public static SearchResultFragment newInstance() {
      return new SearchResultFragment();
   }

   private OnPagerContentChangedListener mCallBack;
   private int tabPosition;
   private String searchWord="";
   private boolean isOnline=true;
   public interface OnFragmentAttachedListener {
      public void OnFragmentAttached();
  }
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      mCallBack=(OnPagerContentChangedListener)activity;
   }
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);
      Bundle arguments = getArguments();
      tabPosition=arguments.getInt("selectedTab");
      searchWord=arguments.getString(DictionarySearchFragment.SEARCH_WORD_KEY);
      isOnline=arguments.getBoolean(DictionarySearchFragment.IS_ONLINE);
   }
@Override
public void onResume() {
   // TODO Auto-generated method stub
   super.onResume();
}
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      return inflater.inflate(R.layout.pager, container, false);
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
               .findViewById(R.id.tabs);
      tabs.setIndicatorColor(Color.parseColor("#fc6900"));
      tabs.setTextColor(Color.parseColor("#ffffff"));
      tabs.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_text_size));
      if(getResources().getBoolean(R.bool.large_layout)){
         tabs.setShouldExpand(true);
      }else{
         tabs.setShouldExpand(false);     
      }
      ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
      
      MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
      pager.setAdapter(adapter);
      tabs.setViewPager(pager);
      tabs.setOnPageChangeListener(new OnPageChangeListener() {

         @Override
         public void onPageSelected(int arg0) {
            if (mCallBack!=null) {
               mCallBack.onPagerContentChanged(arg0);
            }
         }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2){

            }

            @Override
            public void onPageScrollStateChanged(int arg0){

            }
         });
      pager.setCurrentItem(tabPosition);

      }

      public class MyPagerAdapter extends FragmentPagerAdapter {

         public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
         }

         private final String[] TITLES =  getResources().getStringArray(R.array.search_result_array);

         @Override
         public CharSequence getPageTitle(int position) {
            return TITLES[position];
         }

         @Override
         public int getCount() {
            return TITLES.length;
         }

         @Override
         public Fragment getItem(int position) {
        	 String searchWordPassed="";
        	 if(tabPosition==position)
       	  	{
       		  searchWordPassed=searchWord;
       	  	}
            switch (position) {
            case 0:
                return MeaningFragment.newInstance(new Object[]{position,tabPosition,searchWordPassed,isOnline});
            case 1:
            	return DefinitionFragment.newInstance(position); 
            case 2:
           	    return SynonymFragment.newInstance(position);
            case 3:
           	    return AntonymFragment.newInstance(position);
            }
            return null;

         }

      }


   }

