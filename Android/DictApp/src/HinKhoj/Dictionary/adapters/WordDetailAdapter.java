package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Hindi.Android.Common.HindiCommon;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordDetailAdapter  extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context _context;
	private ArrayList<String> words;
	public WordDetailAdapter(Context context,int source,List<String> items){

		mInflater = LayoutInflater.from(context);
		_context=context;
		words = new ArrayList<String>();
		if(items!=null && items.size()>0)
		{
			for (int i = 0; i < items.size(); ++i) {
				words.add(items.get(i));
			}
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return words.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position>=0 && position<words.size())
		{
			return words.get(position);
		}
		
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
			convertView = mInflater.inflate(R.layout.simple_list_item, parent, false);
			holder = new ViewHolder();
			holder.word = (TextView) convertView.findViewById(R.id.list_tv);
			DictCommon.setHindiFont(_context,holder.word);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.word.setText(HindiCommon.ShiftLeftSmallE(words.get(position)));
		return convertView;

	}

	private static class ViewHolder {
		TextView word;

	}

}
