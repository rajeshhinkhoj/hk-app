package HinKhoj.Dictionary.activity;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends CommonBaseActivity {
	  @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.aboutus);
	      
	      getWindow().setSoftInputMode(
	               WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	      getSupportActionBar().setHomeButtonEnabled(true);
	      getSupportActionBar().setDisplayShowTitleEnabled(false);
	      getSupportActionBar().setDisplayShowCustomEnabled(true);
	      View customTitle = getLayoutInflater().inflate(R.layout.appheader_layout, null);
	      getSupportActionBar().setCustomView(customTitle);
	      getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(),UICommon.GetBackgroundImage(this)));
	        
	      TextView contact_tv=(TextView)findViewById(R.id.contact_div);
	      
	      String message="<a href='https://play.google.com/store/apps/details?id=HinKhoj.Dictionary'> Click to give Rating and Review</a><br/><br/> Email your feedback to us at <a href='mailto:info@hinkhoj.com'>info@hinkhoj.com</a>";
	      contact_tv.setText(Html.fromHtml(message));
	      contact_tv.setMovementMethod(LinkMovementMethod.getInstance());
	      
	      DictCommon.AddTrackEvent(this);
	      
	   }

	  @Override
	   public boolean onCreateOptionsMenu(Menu menu) {

	      return super.onCreateOptionsMenu(menu);
	   }

	   @Override
	   public boolean onOptionsItemSelected(
	           MenuItem item) {
	      finish();
	      overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	      return true;
	   }
}
