package HinKhoj.Dictionary.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.billing.util.SkuDetails;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.AppAccountManager;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Constants.AppConstants;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
import HinKhoj.Dictionary.adapters.SimpleListAdapter;
import HinKhoj.Dictionary.billing.AppProperties;
import HinKhoj.Dictionary.billing.domain.items.PremiumAccount;
import HinKhoj.Dictionary.billing.ui.PurchasePremiumActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AdSettingFragment extends Fragment implements OnIabSetupFinishedListener {

	private static final String ARG_SELECT_POSITION = "select_position";
	private int selectedPosition;
	private View view=null;
	private boolean adsEnabled;
	private IabHelper billingHelper=null;
	TextView statusTV=null;
	Button purchase_btn=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		adsEnabled=getArguments().getBoolean(AppAccountManager.ADS_ENABLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ad_settings_fragment, container, false);     
		statusTV=(TextView)view.findViewById(R.id.ads_status_tv);
		refreshView();
		return view;
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(view!=null)
		{
			refreshView();
		}
	}


	private void refreshView() {
		// TODO Auto-generated method stub
		LinearLayout configBtnll=(LinearLayout)view.findViewById(R.id.config_btn_ll);
		configBtnll.setVisibility(View.GONE);

		LinearLayout premiumMsgll=(LinearLayout)view.findViewById(R.id.premium_msg_ll);
		premiumMsgll.setVisibility(View.GONE);
		purchase_btn=(Button)view.findViewById(R.id.purchase_btn);


		LinearLayout no_conn_layout=(LinearLayout)view.findViewById(R.id.no_connection_ll);
		if(!DictCommon.IsConnected(view.getContext()))
		{
			statusTV.setText(" NOT AVAILABLE");
			no_conn_layout.setVisibility(View.VISIBLE);
			purchase_btn.setVisibility(View.GONE);
		}
		else
		{
			no_conn_layout.setVisibility(View.GONE);
			if(billingHelper==null)
			{
				billingHelper = new IabHelper(this.getActivity(), AppProperties.getBase64Key());
				billingHelper.enableDebugLogging(true);
				billingHelper.startSetup(this);
			}
		}

	}


	private static final String ARG_POSITION = "position";

	private int position;

	public static AdSettingFragment newInstance(Object[] args) {
		AdSettingFragment f = new AdSettingFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putBoolean(AppAccountManager.ADS_ENABLE, (boolean)args[1]);
		f.setArguments(b);
		return f;
	}

	public void setupPremiumSettings(boolean adsEnabled) {

		Log.v("hinkhoj","showing premium settings");
		AppAccountManager.setAdsStatus(view.getContext(), adsEnabled);



		LinearLayout configBtnll=(LinearLayout)view.findViewById(R.id.config_btn_ll);

		LinearLayout premiumMsgll=(LinearLayout)view.findViewById(R.id.premium_msg_ll);

		if(adsEnabled)
		{
			statusTV.setText(" Ads Showing");
			statusTV.setTextColor(Color.RED);
			configBtnll.setVisibility(View.VISIBLE);
			premiumMsgll.setVisibility(View.VISIBLE);

			purchase_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i= new Intent(getActivity(),PurchasePremiumActivity.class);
					getActivity().startActivityForResult(i, PurchasePremiumActivity.PURCHASE_PREMIUM_REQUEST);
				}
			});

			purchase_btn.setEnabled(true);


		}
		else
		{
			statusTV.setText(" Ads Removed");
			statusTV.setTextColor(getResources().getColor(R.color.darkgreen));
		}
		
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d("hinkhoj", "Adsettings: Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (billingHelper == null)
			{
				statusTV.setText(" NOT AVAILABLE");
				showDebugMessage("In app purchase is not properly setup.Please contact info@hinkhoj.com");            
				return;
			}

			// Is it a failure?
			if (result.isFailure()) {
				showDebugMessage("Adsettings: Failed to query inventory: " + result);
				setupPremiumSettings(AppAccountManager.isAdsEnabled(getActivity()));
				return;
			}

			Log.d("hinkhoj", "Adsettings: Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(PremiumAccount.getPremiumAccountSKU());
			boolean mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

			AppAccountManager.setAdsStatus(getActivity(), !mIsPremium);
			LinearLayout subs_ll=(LinearLayout)view.findViewById(R.id.subscription_ll);
			List<String> premiumSettings= new ArrayList<String>();
			SkuDetails skuDetails=inventory.getSkuDetails(PremiumAccount.getPremiumAccountSKU());

			if(skuDetails!=null)
			{
				premiumSettings.add("Subscription Name: <b>"+skuDetails.getTitle()+"</b>");
				premiumSettings.add("Subscription Price: <b>"+skuDetails.getPrice()+"</b>");
			}
			if(premiumPurchase!=null)
			{

				if(premiumPurchase.getPurchaseState()==0)
				{
					premiumSettings.add("Subscription state: <b>Purchased</b>");
				}
				else if(premiumPurchase.getPurchaseState()==1)
				{
					premiumSettings.add("Subscription state: <b>Cancelled</b>");
				}
				else if(premiumPurchase.getPurchaseState()==2)
				{
					premiumSettings.add("Subscription state: <b>Refunded</b>");
				}
				Date purchaseDate= new Date(premiumPurchase.getPurchaseTime());
				SimpleDateFormat ft = 
						new SimpleDateFormat("dd.MM.yyyy");
				premiumSettings.add("Subscription Purchase date: <b>"+ft.format(purchaseDate)+"</b>");
				premiumSettings.add("Subscription Order Id: <b>"+premiumPurchase.getOrderId()+"</b>");
			}
			if(premiumSettings.size()>0)
			{


				ExpandableHeightListView subs_data_lv=(  ExpandableHeightListView )view.findViewById(R.id.subs_data_lv);

				subs_data_lv.setAdapter(new SimpleListAdapter(view.getContext(), R.layout.simple_list_item, premiumSettings));
				subs_data_lv.setExpanded(true);

				subs_ll.setVisibility(View.VISIBLE);
			}
			else
			{
				subs_ll.setVisibility(View.GONE);
			}

			setupPremiumSettings(!mIsPremium);

			Log.d("hinkhoj", "Adsettings: Initial inventory query finished; enabling main UI.");

			if(AppConstants.NEED_RENEW_PREMIUM)
			{
				if (inventory.hasPurchase(PremiumAccount.SKU_ANDROID_TEST_PURCHASE_GOOD)) {
					billingHelper.consumeAsync(inventory.getPurchase(PremiumAccount.SKU_ANDROID_TEST_PURCHASE_GOOD),null); 
				}
			}

		}

		private void showDebugMessage(String msg) {
			LinearLayout debug_ll=(LinearLayout)view.findViewById(R.id.debug_billing_ll);
			debug_ll.setVisibility(View.VISIBLE);
			TextView debugMsg=(TextView)view.findViewById(R.id.debug_msg_tv);
			debugMsg.setText(Html.fromHtml(msg));

		}

		private boolean verifyDeveloperPayload(Purchase purchase) {
			// TODO Auto-generated method stub

			return true;
		}
	};

	@Override
	public void onIabSetupFinished(IabResult result) {
		try
		{
		if (result.isSuccess()) {
			Log.d("hinkhoj","In-app Billing set up" + result);

			if(DictCommon.IsConnected(view.getContext()) && billingHelper !=null)
			{

				billingHelper.queryInventoryAsync(mGotInventoryListener);

			}
			else
			{
				statusTV.setText(" NOT AVAILABLE");
			}


		} else {
			Log.d("hinkhoj","Problem setting up In-app Billing: " + result);
			statusTV.setText(" NOT AVAILABLE");        
		}
		}
		catch(Exception e)
		{
			Log.d("hinkhoj","Problem setting up In-app Billing: " + result);
			statusTV.setText(" NOT AVAILABLE");        
			
		}
	}

	@Override
	public void onDestroy() {
		disposeBillingHelper();
		super.onDestroy();
	}

	private void disposeBillingHelper() {
		try
		{
			if (billingHelper != null) {
				billingHelper.dispose();
			}
			billingHelper = null;
		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}
	}

}
