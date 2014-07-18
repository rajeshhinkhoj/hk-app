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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class HindiEnglishDictionaryFragment extends Fragment {

   public static final String TAG = HindiEnglishDictionaryFragment.class
            .getSimpleName();

   public static HindiEnglishDictionaryFragment newInstance() {
      return new HindiEnglishDictionaryFragment();
   }

   private int tabPosition;
   private OnPagerContentChangedListener mCallBack;
   private String searchWord="";
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      try {
         mCallBack=(OnPagerContentChangedListener)activity;
      } catch (Exception e) {
      }
    
   }
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);
      Bundle arguments = getArguments();
      tabPosition=arguments.getInt("selectedTab");
      searchWord=arguments.getString(DictionarySearchFragment.SEARCH_WORD_KEY);
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
      if(getResources().getBoolean(R.bool.large_layout)){
         tabs.setShouldExpand(true);
      }else{
         tabs.setShouldExpand(false);     
      }
      tabs.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_text_size));
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
         public void onPageScrolled(int arg0, float arg1, int arg2) {
         
         }
         
         @Override
         public void onPageScrollStateChanged(int arg0) {
            
         }
      });
      pager.setCurrentItem(tabPosition);

   }

   public class MyPagerAdapter extends FragmentPagerAdapter {

      public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
         super(fm);
      }
   
      private final String[] TITLES =  getResources().getStringArray(R.array.hindi_english_dictionary_array);

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
    	 // Log.v("hinkhoj","Get item is "+ position+" selected tab is :"+tabPosition);
    	  String searchWordPassed="";
    	  if(tabPosition==position)
    	  {
    		  searchWordPassed=searchWord;
    		  //Log.v("hinkhoj","search word passed is "+ searchWordPassed);
    	  }
         switch (position) {
         case 0:
            if (getResources().getBoolean(R.bool.large_layout)) {
            	return MainFragment.newInstance(new Object[]{position,tabPosition,searchWordPassed,true});
            } else {
               return MainFragment.newInstance(new Object[]{position,tabPosition,searchWordPassed,false});
            }   
         
      }
         return null;
      }
   }


}