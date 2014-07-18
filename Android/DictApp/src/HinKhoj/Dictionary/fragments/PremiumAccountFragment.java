package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.UICommon;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PremiumAccountFragment extends Fragment {

   private int tabPosition;
   private boolean adsEnabled;
   private OnPagerContentChangedListener mCallBack;
   public static final String TAG = HindiEnglishDictionaryFragment.class
            .getSimpleName();

   public static PremiumAccountFragment newInstance() {
      return new PremiumAccountFragment();
   }


   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      try {
         mCallBack=(OnPagerContentChangedListener)activity;
      } catch (Exception e) {
      }
   }

   public interface OnPagerContentChangedListener{
      public void onPagerContentChanged(int tabPosition);
      public void onSearchClicked();
      public void  onMenuItemClicked(int selectionPosition,int tabPosition);
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);
      tabPosition=getArguments().getInt("selectedTab");
      adsEnabled= getArguments().getBoolean(AppAccountManager.ADS_ENABLE);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      return inflater.inflate(R.layout.pager, container, false);
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      PagerTabStrip tabs = ( PagerTabStrip) view
               .findViewById(R.id.tabs);
      UICommon.initializePagerTab(view.getContext(), tabs);
      final ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
      MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
      pager.setOffscreenPageLimit(4);
      pager.setAdapter(adapter);
      //tabs.setViewPager(pager);

      pager.setOnPageChangeListener(new OnPageChangeListener() {

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

      public MyPagerAdapter(FragmentManager fm) {
         super(fm);
      }

      private final String[] TITLES =  getResources().getStringArray(R.array.premium_array);

      @Override
      public CharSequence getPageTitle(int position) {
         return TITLES[position].toUpperCase();
      }

      @Override
      public int getCount() {
         return TITLES.length;
      }


      @Override
      public Fragment getItem(int position) {
    	  Log.v("hinkhoj","showing ads setting "+adsEnabled);
         switch (position) {
         case 0:
               return AdSettingFragment.newInstance(new Object[]{position,adsEnabled});      
         }
         return null;
      }

   }
}
