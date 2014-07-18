package HinKhoj.Dictionary.DataModel;

public class PremiumAccountDetails {
	private boolean isActive=false;
	private String accountName="";
	private int price;
	
	public PremiumAccountDetails()
	{
		
	}
	
	public void setAccountName(String accountName)
	{
		this.accountName=accountName;
	}
	
	@Override
	public String toString()
	{
		String premiumInfo=accountName+" is "+isActive+" for "+price;
		return premiumInfo;
	}
	

}
