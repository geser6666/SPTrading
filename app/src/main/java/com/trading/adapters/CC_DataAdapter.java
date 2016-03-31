package com.trading.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.trading.utils.CC_data;
import com.trading.utils.CC_data_item;
import com.trading.utils.Partner;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;


public class CC_DataAdapter extends SimpleAdapter{

	public CC_DataAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}

	

	public static ArrayList<CC_data_item> get_List_CC_Data(ArrayList<CC_data> data) {
		ArrayList<CC_data_item> mList;
		mList = new ArrayList<CC_data_item>(); 

		for (int i=0; i<data.size();i++)
		{
			mList.add(new CC_data_item(String.valueOf(data.get(i).getId()), 
									String.valueOf(data.get(i).getCard_id()), 
									String.valueOf(data.get(i).getPosition_id()), 
									String.valueOf(data.get(i).getOst()), 
									String.valueOf(data.get(i).getZakaz()),
									String.valueOf(data.get(i).getTovname()),
									String.valueOf(data.get(i).getAvail()),
									String.valueOf(data.get(i).getTovar_id())
									
									
									
									));
		}
		return mList;


	}
	
	

}
