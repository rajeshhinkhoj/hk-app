package HinKhoj.Dictionary.billing;

/**
 * These are properties of the app, i.e your gplay keys, gmaps keys, adnetwork keys etc
 * 
 * @author Blundell
 * 
 */
public class AppProperties {

    /**
     * In a real app, either hide this on your server
     * or compose it of multiple concat Strings so it's not so easy to find
     */
    
    public static String getBase64Key()
    {	
    	String baseKey2="mH4RH5i9rkEAa8imLwYtKhuADK3+HSRFOHn/ngmV/EG2AD1iDqM5hDwB1F1EYURlgAxrvprvVmZQM1QIDAQAB";
    	String baseKey1="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsW18LGulse3GnbQMev7P27K/HAEg95YCuAeVeSJf9GfKX68kABomW9YA3BXAcIQDtY/hxATqbkGTJG4GjGhZGWPrK5m80SI5NqHxIIA7B8xXhDBWpy8Rj29j0N5ZdfmwWjrDtqL8kt+nZbZl3s/l8tafIk9Zinmnw3muo1I7xHnULC74FdCA/kAIbrnyG1f3VW+SFH55pLbmXsc/rRv8thhoatha6i+yQZswJA0FqFjlIZLWd9FRrJjxIw4Q767kxsyeP7/";
    	return baseKey1+baseKey2;
    }
    
}
