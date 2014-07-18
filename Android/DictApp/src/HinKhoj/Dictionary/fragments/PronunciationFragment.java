package HinKhoj.Dictionary.fragments;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Helpers.Text2SpeechHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class PronunciationFragment extends Fragment {

   
	Text2SpeechHandler t2sHandler=null;
	EditText wordET=null;
	private static final String ARG_SELECT_POSITION = "select_position";
	private int selectedPosition;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      position = getArguments().getInt(ARG_POSITION);
      t2sHandler= new Text2SpeechHandler(getActivity());
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.dictionary_tools_pronuncitation, container, false);
       Button pronunBtn= (Button)view.findViewById(R.id.pronunciation_btn);
      pronunBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			pronunciate();
			
		}
	});
      
      wordET=(EditText)view.findViewById(R.id.pronunciation_et);
      return view;
   }

   private static final String ARG_POSITION = "position";

   private int position;

   private void pronunciate()
   {
	   String word=wordET.getText().toString();
	   word=word.trim();
	   if(word!="")
	   {
		   t2sHandler.speakOut(word);
	   }
   }
   public static PronunciationFragment newInstance(int position) {
      PronunciationFragment f = new PronunciationFragment();
      Bundle b = new Bundle();
      b.putInt(ARG_POSITION, position);
      f.setArguments(b);
      return f;
   }
   
   @Override
   public void onDestroy()
   {
	   if(t2sHandler !=null)
	   {
		   t2sHandler.onDestroy();
	   }
	   super.onDestroy();
	   
   }

}
