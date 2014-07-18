package HinKhoj.Dictionary.DataModel;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;


public class SpellCheckResultData {
	
	public List<String> suggested_correct_list=null;
	public List<String> recover_correct_list=null;
	public boolean correct_spelling=false;
	public boolean suggested_correct_spelling=false;
	public boolean recover_correct_spelling=false;
	public SpellCheckResultData(SpellCheckData spellCheckData)
	{
		this.suggested_correct_list=new ArrayList<String>();
		this.recover_correct_list=new ArrayList<String>();
		if(spellCheckData != null)
		{
			this.correct_spelling=spellCheckData.correct_spelling;
			this.suggested_correct_spelling=spellCheckData.suggested_correct_spelling;
			this.recover_correct_spelling=spellCheckData.recover_correct_spelling;
			if(spellCheckData.suggested_correct_list!=null)
			{
				this.suggested_correct_list=Arrays.asList(spellCheckData.suggested_correct_list);
			}
			if(spellCheckData.recover_correct_list!=null)
			{
				this.recover_correct_list=Arrays.asList(spellCheckData.recover_correct_list);
			}
		}
		
	}

	}


