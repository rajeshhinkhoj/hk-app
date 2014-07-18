package HinKhoj.Dictionary.fragments;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.WordDetailsFragmentLoadTaskAsync;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.DataModel.DictResultData;
import HinKhoj.Dictionary.DataModel.DictionaryWordData;
import HinKhoj.Dictionary.adapters.WordDetailAdapter;
import HinKhoj.Dictionary.fragments.DictionarySearchFragment.OnWordSelectedFromSearchSuccess;
import HinKhoj.Dictionary.ui.ExpandableHeightListView;
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
public class WordDetailsFragment extends Fragment  {

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

	public WordDetailsFragment(){

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
		view = inflater.inflate(R.layout.online_dictionary_tab_word_details, container, false);
		new WordDetailsFragmentLoadTaskAsync(this).execute(new Void[]{});
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

	public static WordDetailsFragment newInstance(int position) {
		WordDetailsFragment f = new WordDetailsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	public void refreshView(int meaningId,String word) {
		try
		{
			this.clearAllViews();
			TextView  translitateTV=(TextView)view.findViewById(R.id.translitate_word);

			DictResultData drd=DictCommon.dictResultData;
			this.dwd=DictCommon.GetDictionaryWordData(meaningId);
			if(drd!=null && this.dwd!=null)
			{


				isHindi=drd.IsHindi;
				//


				if(dwd.eng_example!="" || dwd.hin_example !="")
				{
					TextView usage = (TextView)view.findViewById(R.id.word_usage);
					usage.setVisibility(View.VISIBLE);
					DictCommon.setHindiFont(view.getContext(), usage);
					String usageStr=dwd.eng_example+"<br/>"+dwd.hin_example;
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
					if(drd.main_word.equalsIgnoreCase(dwd.eng_word))
					{

						if(drd.eng_synonym_list!=null && drd.eng_synonym_list.size()>0)
						{
							showSynonyms(drd.eng_synonym_list);
						}
						if(drd.eng_antonym_list!=null && drd.eng_antonym_list.size()>0)
						{
							showAntonyms(drd.eng_antonym_list);	
						}
						if(drd.eng2eng_list!=null && drd.eng2eng_list.size()>0)
						{
							List<String> defMeaning=new ArrayList<String>(drd.eng2eng_list.size());
							for(DictionaryWordData dwd: drd.eng2eng_list)
							{
								defMeaning.add(dwd.hin_word);
							}
							showDefinition(defMeaning);	 
						}
					}


				}
				else
				{
					main_tv.setText(Html.fromHtml(DictCommon.toCodeString(dwd.hin_word)));	
					this.main_word=dwd.hin_word;

					meaning_tv.setText(dwd.eng_word);	
					meaning_word=dwd.eng_word;
					translitateTV.setText("");

					if(drd.main_word.equalsIgnoreCase(dwd.hin_word))
					{

						if(drd.hin_synonym_list!=null && drd.hin_synonym_list.size()>0)
						{
							showSynonyms(drd.hin_synonym_list);	 
						}
						if(drd.hin_antonym_list!=null && drd.hin_antonym_list.size()>0)
						{
							showAntonyms(drd.hin_antonym_list);
						}
						if(drd.hin2hin_list!=null && drd.hin2hin_list.size()>0)
						{
							List<String> defMeaning=new ArrayList<String>(drd.hin2hin_list.size());
							for(DictionaryWordData dwd: drd.hin2hin_list)
							{
								defMeaning.add(dwd.eng_word);
							}
							showDefinition(defMeaning);	 
						}
					}
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
		catch(Exception e)
		{
			Toast.makeText(getActivity(), "Error loading word details", Toast.LENGTH_LONG).show();
		}
	}

	private void clearAllViews() {
		// TODO Auto-generated method stub
		resetSynonyms();
		resetAntonyms();
		resetDefinition();

		TextView usage=(TextView)view.findViewById(R.id.word_usage);
		usage.setVisibility(View.GONE);

	}

	private void showSynonyms(List<String> synonymList)
	{
		ExpandableHeightListView  synonymsList = ( ExpandableHeightListView )view.findViewById(R.id.synonyms_list);
		synonymsList.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,synonymList));
		synonymsList.setExpanded(true);
		TextView synonyms=(TextView)view.findViewById(R.id.synonyms);
		synonyms.setVisibility(View.VISIBLE);
		synonymsList.setVisibility(View.VISIBLE);
		synonymsList.setDivider(getResources().getDrawable(R.drawable.list_divider));
		//Utils.updateListViewHeight(synonymsList);
	}

	private void showAntonyms(List<String> listItems)
	{
		ExpandableHeightListView  lv = ( ExpandableHeightListView)view.findViewById(R.id.antonyms_list);
		lv.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,listItems));
		lv.setExpanded(true);
		TextView heading=(TextView)view.findViewById(R.id.antonyms);
		heading.setVisibility(View.VISIBLE);
		lv.setVisibility(View.VISIBLE);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		//Utils.updateListViewHeight(lv);
	}

	private void showDefinition(List<String> listItems)
	{
		ExpandableHeightListView lv = (ExpandableHeightListView)view.findViewById(R.id.definition_list);
		lv.setAdapter(new WordDetailAdapter(view.getContext(), R.layout.simple_list_item,listItems));
		lv.setExpanded(true);
		TextView heading=(TextView)view.findViewById(R.id.definition);
		heading.setVisibility(View.VISIBLE);
		lv.setVisibility(View.VISIBLE);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		//Utils.updateListViewHeight(lv);
	}


	private void resetSynonyms()
	{
		ExpandableHeightListView synonymsList = (ExpandableHeightListView)view.findViewById(R.id.synonyms_list);	   
		TextView synonyms=(TextView)view.findViewById(R.id.synonyms);
		synonyms.setVisibility(View.GONE);
		synonymsList.setVisibility(View.GONE);
	}


	private void resetAntonyms()
	{
		ExpandableHeightListView listView = (ExpandableHeightListView)view.findViewById(R.id.antonyms_list);
		TextView heading=(TextView)view.findViewById(R.id.antonyms);
		listView.setVisibility(View.GONE);
		heading.setVisibility(View.GONE);
	}

	private void resetDefinition()
	{
		ExpandableHeightListView listView = (ExpandableHeightListView)view.findViewById(R.id.definition_list);
		TextView heading=(TextView)view.findViewById(R.id.definition);
		listView.setVisibility(View.GONE);
		heading.setVisibility(View.GONE);
	}

	public void delayedInitialization() {
		// TODO Auto-generated method stub
		if(view!=null)
		{

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
		}

	}

}
