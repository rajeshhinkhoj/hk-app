package HinKhoj.Dictionary.adapters;


import HinKhoj.Dictionary.R;
import android.R.dimen;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple adapter which maintains an ArrayList of photo resource Ids. 
 * Each photo is displayed as an image. This adapter supports clearing the
 * list of photos and adding a new photo.
 *
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

   private String[] textArray;
   private Context _context;
   private LayoutInflater mInflater;
   private final int[] mIcons;


   private String[][] children = {
            {},
            {"Word of the day","Pronunciation","Spell checking","Word guess game"},          
            {"Saved Words","Search History"},
            {"Advertisement Settings"},
            {"Configure","Settings"},
            {},
            {},
            {},
            {},
            {}
   };


   public MyExpandableListAdapter(Context context,int resource) {
      mInflater = LayoutInflater.from(context);
      _context=context;
      Resources resources = _context.getResources();
      textArray = resources.getStringArray(R.array.drawer_text_array);
      TypedArray drawableArray = resources.obtainTypedArray(R.array.drawer_image_array);
      
      final int count = drawableArray.length();
      mIcons = new int[count];
      for ( int i=0; i<count; ++i ) {
        mIcons[i] = drawableArray.getResourceId(i, 0);
      }
      drawableArray.recycle();


   }


   public static  boolean isGroupParent(int groupId)
   {
	   if(groupId==1 || groupId==2 || groupId==3 || groupId==4)
	   {
		   return true;
	   }
	   return false;
   }
   
   @Override
public Object getChild(int groupPosition, int childPosition) {
      return children[groupPosition][childPosition];
   }

   @Override
public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
   }

   @Override
public int getChildrenCount(int groupPosition) {
      return children[groupPosition].length;
   }

   public TextView getGenericView() {
      // Layout parameters for the ExpandableListView
      AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
               ViewGroup.LayoutParams.MATCH_PARENT, _context.getResources().getDimensionPixelSize(R.dimen.drawer_item_height));
      TextView textView = new TextView(_context);
      textView.setLayoutParams(lp);
      // Center the text vertically
      textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
      // Set the text starting position
      textView.setPadding(_context.getResources().getDimensionPixelSize(R.dimen.drawer_padding_top), 0, 0, 0);
     // textView.setTextColor(Color.WHITE);
      textView.setTextSize(18.0f);
      return textView;
   }

   @Override
public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
      TextView textView = getGenericView();
      textView.setText(getChild(groupPosition, childPosition).toString());
      return textView;
   }

   @Override
public Object getGroup(int groupPosition) {
      return textArray[groupPosition];
   }

   @Override
public int getGroupCount() {
      return textArray.length;
   }

   @Override
public long getGroupId(int groupPosition) {
      return groupPosition;
   }

   @Override
public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
      final ViewHolder holder;
      if (convertView == null) {
         convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
         holder = new ViewHolder();
         holder.text = (TextView) convertView.findViewById(R.id.drawer_text_view_id);
         holder.image = (ImageView) convertView.findViewById(R.id.drawer_image_view_id);
         holder.arrow = (ImageView) convertView.findViewById(R.id.expandable_image_view_id);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      if(isGroupParent(groupPosition)){
         holder.arrow.setVisibility(View.VISIBLE);
      }
      else{
         holder.arrow.setVisibility(View.INVISIBLE);
      }

      holder.image.setBackgroundResource(mIcons[groupPosition]);
      holder.text.setText(textArray[groupPosition]);


      return convertView;
   }

   @Override
public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
   }

   @Override
public boolean hasStableIds() {
      return true;
   }

   private static class ViewHolder {
      TextView text;
      ImageView image;
      ImageView arrow;
   }
}