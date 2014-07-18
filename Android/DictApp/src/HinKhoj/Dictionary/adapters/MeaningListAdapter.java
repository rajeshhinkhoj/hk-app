package HinKhoj.Dictionary.adapters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import HinKhoj.Dictionary.R;
import HinKhoj.Dictionary.UIData.StringIdRowItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

public class MeaningListAdapter extends BaseAdapter
{
   public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
   private List<String> grammarList;
   public final GrammarHeaderListAdapter headers;
   public final static int TYPE_SECTION_HEADER = 0;
   public String searchWord="";

   public MeaningListAdapter(Context context,String searchWord,Map<String,Map<String,List<StringIdRowItem>>> meaningMap)
   {
	   grammarList= new ArrayList<String>();
	   if(meaningMap.containsKey("EXACT"))
	   {
		   Map<String,List<StringIdRowItem>> grammarMap=meaningMap.get("EXACT");
      // Add Sections
      for(String grammar : grammarMap.keySet())
      {
    	  grammarList.add(grammar);
         StringIdListAdapter listadapter=new StringIdListAdapter(context, R.layout.stringid_list_item,grammarMap.get(grammar));
         this.addSection(grammar, listadapter);  
      }

	   }
	   if(meaningMap.containsKey("NEARBY"))
	   {
		   Map<String,List<StringIdRowItem>> grammarMap=meaningMap.get("NEARBY");
		// Add Sections
		      for(String grammar : grammarMap.keySet())
		      {
		    	  grammarList.add(grammar);
		         StringIdListAdapter listadapter=new StringIdListAdapter(context, R.layout.stringid_list_item,grammarMap.get(grammar));
		         this.addSection(grammar, listadapter);  
		      }
	   }
	   
      headers = new GrammarHeaderListAdapter(context, R.layout.grammar_header,searchWord,grammarList);
   }
   
   public void addSection(String section, Adapter adapter)
   {
      //this.headers.add(section);
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

