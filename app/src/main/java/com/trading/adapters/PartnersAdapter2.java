package com.trading.adapters;

import java.util.ArrayList;
import java.util.List;

import com.trading.R;
import com.trading.dao.PartnersDao;
import com.trading.utils.Const;
import com.trading.utils.Partner;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PartnersAdapter2 extends ArrayAdapter<Partner> {
	private static String searchname;
	private static PartnersDao pd;
	private ArrayList<Partner> items;

	

	
	

	public PartnersAdapter2(Context context, int textViewResourceId,
			ArrayList<Partner> objects) {
		super(context, textViewResourceId, objects);
		pd = new PartnersDao(context);
		this.items = objects;
		searchname="";
		//notifyDataSetChanged();
	
	}

	public PartnersAdapter2(Context context, int textViewResourceId,
							ArrayList<Partner> objects, String search) {
		super(context, textViewResourceId, objects);
		pd = new PartnersDao(context);
		this.items = objects;
		searchname=search;
		//notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.partner_row, null);
			
		}
		Partner o = items.get(position);
		if (o != null) {
			TextView partner_name = (TextView) v.findViewById(R.id.partner_name);
			TextView partner_id = (TextView) v.findViewById(R.id.partner_id);
			TextView partner_debt = (TextView) v.findViewById(R.id.partner_debt);
			TextView partner_cat = (TextView) v.findViewById(R.id.partner_cat);
			if (partner_id != null) {
				partner_id.setText(String.valueOf(o.id));
			}
			if (partner_name != null) {
				partner_name.setText( o.name);
			}
			if (partner_debt != null) {
				partner_debt.setText( String.valueOf(o.debtsumm1));
			}
			if (partner_cat != null) {
				partner_cat.setText( o.category);
			}
				
		}
		return v;

	}

	public static ArrayList<Partner> getdata(Context context, String searchname, String notemptyfldname, int iweek_day ) {
		ArrayList<Partner> ret=new ArrayList<Partner>();
		if (pd ==null)
			pd = new PartnersDao(context);
		Cursor c;
		if (notemptyfldname!="")
		c = pd.getPartners(searchname, notemptyfldname, iweek_day );
		else
		c = pd.getPartners(searchname,iweek_day);
			
		
		//startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				int id=c.getInt(c.getColumnIndex(Const.TABLE_PARTNERS_ID));
				String name=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_NAME));
				String name_l=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_NAME_L));
				String address=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_ADDRESS));
				String phone=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_PHONE));
				int daysdelay=c.getInt(c.getColumnIndex(Const.TABLE_PARTNERS_DAYSDELAY));
				double debtsumm1=c.getDouble(c.getColumnIndex(Const.TABLE_PARTNERS_DEBTSUMM1));
				int idskidka=c.getInt(c.getColumnIndex("idskidka"));
				String category=c.getString(c.getColumnIndex("cat"));
				String idhenkel=c.getString(c.getColumnIndex("idhenkel"));
				int week_day=c.getInt(c.getColumnIndex("week_day"));
				 Partner temp = new Partner(id, name,name_l, address, phone, daysdelay, debtsumm1, idskidka, category,idhenkel, week_day);
				 ret.add(temp);
			} while (c.moveToNext());

		}
		return ret;

	}
}
