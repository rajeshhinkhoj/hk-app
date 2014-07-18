package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import HinKhoj.Dictionary.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SavedWordListAdapter extends BaseAdapter
{
   public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
   public final ArrayAdapter<String> headers;
   public final static int TYPE_SECTION_HEADER = 0;

   public SavedWordListAdapter(Context context,List<String> savedWords,boolean showCheckBox)
   {
      headers = new ArrayAdapter<String>(context, R.layout.list_header);

      String[] headerValues = new String[] { "A", "B", "C",
               "D", "E", "F", "G", "H",
               "I", "J", "K", "L", "M", "N",
               "O", "P", "Q", "R", "S", "T",
               "U", "V", "W","X","Y","Z",
               "अ","आ","इ","ई","उ",
               "ऊ","ए","ऐ","ओ","औ","अं","अः","ऋ",
            "क", "ख","ग","घ","ङ",
            "च","छ","ज","झ","ञ",
            "ट","ठ","ड","ढ","ण",
            "त","थ","द","ध","न",
            "प","फ","ब","भ","म",
            "य","र","ल","व","श",
            "ष","स","ह","क्ष","त्र",
            "ज्ञ"};



      // Add Sections
      for (int i = 0; i < headerValues.length; i++)
      {
         String headerValue = headerValues[i];
         SavedWordAdapter listadapter=new SavedWordAdapter(context, R.layout.saved_word_list_item, showCheckBox,headerValue,findInString(headerValue,  savedWords));
         if(findInString(headerValue,  savedWords).size()>0){
            this.addSection(headerValues[i], listadapter);  
         }
      }

   }
   public static ArrayList<String> findInString(String needle, List<String> haystack) {
	   needle=needle.toLowerCase();
      ArrayList<String> found = new ArrayList<String>();

      for(String s : haystack) {
    	  s=s.toLowerCase();
         if(s.startsWith(needle)) {
            found.add(s);
         }
      }

      return found;
   }
   public void addSection(String section, Adapter adapter)
   {
      this.headers.add(section);
      this.sections.put(section, adapter);
   }

   @Override
public Object getItem(int position)
   {
      for (Object section : this.sections.keySet())
      {
         Adapter adapter = sections.get(section);
         int size = adapter.getCount() + 1;

         // check if position inside this section
         if (position == 0) return section;
         if (position < size) return adapter.getItem(position - 1);

         // otherwise jump into next section
         position -= size;
      }
      return null;
   }

   @Override
public int getCount()
   {
      // total together all sections, plus one for each section header
      int total = 0;
      for (Adapter adapter : this.sections.values())
         total += adapter.getCount() + 1;
      return total;
   }

   @Override
   public int getViewTypeCount()
   {
      // assume that headers count as one, then total all sections
      int total = 1;
      for (Adapter adapter : this.sections.values())
         total += adapter.getViewTypeCount();
      return total;
   }

   @Override
   public int getItemViewType(int position)
   {
      int type = 1;
      for (Object section : this.sections.keySet())
      {
         Adapter adapter = sections.get(section);
         int size = adapter.getCount() + 1;

         // check if position inside this section
         if (position == 0) return TYPE_SECTION_HEADER;
         if (position < size) return type + adapter.getItemViewType(position - 1);

         // otherwise jump into next section
         position -= size;
         type += adapter.getViewTypeCount();
      }
      return -1;
   }

   public boolean areAllItemsSelectable()
   {
      return false;
   }

   @Override
   public boolean isEnabled(int position)
   {
      return (getItemViewType(position) != TYPE_SECTION_HEADER);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent)
   {
      int sectionnum = 0;
      for (Object section : this.sections.keySet())
      {
         Adapter adapter = sections.get(section);
         int size = adapter.getCount() + 1;

         // check if position inside this section
         if (position == 0) return headers.getView(sectionnum, convertView, parent);
         if (position < size) return adapter.getView(position - 1, convertView, parent);

         // otherwise jump into next section
         position -= size;
         sectionnum++;
      }
      return null;
   }

   @Override
   public long getItemId(int position)
   {
      return position;
   }


}

