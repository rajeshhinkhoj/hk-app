package HinKhoj.Dictionary.DataModel;
public class wordofthedayresultdata {
	public DictionaryWordofthedayData[] dictDataList=null;
	public wordofthedayresultdata(DictionaryWordofthedayData[] rd)
	{
		if(rd !=null)
		{
			this.dictDataList=rd.clone();
		}
	}

}
