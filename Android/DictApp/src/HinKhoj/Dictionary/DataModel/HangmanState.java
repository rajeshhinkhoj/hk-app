package HinKhoj.Dictionary.DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import HinKhoj.Dictionary.fragments.WordGuessGameFragment;

public class HangmanState implements Serializable
{
	public String targetWord;
	public int maxAttempts;
	public int attempt;
	public ArrayList<Character> clickedLetterList;
	private ArrayList<Boolean> matchMap;
	private WordGuessGameFragment wgf;

	public HangmanState(String targetWord)
	{
		this.targetWord=targetWord.toUpperCase();
		this.attempt=0;
		this.clickedLetterList= new ArrayList<Character>();
		this.maxAttempts=6;
		this.matchMap=new ArrayList<Boolean>();
		for(int i=0;i<this.targetWord.length();i++)
		{
			if(this.targetWord.charAt(i)==' ')
			{
				this.matchMap.add(i, true);
			}
			else
			{
				this.matchMap.add(i, false);
			}
		}
	}

	public void ChangeState(char letter)
	{
		if(attempt>maxAttempts)
		{
			return;
		}
		clickedLetterList.add(letter);
		Boolean isMatched=false;
		for(int i=0;i<this.targetWord.length();i++)
		{
			if(this.targetWord.charAt(i) == letter)
			{
				isMatched=true;
				this.matchMap.set(i, true);
			}
		}
		if(isMatched)
		{
			RefreshGuessWord();
			if(AllWordMatched())
			{
				wgf.DisplaySuccess(this.targetWord);
			}
		}
		else
		{
			attempt++;
			wgf.RefreshImage(attempt);
			if(attempt>=maxAttempts)
			{
				wgf.DisplayFailure(this.targetWord);
				
				return;
			}
		}

	}

	private boolean AllWordMatched()
	{
		for(int i=0;i<this.matchMap.size();i++)
		{
			if(!(Boolean)matchMap.get(i))
			{
				return false;
			}
		}
		return true;
	}

	private void RefreshGuessWord()
	{
		String displayString=GetGuessWordString();
		// TODO Auto-generated method stub
		wgf.RefreshGuessWord(displayString);
	}

	public String GetGuessWordString()
	{
		// TODO Auto-generated method stub
		String display="";
		for(int i=0;i<this.matchMap.size();i++)
		{
			if(matchMap.get(i))
			{
				display+=(targetWord.charAt(i)+" ");
			}
			else
			{
				if(targetWord.charAt(i)==' ')
				{
					display+="  ";
				}
				else
				{
					display+="- ";
				}
			}
		}
		return display;
	}
	
	public void setHangmanActivity(WordGuessGameFragment wgf)
	{
		this.wgf=wgf;
	}


}