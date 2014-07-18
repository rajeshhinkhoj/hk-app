package HinKhoj.Dictionary.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

   public Utils() {
      // TODO Auto-generated constructor stub
   }
   public static void updateListViewHeight(ListView myListView) {
      ListAdapter myListAdapter = myListView.getAdapter();
      if (myListAdapter == null) {            
               return;
      }
      
      //current height
      myListView.measure(0, 0);
      int current_height= myListView.getMeasuredHeight();
     //get listview height
     int totalHeight = 0;
     int adapterCount = myListAdapter.getCount();
     for (int size = 0; size < adapterCount ; size++) {
         View listItem = myListAdapter.getView(size, null, myListView);
         listItem.measure(0, 0);
         totalHeight += listItem.getMeasuredHeight();
     }
     //Change Height of ListView 
     ViewGroup.LayoutParams params = myListView.getLayoutParams();
     params.height = totalHeight + (myListView.getDividerHeight() * (adapterCount - 1));
     if(params.height > current_height)
     {
       myListView.setLayoutParams(params);
     }
 }
   
   
   public static void disableButton(View button) {
      AlphaAnimation alphaUp = new AlphaAnimation(1, 0.5f);
      alphaUp.setFillAfter(true);
      button.startAnimation(alphaUp);
      button.setEnabled(false);
   }

   public static void enableButton(View button) {
      AlphaAnimation alphaUp = new AlphaAnimation(0.5f, 1);
      alphaUp.setFillAfter(true);
      button.startAnimation(alphaUp);
      button.setEnabled(true);
   }

}
