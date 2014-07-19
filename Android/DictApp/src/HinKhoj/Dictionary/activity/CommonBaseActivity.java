package HinKhoj.Dictionary.activity;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.fragments.AppSetupFragment;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment;
import HinKhoj.Dictionary.fragments.DictionaryToolsFragment;
import HinKhoj.Dictionary.fragments.HindiEnglishDictionaryFragment;
import HinKhoj.Dictionary.fragments.MyDictionaryFragment;
import HinKhoj.Dictionary.fragments.PremiumAccountFragment;
import HinKhoj.Dictionary.fragments.WordDetailsMobileFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class CommonBaseActivity extends ActionBarActivity{

   public int myDictionaryTabPosition;
   public static final int HINDI_ENGLISH_DICTIONARY=0;
   public static final int MEANING_TAB=100;
   public static final int DICTIONARY_TOOLS=1;
   public static final int MY_DICTIONARY=2;
   public static final int SETUP=4;
   public static final int SHARE=5;
   public static final int FEEDBACK=6;
   public static final int ABOUT_US=7;
   public static final int WEB_APP=8;
   public static final int EXIT_APP=9;
   public static final int PREMIUM_ACCOUNT=3;

   //   tabs position inside main fragment

   //   hindi english dictionary
   public static final int ONLINE_DICTIONARY=0;
   public static final int OFFLINE_DICTIONARY=1;

   // my dictionary
   public static final int SAVED_WORDS=0;
   public static final int SEARCH_HISTORY=1;
   public static final int CONFIGURE=0;
   public static final int SETTINGS=1;

   //dictionary tools
   public static final int WORD_OF_DAY=0;
   public static final int PRONUNCIATION=1;
   public static final int SPELL_CHECKING=2;
   public static final int WORD_GUESS_GAME=3;
   
   //premium account
   public static final int ADS_SETTINGS=0;
   
   //intent constants
   public static final String FRAGMENT_INDEX="Fragment_index";
   public static final String TAB_INDEX="Tab_index";

   protected void init(int routeNestedLayout, Bundle savedInstanceState, int rootContainerID) {
      super.onCreate(savedInstanceState);
      setContentView(routeNestedLayout);
     
   }

   public  void changeFragment(int selectedFragment,int selectedTab){
      Bundle bundle=new Bundle();
      bundle.putInt("selectedTab", selectedTab);
      switch (selectedFragment) {
         case 0:
        	removeAllFragments(); 
            HindiEnglishDictionaryFragment pageSlidingTabStripFragment = HindiEnglishDictionaryFragment.newInstance();
            pageSlidingTabStripFragment.setArguments(bundle);
            replaceFragment(pageSlidingTabStripFragment,pageSlidingTabStripFragment.getClass().getSimpleName());            
        	  
            break;
         case 1:
            DictionaryToolsFragment dictionaryToolsFragment = DictionaryToolsFragment.newInstance();
            dictionaryToolsFragment.setArguments(bundle);
            replaceFragment(dictionaryToolsFragment,dictionaryToolsFragment.getClass().getSimpleName());
            break;
         case 2:
            MyDictionaryFragment myDictionaryFragment = MyDictionaryFragment.newInstance();
            myDictionaryFragment.setArguments(bundle);
            replaceFragment(myDictionaryFragment,myDictionaryFragment.getClass().getSimpleName());
            break;
            
         case 3:
        	 bundle.putBoolean(AppAccountManager.ADS_ENABLE, AppAccountManager.isAdsEnabled(this));
             PremiumAccountFragment premiumFragment = PremiumAccountFragment.newInstance();
             premiumFragment.setArguments(bundle);
             
             replaceFragment(premiumFragment,premiumFragment.getClass().getSimpleName());
             break;
             
         case 4:
             AppSetupFragment setupFragment = AppSetupFragment.newInstance();
             setupFragment.setArguments(bundle);
             replaceFragment(setupFragment,setupFragment.getClass().getSimpleName());
             break;
             
        
             
         default:
        	 removeAllFragments();
            HindiEnglishDictionaryFragment pageSlidingTabStripFragment3 = HindiEnglishDictionaryFragment.newInstance();
            pageSlidingTabStripFragment3.setArguments(bundle);
            replaceFragment(pageSlidingTabStripFragment3,pageSlidingTabStripFragment3.getClass().getSimpleName());
            break;

      }

   }

   
   private void removeAllFragments() {
	     try
	     {
	    	 if(getSupportFragmentManager().getBackStackEntryCount()>0)
	    	 {
	    		 getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    	 }
	     }
	     catch(Exception e)
	     {
	    	 DictCommon.LogException(e);
	     }
	
}

@Override
   public void onBackPressed() {
      super.onBackPressed();
   }

   public void hideKeyboard(){
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
   }
   public void setupUI(View view) {
      if(!(view instanceof EditText)) {
         view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               hideKeyboard();
               return false;
            }
         });
      }
      if (view instanceof ViewGroup) {
         for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            View innerView = ((ViewGroup) view).getChildAt(i);
            setupUI(innerView);
         }
      }
   }
   /**
    * @return the myDictionaryTabPosition
    */
   public int getTabPosition() {
      return myDictionaryTabPosition;
   }

   /**
    * @param myDictionaryTabPosition the myDictionaryTabPosition to set
    */
   public void setTabPosition(int myDictionaryTabPosition) {
      this.myDictionaryTabPosition = myDictionaryTabPosition;
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
      super.onSaveInstanceState(outState);

   }

   public void addFragment(Fragment fragment ,String fragmentName){
      try {
         getSupportFragmentManager()
         .beginTransaction()
         .add(R.id.content,
                  fragment,
                  fragmentName).addToBackStack(fragmentName).commit();
      } catch (Exception e) {

      }

   }
   
   public void replaceFragment(Fragment fragment ,String fragmentName){
      try {
         getSupportFragmentManager()
         .beginTransaction()
         .replace(R.id.content,
                  fragment,
                  fragmentName).addToBackStack(fragmentName).commit();
         hideKeyboard();
      } catch (Exception e) {

      }
   }
   
   
   public void showWordDetailsMobile(int meaning_id,String word)
   {
	   Bundle bundle=new Bundle();
	   bundle.putInt("selectedTab", 0);
	   bundle.putInt(DictionarySearchFragment.MEANING_ID, meaning_id);
	   bundle.putString(DictionarySearchFragment.MAIN_WORD, word);
	   WordDetailsMobileFragment wdFrag= WordDetailsMobileFragment.newInstance(new Object[]{meaning_id,word}) ; 
       replaceFragment(wdFrag,wdFrag.getClass().getSimpleName());
   }
}