package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GrammarHeaderListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context _context;
	private ArrayList<String> list;
	private String word;
	public GrammarHeaderListAdapter(Context context,int source,String word,List<String> wordsList){
		mInflater = LayoutInflater.from(context);
		_context=context;
		this.word=word;
		list = new ArrayList<String>();
		for (int i = 0; i < wordsList.size(); ++i) {
			list.add(wordsList.get(i));
		}

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grammar_header, parent, false);
			holder = new ViewHolder();
			holder.match_header=(RelativeLayout)convertView.findViewById(R.id.match_header);
			holder.match_header.setVisibility(View.GONE);
			
			holder.word = (TextView) convertView.findViewById(R.id.word);
			holder.grammar = (TextView) convertView.findViewById(R.id.grammar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DictCommon.setHindiFont(_context, holder.word);
		holder.word.setText(word);
		TextView check_tv=(TextView)holder.match_header.findViewById(R.id.checkmark_tv);
		DictCommon.setHindiFont(_context, check_tv);
		ImageView checkmark_image=(ImageView)holder.match_header.findViewById(R.id.checkmark_image);
		TextView match_tv=(TextView)holder.match_header.findViewById(R.id.match_name);
		if(list.get(position)=="NEARBY")
		{

			check_tv.setText(HindiCommon.ShiftLeftSmallE(word));
			holder.grammar.setText(Html.fromHtml("Near by words"));
			holder.match_header.setVisibility(View.VISIBLE);
			checkmark_image.setVisibility(View.GONE);
			match_tv.setText("NEAR BY \nWORDS");
		}
		else if(list.get(position)=="DEF")
		{
			check_tv.setText(HindiCommon.ShiftLeftSmallE(word));
			holder.grammar.setText(Html.fromHtml("Definition"));
			holder.match_header.setVisibility(View.VISIBLE);
			checkmark_image.setVisibility(View.GONE);
			match_tv.setText("WORD \nDEFINITION");
		}
		else{
			check_tv.setText(HindiCommon.ShiftLeftSmallE(word));
			holder.grammar.setText(Html.fromHtml("("+list.get(position)+")"));
			if(position==0)
			{
				holder.match_header.setVisibility(View.VISIBLE);
				checkmark_image.setVisibility(View.VISIBLE);
				match_tv.setText("EXACT\nMATCH");
			}
			else
			{
				holder.match_header.setVisibility(View.GONE);
			}
		}

		return convertView;

	}

	private static class ViewHolder {
		TextView word;
		TextView grammar;
		RelativeLayout match_header;
	}

}
