package HinKhoj.Dictionary.fragments;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.AsyncTasks.WODFragmentLoadAsyncTask;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class WordOfDayFragment extends Fragment {
	private View view=null;
	private String eng_word=null;
	private static final String ARG_SELECT_POSITION = "select_position";
	private int selectedPosition;
	private static final String ARG_POSITION = "position";
	private int position;
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
		this.view = inflater.inflate(R.layout.dictionary_tools_word_of_day, container, false);
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
			isPageLoaded=true;
			CheckBox notificationCB=(CheckBox)view.findViewById(R.id.notification_cb);
			if(DictCommon.IsNotificationSet(view.getContext()))
			{
				notificationCB.setChecked(true);
			}
			else
			{
				notificationCB.setChecked(false);
			}
			notificationCB.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (((CheckBox) v).isChecked()) {
						DictCommon.setNotification(v.getContext(), true);
					}
					else
					{
						DictCommon.setNotification(v.getContext(), false);
					}
				}
			});

			Button save_word_btn=(Button)view.findViewById(R.id.save_this_word);

			save_word_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					saveEngWord();
				}
			});

			new WODFragmentLoadAsyncTask(this).execute(new Void[]{});
		}
	}

	public Context getFragmentContext()
	{
		return view.getContext();
	}

	private void saveEngWord()
	{
		if(eng_word!=null)
		{
			DictCommon.SaveWord(eng_word, false);
			Toast.makeText(view.getContext(), eng_word+" is saved.", Toast.LENGTH_LONG).show();
		}
	}
	public void initializeWOD( String[][] worddata) {
		// TODO Auto-generated method stub

		try
		{
			String eng_word=worddata[0][0];
			this.eng_word=eng_word;
			String eng_example=worddata[0][1];
			String hin_word=worddata[0][2];
			hin_word=HindiCommon.ShiftLeftSmallE(hin_word);
			String hin_example=worddata[0][3];
			String date=worddata[0][4];
			TextView eng_word_tv=(TextView)view.findViewById(R.id.eng_word_tv);
			if(eng_word!="")
			{
				TextView hin_word_tv=(TextView)view.findViewById(R.id.hin_word_tv);
				DictCommon.setHindiFont(view.getContext(), hin_word_tv);
				hin_word_tv.setVisibility(View.VISIBLE);

				hin_word_tv.setText(hin_word);
				eng_word_tv.setText(eng_word);

				TextView equal_tv=(TextView)view.findViewById(R.id.equal_sign);
				equal_tv.setVisibility(View.VISIBLE);
			}
			else
			{
				eng_word_tv.setText("Word of the day not available.");
			}

			TextView usage_tv=(TextView)view.findViewById(R.id.word_usage);
			DictCommon.setHindiFont(view.getContext(),usage_tv);
			String usage="No usage available.";
			if(eng_example!="" || hin_example!="")
			{
				usage=eng_example+" <br/> "+hin_example;
				usage=HindiCommon.ShiftLeftSmallE(usage);
			} 	 
			usage_tv.setText(Html.fromHtml(usage));

		}
		catch(Exception e)
		{
			DictCommon.LogException(e);
		}

		final NotificationManager notificationManager = (NotificationManager) 
				getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

	}

	public static WordOfDayFragment newInstance(Object[] args) {
		WordOfDayFragment f = new WordOfDayFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, (int)args[0]);
		b.putInt(ARG_SELECT_POSITION, (int)args[1]); 
		f.setArguments(b);
		return f;
	}

}
