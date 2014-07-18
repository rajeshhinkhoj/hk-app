package HinKhoj.Dictionary.fragments;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.LoadGameAsyncTask;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.Common.UICommon;
import HinKhoj.Dictionary.DataModel.HangmanGameInfo;
import HinKhoj.Dictionary.DataModel.HangmanState;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class WordGuessGameFragment extends Fragment implements OnClickListener{

	private int chancesLeft=6;
	private int maxAttempt=6;
	private Button mB[] = new Button[26];
	private View view;
	private TextView holder_tv;
	private ImageView hangman;
	private TextView num_of_chances_left;
	private LinearLayout chanceleft_layout=null;
	private static final String ARG_POSITION = "position";

	private int position;
	private static final String ARG_SELECT_POSITION = "select_position";

	private int selectedPosition;
	private int wordPosition;
	private int keyBoardPressed;
	private String hint="";
	private String targetWord="";
	private HangmanState hangmanState=null;
	private TextView hintWord=null;
	private boolean isPageLoaded=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		selectedPosition=getArguments().getInt(ARG_SELECT_POSITION);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		view = inflater.inflate(R.layout.dictionary_tools_word_guess_game, container, false);
		isPageLoaded=false;
		if(selectedPosition==position)
		{
			LoadContent();
		}

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && view !=null ) {
			LoadContent();

		}
	}
	private void LoadContent() {

		if(!isPageLoaded && view!=null)
		{
			try
			{
				hangman= (ImageView) view.findViewById(R.id.hangman);
				hintWord=(TextView)view.findViewById(R.id.hint_textview);
				DictCommon.setHindiFont(view.getContext(), hintWord);
				num_of_chances_left= (TextView) view.findViewById(R.id.num_of_chances_left);
				num_of_chances_left.setText(chancesLeft+"");
				chanceleft_layout=(LinearLayout)view.findViewById(R.id.chance_left_layout);
				holder_tv= (TextView) view.findViewById(R.id.holder_txtview);
				Button newGame=(Button)view.findViewById(R.id.refresh_icon);
				newGame.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						reFreshWord();
					}
				});
				DictCommon.initializeHangmanDB();
				reFreshWord();
			}
			catch(Exception e)
			{
				Toast.makeText(view.getContext(), "Unable to load word guess game", Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
	@Override
	public void onResume() {
		super.onResume();
		setKeys();
	}

	@Override 
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		Log.v("hinkhoj","Hangman..options menu create called.");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.guess_game, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){

		}
		return true;
	}

	private void setKeys() {
		try
		{

			mB[0] = (Button) view.findViewById(R.id.xA);
			mB[1] = (Button) view.findViewById(R.id.xB);
			mB[2] = (Button) view.findViewById(R.id.xC);
			mB[3] = (Button) view.findViewById(R.id.xD);
			mB[4] = (Button) view.findViewById(R.id.xE);
			mB[5] = (Button) view.findViewById(R.id.xF);
			mB[6] = (Button) view.findViewById(R.id.xG);
			mB[7] = (Button) view.findViewById(R.id.xH);
			mB[8] = (Button) view.findViewById(R.id.xI);
			mB[9] = (Button) view.findViewById(R.id.xJ);
			mB[10] = (Button) view.findViewById(R.id.xK);
			mB[11] = (Button) view.findViewById(R.id.xL);
			mB[12] = (Button) view.findViewById(R.id.xM);
			mB[13] = (Button) view.findViewById(R.id.xN);
			mB[14] = (Button) view.findViewById(R.id.xO);
			mB[15] = (Button) view.findViewById(R.id.xP);
			mB[16] = (Button) view.findViewById(R.id.xQ);
			mB[17] = (Button) view.findViewById(R.id.xR);
			mB[18] = (Button) view.findViewById(R.id.xS);
			mB[19] = (Button) view.findViewById(R.id.xT);
			mB[20] = (Button) view.findViewById(R.id.xU);
			mB[21] = (Button) view.findViewById(R.id.xV);
			mB[22] = (Button) view.findViewById(R.id.xW);
			mB[23] = (Button) view.findViewById(R.id.xX);
			mB[24] = (Button) view.findViewById(R.id.xY);
			mB[25] = (Button) view.findViewById(R.id.xZ);
			view.findViewById(R.id.new_word).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hangman.setBackgroundResource(R.drawable.hangman_right);
					chancesLeft=6;
					((TextView) view.findViewById(R.id.u_got_it_right)).setText("");
					((TextView) view.findViewById(R.id.u_got_it_right)).setTextColor(getResources().getColor(R.color.black));
					((ImageView) view.findViewById(R.id.cross_image)).setBackgroundResource(0);
					num_of_chances_left.setText(chancesLeft+"");
					reFreshKeyBoard();
					reFreshWord();

				}
			});

			for (int i = 0; i < mB.length; i++)
				mB[i].setOnClickListener(this);
		}
		catch(Exception e)
		{
			UICommon.showLongToast(getActivity(), "Error loading keyboard");
		}

	}

	public void reFreshWord(){

		new LoadGameAsyncTask(this).execute(new Void[]{});


	}

	public void showNewGame(HangmanGameInfo hgi)
	{
		if(hgi !=null && hgi.Hint !=null)
		{
			hint=hgi.Hint.toUpperCase();
			hint="HINT: <b>"+hint+"</b>";
			hint=HindiCommon.ShiftLeftSmallE(hint);
			targetWord=hgi.Word.toUpperCase();

			hintWord.setText(Html.fromHtml(hint));
			hangmanState=new HangmanState(targetWord);
			hangmanState.setHangmanActivity(this);
			RefreshGuessWord(hangmanState.GetGuessWordString());
		}
		else
		{
			hintWord.setText(Html.fromHtml("Error while loading new game."));
		}
		RefreshImage(0);
		wordPosition = (int)(Math.random() *4 + 0);


		keyBoardPressed=0;
		((LinearLayout) view.findViewById(R.id.answerLayout)).setVisibility(View.GONE);
		chanceleft_layout.setVisibility(View.VISIBLE);
		reFreshKeyBoard();

	}
	public void  reFreshKeyBoard(){
		View kbv=view.findViewById(R.id.xKeyBoard);
		kbv.setVisibility(View.VISIBLE);
		for (int i = 0; i < mB.length; i++)
			mB[i].setBackgroundColor(getResources().getColor(R.color.sky_blue_background2));
	}
	public static WordGuessGameFragment newInstance(Object[] args) {
		WordGuessGameFragment f = new WordGuessGameFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]); 
		f.setArguments(b);
		return f;
	}

	@Override
	public void onClick(View arg0) {
		if(hangmanState==null)
		{
			Toast.makeText(arg0.getContext(), "Please wait for game to get initialized", Toast.LENGTH_SHORT).show();
			return;
		}
		if(hangmanState.attempt <= hangmanState.maxAttempts)
		{
			String letter=((Button)arg0).getText().toString();
			this.hangmanState.ChangeState(letter.charAt(0));
			((Button)arg0).setBackgroundColor(getResources().getColor(R.color.selected_tab_background));
		}
		/*
      if(chancesLeft<=0||(keyBoardPressed==targetWord.length())){
         return;
      }

      int index = targetWord.indexOf(((Button)arg0).getText().toString());

      while (index >= 0) {
//         ((TextView) word_holder.getChildAt(index)).setTextSize(15.0f);
         ((TextView) word_holder.getChildAt(index)).setText(((Button)arg0).getText());
         index = targetWord.indexOf(((Button)arg0).getText().toString(), index + 1);
         keyBoardPressed++;
      }
      boolean contains = targetWord.contains(((Button)arg0).getText());
      if(!contains){
         chancesLeft=chancesLeft-1;    
         RefreshImage(maxAttempt-chancesLeft);
      }



      if(chancesLeft==0){
         youAreHanged();
      }else{
         if(keyBoardPressed==targetWord.length()){
            youGotItRight();
         }  
      }
		 */

	}

	public void RefreshImage(int paramInt){
		int chancesLeft=6-paramInt;
		hangman.setBackgroundResource(0);
		num_of_chances_left.setText(chancesLeft+" ");
		switch (paramInt) {
		case 1:
			hangman.setBackgroundResource(R.drawable.hangman_right);
			break;
		case 2:
			hangman.setBackgroundResource(R.drawable.hangman2);
			break;
		case 3:
			hangman.setBackgroundResource(R.drawable.hangman3);
			break;
		case 4:
			hangman.setBackgroundResource(R.drawable.hangman4);
			break;
		case 5:
			hangman.setBackgroundResource(R.drawable.hangman5);
			break;
		case 6:
			hangman.setBackgroundResource(R.drawable.hangman6);
			break;

		default:
			hangman.setBackgroundResource(R.drawable.hangman_right);
			break;
		}
	}

	private void youAreHanged(){
		chanceleft_layout.setVisibility(View.GONE);

		((TextView) view.findViewById(R.id.u_got_it_right)).setText("You are Hanged");
		((TextView) view.findViewById(R.id.u_got_it_right)).setTextColor(getResources().getColor(R.color.red));
		((ImageView) view.findViewById(R.id.cross_image)).setBackgroundResource(0);
		((ImageView) view.findViewById(R.id.cross_image)).setBackgroundResource(R.drawable.wrong_word);
		((LinearLayout) view.findViewById(R.id.answerLayout)).setVisibility(View.VISIBLE);
		((TextView) view.findViewById(R.id.word_answer)).setText(targetWord);
	}

	private void youGotItRight(){
		chanceleft_layout.setVisibility(View.GONE);
		((TextView) view.findViewById(R.id.u_got_it_right)).setText(getResources().getString(R.string.u_got_it_right));
		((TextView) view.findViewById(R.id.u_got_it_right)).setTextColor(getResources().getColor(R.color.green));
		((ImageView) view.findViewById(R.id.cross_image)).setBackgroundResource(0);
		((ImageView) view.findViewById(R.id.cross_image)).setBackgroundResource(R.drawable.green_checkmark_small);
	}

	public void ShowHint(String string) {
		// TODO Auto-generated method stub
		this.hintWord.setText(string);
	}

	public void DisplaySuccess(String targetWord2) {
		// TODO Auto-generated method stub
		this.youGotItRight();
		hideKeyBoard();
	}

	private void hideKeyBoard() {
		// TODO Auto-generated method stub
		View kbv=view.findViewById(R.id.xKeyBoard);
		kbv.setVisibility(View.GONE);
	}



	public void DisplayFailure(String targetWord2) {
		// TODO Auto-generated method stub
		this.youAreHanged();
		hideKeyBoard();
	}


	public void RefreshGuessWord(String displayString) {
		// TODO Auto-generated method stub
		//
		holder_tv.setText(displayString);

	}



}
