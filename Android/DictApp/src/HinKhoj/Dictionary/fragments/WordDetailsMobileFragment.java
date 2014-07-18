package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.DataModel.DictionaryWordData;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordDetailsMobileFragment extends Fragment  {

	private View view;
	private DictionaryWordData dwd=null;
	private boolean isHindi=false;
	private TextView main_tv=null;
	private TextView meaning_tv=null;
	private String main_word="";
	private String meaning_word="";
	int meaning_id=-1;
	private OnWordSelectedFromSearchSuccess mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack=(OnWordSelectedFromSearchSuccess)activity;
		} catch (Exception e) {
		}

	}

	public WordDetailsMobileFragment(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main_word="";
		if(savedInstanceState!=null)
		{
			main_word=savedInstanceState.getString("word");
			meaning_id=savedInstanceState.getInt("meaning_id");

		}
		Intent i=getActivity().getIntent();
		if(i!=null)
		{
			main_word=i.getStringExtra("word");
			meaning_id=i.getIntExtra("meaning_id", -1);
		}
	}

	private static String ARG_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.online_dictionary_mobile_word_details, container, false);

		main_tv = (TextView)view.findViewById(R.id.main_word);
		DictCommon.setHindiFont(view.getContext(), main_tv);

		meaning_tv = (TextView)view.findViewById(R.id.meaning_word);
		DictCommon.setHindiFont(view.getContext(), meaning_tv);

		ImageButton saveWord= (ImageButton)view.findViewById(R.id.save_main_word);
		saveWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save_word();
			}
		});

		ImageButton listenWord= (ImageButton)view.findViewById(R.id.listen_main_word);
		listenWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listen_word();
			}
		});

		
		ImageButton saveMeaningWord= (ImageButton)view.findViewById(R.id.save_meaning_word);
		saveMeaningWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save_meaning_word();
			}
		});

		ImageButton listenMeaningWord= (ImageButton)view.findViewById(R.id.listen_meaning_word);
		listenMeaningWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listen_meaning_word();
			}
		});
		
		
		if(meaning_id!=-1 || main_word !="")
		{
			refreshView(meaning_id, main_word);
		}
		else
		{
			clearAllViews();
		}
		return view;
	}

	private void save_word() {
		// TODO Auto-generated method stub
		if(this.dwd!=null && this.main_word!=null)
		{

			DictCommon.SaveWord(this.main_word,isHindi);
			Toast.makeText(view.getContext(), "word "+this.main_word+" successfully saved!!!", Toast.LENGTH_LONG).show();
		}
	}


	private void save_meaning_word() {
		// TODO Auto-generated method stub
		if(this.dwd!=null && this.main_word!=null)
		{


			String meaning_word="";
			if(isHindi)
			{
				meaning_word=dwd.eng_word;
			}
			else
			{
				meaning_word=dwd.hin_word;
			}
			
			DictCommon.SaveWord(meaning_word,!isHindi);
			Toast.makeText(view.getContext(), "word "+meaning_word+" successfully saved!!!", Toast.LENGTH_LONG).show();
		}
	}

	
	private void listen_word() {
		// TODO Auto-generated method stub
		if(this.dwd!=null && this.main_word!=null)
		{
			if(this.main_word!="")
			{
				if(isHindi)
				{
					DictCommon.listen_in_hindi(this.main_word);
				}
				else
				{
					if(mCallBack==null)
					{
						mCallBack=(OnWordSelectedFromSearchSuccess) getActivity();
					}
					mCallBack.onWordSpeak(this.main_word);
				}
			}
		}
	}
	

	private void listen_meaning_word() {
		// TODO Auto-generated method stub
		if(this.dwd!=null)
		{
			String meaning_word="";
			if(isHindi)
			{
				meaning_word=dwd.eng_word;
			}
			else
			{
				meaning_word=dwd.hin_word;
			}
			if(isHindi)
			{
			if(this.main_word!="")
			{
				if(mCallBack==null)
				{
					mCallBack=(OnWordSelectedFromSearchSuccess) getActivity();
				}
               mCallBack.onWordSpeak(meaning_word);
			}
			}
			else
			{
				DictCommon.listen_in_hindi(meaning_word);
			}
		}
	}


	public static WordDetailsMobileFragment newInstance(int position) {
		WordDetailsMobileFragment f = new WordDetailsMobileFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	public void refreshView(int meaningId,String word) {
		this.clearAllViews();
		TextView  translitateTV=(TextView)view.findViewById(R.id.translitate_word);

		DictResultData drd=DictCommon.dictResultData;
		this.dwd=DictCommon.GetDictionaryWordData(meaningId);
		if(drd!=null && this.dwd!=null)
		{


			isHindi=drd.IsHindi;
			//

			if(dwd.eng_example!=null)
			{
				dwd.eng_example=dwd.eng_example.trim();
			}
			if(dwd.hin_example!=null)
			{
				dwd.hin_example=dwd.hin_example.trim();
			}
			
			if((dwd.eng_example!=null && !dwd.eng_example.equalsIgnoreCase("")) || (dwd.hin_example!=null && !dwd.hin_example.equalsIgnoreCase("")))
			{
				TextView usage = (TextView)view.findViewById(R.id.word_usage);
				usage.setVisibility(View.VISIBLE);
				DictCommon.setHindiFont(view.getContext(), usage);
				String usageStr="<b>Usage:</b> "+dwd.eng_example+"<br/>"+dwd.hin_example;
				usageStr=HindiCommon.ShiftLeftSmallE(usageStr);
				usage.setText(Html.fromHtml(usageStr));
			}
			else
			{
				TextView usage = (TextView)view.findViewById(R.id.word_usage);
				usage.setVisibility(View.GONE);
			}
			if(dwd.htraslitate!="")
			{
				translitateTV.setText(Html.fromHtml("<i> pr. {"+dwd.htraslitate+"}</i>"));
			}
			else
			{
				translitateTV.setText("");
			}

			if(!drd.IsHindi)
			{
				main_tv.setText(dwd.eng_word);
				this.main_word=dwd.eng_word;

				meaning_tv.setText(Html.fromHtml(DictCommon.toCodeString(dwd.hin_word)));
				meaning_word=dwd.hin_word;
				
			}
			else
			{
				main_tv.setText(Html.fromHtml(DictCommon.toCodeString(dwd.hin_word)));	
				this.main_word=dwd.hin_word;

				meaning_tv.setText(dwd.eng_word);	
				meaning_word=dwd.eng_word;
				translitateTV.setText("");

				}	

		}
		else
		{
			if(word!=null)
			{
				main_tv.setText(word);
				this.main_word=word;
				meaning_tv.setText("");
				translitateTV.setText("");
			}
		}
	}

	private void clearAllViews() {
		// TODO Auto-generated method stub
		TextView usage=(TextView)view.findViewById(R.id.word_usage);
		usage.setVisibility(View.GONE);

	}
}
