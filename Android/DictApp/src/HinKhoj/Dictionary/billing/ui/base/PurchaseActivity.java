package HinKhoj.Dictionary.billing.ui.base;

import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.billing.AppProperties;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.billing.domain.items.PremiumAccount;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.vending.billing.util.*;
import com.android.vending.billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;

/**
 * This class should be the parent of any Activity that wants to do in app purchases
 * it makes our life easier by wrapping up the talking to the IabHelper and just exposing what is needed.
 * 
 * When this activity starts it will bind to the Google Play IAB service and check for its availability
 * 
 * After that you can purchase items using purchaseItem(String sku) and listening for the result
 * by overriding dealWithPurchaseFailed(IabResult result) and dealWithPurchaseSuccess(IabResult result, Purchase info)
 * 
 * @author Blundell
 * 
 */
public abstract class PurchaseActivity extends Activity implements OnIabSetupFinishedListener, OnIabPurchaseFinishedListener {

    private IabHelper billingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            Log.d("hinkhoj","In-app Billing set up" + result);
            dealWithIabSetupSuccess();
        } else {
            Log.d("hinkhoj","Problem setting up In-app Billing: " + result);
            dealWithIabSetupFailure();
        }
    }

    protected abstract void dealWithIabSetupSuccess();

    protected abstract void dealWithIabSetupFailure();

    protected void purchaseItem(String sku) {
        billingHelper.launchPurchaseFlow(this, sku, 123, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        billingHelper.handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * Security Recommendation: When you receive the purchase response from Google Play, make sure to check the returned data
     * signature, the orderId, and the developerPayload string in the Purchase object to make sure that you are getting the
     * expected values. You should verify that the orderId is a unique value that you have not previously processed, and the
     * developerPayload string matches the token that you sent previously with the purchase request. As a further security
     * precaution, you should perform the verification on your own secure server.
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            dealWithPurchaseFailed(result);
        } else if (PremiumAccount.getPremiumAccountSKU().equals(info.getSku())) {
            dealWithPurchaseSuccess(result, info);
        }
        finish();
    }

    protected void dealWithPurchaseFailed(IabResult result) {
        Log.v("hinkhoj","Error while purchasing subscription: " + result);
    }

    protected void dealWithPurchaseSuccess(IabResult result, Purchase info) {
            AppAccountManager.setAdsStatus(this, false);
    		DictCommon.InitializeAds(this, R.id.ad);
    }

    @Override
    protected void onDestroy() {
        disposeBillingHelper();
        super.onDestroy();
    }

    private void disposeBillingHelper() {
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
    }

	public void startPurchase() {
		// TODO Auto-generated method stub

        billingHelper = new IabHelper(this, AppProperties.getBase64Key());
        billingHelper.startSetup(this);
	}
}
