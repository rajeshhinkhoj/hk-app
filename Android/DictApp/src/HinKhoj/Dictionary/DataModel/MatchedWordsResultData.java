package HinKhoj.Dictionary.DataModel;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;


public class MatchedWordsResultData {
	
	public List<MatchWordsData> MWList=null;
	
	public MatchedWordsResultData(MatchWordsData[] mwData)
	{
		this.MWList= new ArrayList<MatchWordsData>();
		if(mwData != null)
		{		
			this.MWList=Arrays.asList(mwData);
		
		}
		
	}

	}