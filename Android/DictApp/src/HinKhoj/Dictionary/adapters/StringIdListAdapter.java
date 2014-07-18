package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.List;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import HinKhoj.Dictionary.UIData.StringIdRowItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StringIdListAdapter extends BaseAdapter{

   private LayoutInflater mInflater;
   private Context _context;
   private ArrayList<StringIdRowItem> list;
   public StringIdListAdapter(Context context,int source,List<StringIdRowItem> wordsList){
      mInflater = LayoutInflater.from(context);
      _context=context;
    list = new ArrayList<StringIdRowItem>();
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
      return list.get(position);
   }

   @Override
   public long getItemId(int position) {
      // TODO Auto-generated method stub
      StringIdRowItem item=list.get(position);
      return item.getRId();
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      if (convertView == null) {
         convertView = mInflater.inflate(R.layout.stringid_list_item, parent, false);
         holder = new ViewHolder();
         holder.text = (TextView) convertView.findViewById(R.id.list_word);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }
        DictCommon.setHindiFont(_context, holder.text);
        holder.text.setText(list.get(position).getText());

      return convertView;

   }

   private static class ViewHolder {
      TextView text;
   }

}
