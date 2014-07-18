package HinKhoj.Dictionary.DataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DictResultData {
	public List<DictionaryWordData> dictDataList=null;
	public List<DictionaryWordData> eng2eng_list=null;
	public List<String> eng_synonym_list=null;
	public List<String> eng_antonym_list=null;
	public List<DictionaryWordData> hin2hin_list=null;
	public List<String> hin_synonym_list=null;
	public List<String> hin_antonym_list=null;
	public boolean suggested_word;
	public boolean IsHindi;
	public String main_word;
	
	public DictResultData(DictCompleteResult completeDict)
	{
		if(completeDict !=null )
		{
			this.suggested_word=completeDict.suggested_word;
			this.IsHindi=completeDict.IsHindi;
			this.main_word=completeDict.main_word;
			if(completeDict.main_result!=null && completeDict.main_result.length >0)
			{
				this.dictDataList= new ArrayList<DictionaryWordData>();
				HashMap<String,String> wordHash= new HashMap<String,String>();
				for(int i=0;i<completeDict.main_result.length;i++)
				{
					DictionaryWordData tmpDWD=completeDict.main_result[i];
					String key=tmpDWD.eng_word.trim();
					key+="==";
					key+=tmpDWD.hin_word.trim();
					if(wordHash.containsKey(key))
					{
						continue;
					}
					else
					{
						wordHash.put(key, key);
						this.dictDataList.add(tmpDWD);
					}
				}
			}
			if(completeDict.eng2eng_result !=null )
			{
			  this.eng2eng_list=Arrays.asList(completeDict.eng2eng_result);
			}
			if(completeDict.eng_synonym_list !=null)
			{
                this.eng_synonym_list= Arrays.asList(completeDict.eng_synonym_list);
                
			}
			
			if(completeDict.eng_antonym_list !=null)
			{
                this.eng_antonym_list= Arrays.asList(completeDict.eng_antonym_list);
                
			}
            

			if(completeDict.hin2hin_result !=null )
			{
			  this.hin2hin_list=Arrays.asList(completeDict.hin2hin_result);
			}
			if(completeDict.hin_synonym_list !=null)
			{
                this.hin_synonym_list= Arrays.asList(completeDict.hin_synonym_list);
                
			}
			
			if(completeDict.hin_antonym_list !=null)
			{
                this.hin_antonym_list= Arrays.asList(completeDict.hin_antonym_list);
                
			}
			

			
		}
	}

}
