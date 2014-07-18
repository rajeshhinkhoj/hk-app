package HinKhoj.Dictionary.ui;

import HinKhoj.Dictionary.R;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTextView  extends TextView{
   public MyTextView(Context context) {
      super(context);
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
      lp.leftMargin=5;
      this.setLayoutParams(lp);
      // Center the text vertically
      this.setGravity(Gravity.CENTER_VERTICAL);
      // Set the text starting position
      this.setSingleLine(true);
      this.setText("-");
      this.setBackgroundColor(getResources().getColor(R.color.transparent));
      this.setTextSize(20.0f);
      this.setTextColor(getResources().getColor(R.color.black));
      
   }

 
   
}
