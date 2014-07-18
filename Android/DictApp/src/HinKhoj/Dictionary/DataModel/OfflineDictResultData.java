package HinKhoj.Dictionary.DataModel;

import java.util.ArrayList;
public class OfflineDictResultData {
	public DictionaryWordData[] dictDataList=null;
	public OfflineDictResultData(ArrayList<DictionaryWordData> dwdList)
	{
		if(dwdList !=null)
		{
			this.dictDataList= new DictionaryWordData[dwdList.size()];
			int i=0;
			for(DictionaryWordData dwd : dwdList)
			{
				dictDataList[i++]=dwd;
			}
		}
	}

}
