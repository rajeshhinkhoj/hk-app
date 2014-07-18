package HinKhoj.Dictionary.ui;


import HinKhoj.Dictionary.R;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class AppHeader extends LinearLayout {

   private View    headerView;
   private View    navigate_button;
   private View    back_button;

   public AppHeader(Context context, View rootView) {
      super(context);
      headerView = inflate(context, R.layout.appheader_layout, this);
      ((LinearLayout) rootView).addView(this, 0);
      initializeAllComponentsFromHeader();
   }

   private void initializeAllComponentsFromHeader() {
  
   }

   public View getNavigateBtnView() {
      navigate_button.setVisibility(View.VISIBLE);
      return navigate_button;

   }

   public View getBackBtnView() {
      back_button.setVisibility(View.VISIBLE);
      return back_button;

   }
}

