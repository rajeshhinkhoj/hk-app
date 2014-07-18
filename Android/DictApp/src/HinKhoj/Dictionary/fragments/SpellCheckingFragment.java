package HinKhoj.Dictionary.fragments;

import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.SpellCheckSearchTask;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.DataModel.SpellCheckResultData;
import HinKhoj.Dictionary.Helpers.AdvanceHindiTextWatcher;
import HinKhoj.Dictionary.adapters.SimpleListAdapter;
import HinKhoj.Dictionary.utils.Utils;
import HinKhoj.Hindi.KeyBoard.HindiCheckBoxHandler;
import HinKhoj.Hindi.KeyBoard.HindiEditText;
import HinKhoj.Hindi.KeyBoard.HindiKBHelper;
import HinKhoj.Hindi.KeyBoard.HindiKeyHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SpellCheckingFragment extends Fragment {


  private String spellWord=null;
  private HindiEditText spell_tv=null;
  private View view=null;
  private ProgressBar progressBar =null;
  private TextView message_tv=null;
  HindiKBHelper kbh=null;
  private static final String ARG_SELECT_POSITION = "select_position";
  private int selectedPosition;
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      position = getArguments().getInt(ARG_POSITION);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   view = inflater.inflate(R.layout.dictionary_tools_spell_checker, container, false);
    
   spell_tv=(HindiEditText)view.findViewById(R.id.spellET);
 
      Button check_spell_btn = (Button)view.findViewById(R.id.check_spell_btn);
      check_spell_btn.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View arg0) {
            startSpellCheck();

         }
      });
      
      kbh= new HindiKBHelper(getActivity());
		TableLayout helpLayout=(TableLayout)view.findViewById(R.id.kbhelpId);
		kbh.setHelpLayout(helpLayout);
		spell_tv.SetHindiKBHelper(kbh);
		HindiKeyHandler kh= new HindiKeyHandler(spell_tv);
		spell_tv.setOnKeyListener(kh);
		AdvanceHindiTextWatcher htw=new AdvanceHindiTextWatcher(spell_tv);
		spell_tv.addTextChangedListener(htw);

		spell_tv.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		UICommon.HideInputKeyBoard(spell_tv, view.getContext());
		CheckBox cb=(CheckBox)view.findViewById(R.id.cbh);
		cb.setOnCheckedChangeListener(new HindiCheckBoxHandler(spell_tv));


		String[] acList=DictCommon.GetDefaultAutoCompleteList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.dropdown_item,acList);
		spell_tv.setThreshold(1);
		spell_tv.setAdapter(adapter);
		progressBar=(ProgressBar)view.findViewById(R.id.searchProgressBar);
		message_tv=(TextView)view.findViewById(R.id.search_message);
		DictCommon.setHindiFont(view.getContext(), message_tv);      
        
		return view;
   }
   @Override
   public void onResume() {
      super.onResume();
   }
   
   private void startSpellCheck()
   {
	   spellWord=spell_tv.getText().toString();
	   spellWord=spellWord.trim();
	   if(spellWord.equalsIgnoreCase(""))
	   {
		   Toast.makeText(view.getContext(), "Please provide valid input for spell check", Toast.LENGTH_LONG).show();
		   return;
	   }
	   
	   cleanSpellViews();
	   DictCommon.hideKeyBoard(spell_tv.getContext(),spell_tv);
	   kbh.HideHelp();
	   progressBar.setVisibility(View.VISIBLE);
	   message_tv.setText("Checking if "+spellWord+" is correct spelling or not. Please wait...");
	   message_tv.setVisibility(View.VISIBLE);
	   
	   new SpellCheckSearchTask(this).execute(spellWord);	
   }

   private void cleanSpellViews() {
	// TODO Auto-generated method stub
	   
		LinearLayout correct_ll=(LinearLayout)view.findViewById(R.id.correct_spell_ll);
		LinearLayout wrong_ll=(LinearLayout)view.findViewById(R.id.wrong_spell_ll);
		correct_ll.setVisibility(View.GONE);
		wrong_ll.setVisibility(View.GONE);
		
	    LinearLayout similar_words_ll=(LinearLayout)view.findViewById(R.id.similar_words_list_layout);
	    similar_words_ll.setVisibility(View.GONE);
}

private static final String ARG_POSITION = "position";

   private int position;

   public static SpellCheckingFragment newInstance(int position) {
      SpellCheckingFragment f = new SpellCheckingFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, position);
      f.setArguments(b);
      return f;
   }

public void DisplaySpellCheckResult(SpellCheckResultData srd) {
	// TODO Auto-generated method stub
	
	 progressBar.setVisibility(View.GONE);
	 message_tv.setVisibility(View.GONE);
	LinearLayout correct_ll=(LinearLayout)view.findViewById(R.id.correct_spell_ll);
	LinearLayout wrong_ll=(LinearLayout)view.findViewById(R.id.wrong_spell_ll);
	correct_ll.setVisibility(View.GONE);
	wrong_ll.setVisibility(View.GONE);
	
 if(srd!=null)
 {
	 List<String> relatedWordsList=null;
	 
	 if(srd.correct_spelling)
	 {
		 correct_ll.setVisibility(View.VISIBLE);
	 }
	 else if(srd.recover_correct_spelling)
	 {
		 correct_ll.setVisibility(View.VISIBLE);
		 relatedWordsList=srd.recover_correct_list;
	 }
	 else if(srd.suggested_correct_spelling)
	 {
		 wrong_ll.setVisibility(View.VISIBLE);
		 relatedWordsList=srd.suggested_correct_list;
	 }
	 else
	 {
		 wrong_ll.setVisibility(View.VISIBLE);
	 }
	 if(relatedWordsList !=null && relatedWordsList.size()>0)
	 {
    LinearLayout similarWordsListLayout = (LinearLayout)view.findViewById(R.id.similar_words_list_layout);
    similarWordsListLayout.setVisibility(View.VISIBLE);
    
    SimpleListAdapter adapter = new SimpleListAdapter(getActivity(),
             R.layout.simple_list_item, relatedWordsList);
    final  ListView similarWordsList = (ListView)view.findViewById(R.id.similar_words_list);
    similarWordsList.setAdapter(adapter);
    Utils.updateListViewHeight(similarWordsList);
	 }
 }
}

public void setSpellCheckResult(String errorInfo) {
	// TODO Auto-generated method stub
	message_tv.setText(Html.fromHtml(errorInfo));
	message_tv.setVisibility(View.VISIBLE);
}

}
