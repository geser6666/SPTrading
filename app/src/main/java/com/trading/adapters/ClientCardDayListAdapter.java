package com.trading.adapters;

import com.trading.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ClientCardDayListAdapter extends SimpleCursorAdapter
{
    private Cursor c;
    private Context context;
	public ClientCardDayListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
		this.c = c;
        this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View v = convertView;
         if (v == null) {
              LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              v = inflater.inflate(R.layout.three_col_item, null);
         }
         this.c.moveToPosition(position);
         TextView text1;
         TextView text2;
         TextView text3;
         text1=(TextView)v.findViewById(R.id.text1);
         text2=(TextView)v.findViewById(R.id.text2);
         text3=(TextView)v.findViewById(R.id.text3);
         text1.setText(c.getString(c.getColumnIndex("clientcardname")));
         text2.setText(c.getString(c.getColumnIndex("clientname")));
         text3.setText(c.getString(c.getColumnIndex("_id")));
         
         
         if (c.getInt(c.getColumnIndex("central_id"))<=0)
         {
        	 v.setBackgroundColor(Color.LTGRAY);
        	 text1.setTextColor(ColorStateList.valueOf(Color.BLACK));
        	 text2.setTextColor(ColorStateList.valueOf(Color.BLACK));
        	 text3.setTextColor(ColorStateList.valueOf(Color.BLACK));
        	 
         }else
         {
        	 v.setBackgroundColor(Color.BLACK);
        	 text1.setTextColor(ColorStateList.valueOf(Color.WHITE));
        	 text2.setTextColor(ColorStateList.valueOf(Color.WHITE));
        	 text3.setTextColor(ColorStateList.valueOf(Color.WHITE));
         } 
         
         
         
		return v;
       }
	

}
