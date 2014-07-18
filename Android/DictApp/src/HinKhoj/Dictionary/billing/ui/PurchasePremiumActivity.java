package HinKhoj.Dictionary.billing.ui;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.activity.CommonBaseActivity;
import HinKhoj.Dictionary.activity.DictionaryMainActivity;
import HinKhoj.Dictionary.billing.domain.items.PremiumAccount;
import HinKhoj.Dictionary.billing.ui.base.PurchaseActivity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Purchase;

/**
 * This activity will purchase a Passport from Google Play.
 * 
 * If you wanted to change to purchase something else all you have to change is the SKU (item id) that is used
 * you could even pass this in as an Intent EXTRA to avoid duplication for multiple items to purchase
 * 
 * N.B that we extend PurchaseActivity if you don't understand something look up to this class
 * 
 * @author Blundell
 * 
 */
public class PurchasePremiumActivity extends PurchaseActivity {

	public static int PURCHASE_PREMIUM_REQUEST=2345;

	TextView payment_success_status=null;
	TextView payment_fail_status=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the result as cancelled in case anything fails before we purchase the item
		setResult(RESULT_CANCELED);
		setContentView(R.layout.payment_status);
		getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(),UICommon.GetBackgroundImage(this)));	     
		payment_success_status=(TextView)findViewById(R.id.correct_msg_tv);
		payment_fail_status=(TextView)findViewById(R.id.wrong_msg_tv);
		super.startPurchase();
		// Then wait for the callback if we have successfully setup in app billing or not (because we extend PurchaseActivity)
	}

	@Override
	protected void dealWithIabSetupFailure() {
		UpdatePaymentFailStatus("Sorry buying a premium is not available at this current time");
		waitToFinish();
	}

	private void waitToFinish() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	private void UpdatePaymentFailStatus(String msg) {
		TextView progressTV=(TextView)findViewById(R.id.payment_progress_tv);
		progressTV.setVisibility(View.GONE);
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
		LinearLayout failLL=(LinearLayout)findViewById(R.id.wrong_msg_ll);
		failLL.setVisibility(View.VISIBLE);
		payment_fail_status.setText(Html.fromHtml(msg));
	}

	@Override
	protected void dealWithIabSetupSuccess() {
		purchaseItem(PremiumAccount.getPremiumAccountSKU());
	}

	@Override
	protected void dealWithPurchaseSuccess(IabResult result, Purchase info) {
		try
		{
			super.dealWithPurchaseSuccess(result, info);
			setResult(RESULT_OK);
			String successMessage="Upgrade to premium account is successfull. <br/>";
			if(info!=null)
			{
				if(result!=null && result.isSuccess())
				{
					AppAccountManager.setAdsStatus(this, false);
				}
				successMessage+="Please record below data for future communication: ";
				successMessage+=("<br/><br/>Order Id: <b>"+info.getOrderId());	
			}
			Log.v("hinkhoj",successMessage);
			displaySuccessMessage(successMessage);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
		Intent i=new Intent(this,DictionaryMainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra(CommonBaseActivity.FRAGMENT_INDEX, CommonBaseActivity.PREMIUM_ACCOUNT);
		i.putExtra(CommonBaseActivity.TAB_INDEX, CommonBaseActivity.ADS_SETTINGS);
		startActivity(i);
		finish();
	}

	private void displaySuccessMessage(String msg) {
		// TODO Auto-generated method stub
		TextView progressTV=(TextView)findViewById(R.id.payment_progress_tv);
		progressTV.setVisibility(View.GONE);
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
		LinearLayout correctLL=(LinearLayout)findViewById(R.id.correct_msg_ll);
		correctLL.setVisibility(View.VISIBLE);
		payment_success_status.setText(Html.fromHtml(msg));
	}

	@Override
	protected void dealWithPurchaseFailed(IabResult result) {
		super.dealWithPurchaseFailed(result);
		setResult(RESULT_CANCELED);

		String successMessage="Upgrade to premium account FAILED. <br/>";
		if(result!=null)
		{
			successMessage+="Please record below data for future communication: ";
			successMessage+=("<br/><br/>Failure reason: <b>"+result.getMessage());	
		}
		UpdatePaymentFailStatus(successMessage);
		
		waitToFinish();
	}

}
