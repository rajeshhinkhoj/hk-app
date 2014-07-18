package HinKhoj.Dictionary.billing.domain.items;

import HinKhoj.Dictionary.Constants.AppConstants;

/**
 * This is an object representation of the Passport object in Google Play
 * Here you can store the items ID (its SKU) it's price and any other fields
 * 
 * @author Blundell
 * 
 */
public class PremiumAccount {

   // public static final String SILVER_SKU = "hinkhoj_premium_account_silver_2014"; // Replace this with your item ID;
	private static final String PREMIUM_SKU = "hinkhoj_premium_75_annual"; // Replace this with your item ID;
	//
	//public static final String PREMIUM_SKU = "android.test.purchased"; // Replace this with your item ID;
	public static final String SKU_ANDROID_TEST_PURCHASE_GOOD = "android.test.purchased";
	
	public static String getPremiumAccountSKU()
	{
		if(AppConstants.IN_APP_BILLING_TEST)
		{
			return SKU_ANDROID_TEST_PURCHASE_GOOD;
		}
		else
		{
			return PREMIUM_SKU;
		}
	}

}
