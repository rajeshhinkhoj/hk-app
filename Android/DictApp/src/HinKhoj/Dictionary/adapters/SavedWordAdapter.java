package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.List;
import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.Common.DictCommon;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

public class SavedWordAdapter extends BaseAdapter{

   private LayoutInflater mInflater;
   private Context _context;
   private boolean _showCheckBox;
   private ArrayList<String> list;
   //raj  starts
   public static ArrayList<String> ArrayId=new ArrayList<String>();
   //raj  ends
   public SavedWordAdapter(Context context,int source,boolean showCheckBox,String startingWord,List<String> wordsList){
      mInflater = LayoutInflater.from(context);
      _context=context;
      _showCheckBox=showCheckBox;
    list = new ArrayList<String>();
      for (int i = 0; i < wordsList.size(); ++i) {
         list.add(wordsList.get(i));
      }
      //raj  starts
      ArrayId.clear();
      //raj  ends
   }
   @Override
   public int getCount() {
      return list.size();
   }

   @Override
   public Object getItem(int position) {
      return null;
   }

   @Override
   public long getItemId(int position) {
      return 0;
   }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      if (convertView == null) {
         convertView = mInflater.inflate(R.layout.saved_word_list_item, parent, false);
         holder = new ViewHolder();
         holder.text = (TextView) convertView.findViewById(R.id.saved_word);
         holder.button = (ImageButton) convertView.findViewById(R.id.list_button);
         holder.checkBox = (CheckBox) convertView.findViewById(R.id.delete_word_checkbox);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }
      
      //raj  starts
      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

         @Override
         public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

               if(isChecked)
                {
                  ArrayId.add(list.get(position));//add item position to arraylist if checked
                }
                else
                {
                  ArrayId.remove(list.get(position));//remove item position from arraylist if unchecked
                }

         }
      });
      //raj  ends
      
        DictCommon.setHindiFont(_context, holder.text);
           holder.text.setText(list.get(position));
           holder.button.setFocusable(false);
           if (_showCheckBox) {
              holder.button.setVisibility(View.GONE);
           }else{
              holder.checkBox.setVisibility(View.GONE);
           }

      return convertView;

   }

   public static class ViewHolder {
      TextView text;
      ImageButton button;
     CheckBox checkBox;
   }

}
