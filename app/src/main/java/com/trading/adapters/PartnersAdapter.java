package com.trading.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.trading.R;
import com.trading.dao.PartnersDao;
import com.trading.utils.Const;
import com.trading.utils.Partner;

public class PartnersAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Partner> partners;
	private PartnersDao pd;
	public int id;
	public String name;
	public String name_l;
	public String address;
	public String phone;
	public int daysdelay;
	public Double debtsumm1;
	public int idskidka;
	public String category;
	private String searchname;
	public String idhenkel;
	public int week_day;
	public int filterweek_day=0;
	
	public PartnersAdapter(Context context ) {
		pd = new PartnersDao(context);
		
		mInflater = LayoutInflater.from(context);
		partners = new ArrayList<Partner>();
		getdata();
	}
	public PartnersAdapter(Context context, String name, int filterweek_day ) {
		pd = new PartnersDao(context);
		mInflater = LayoutInflater.from(context);
		partners = new ArrayList<Partner>();
		searchname=name;
		this.filterweek_day=filterweek_day;
		getdata();
	}

	public void getdata() {
		Cursor c = pd.getPartners(searchname, this.filterweek_day);
		//startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				id=c.getInt(c.getColumnIndex(Const.TABLE_PARTNERS_ID));
				name=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_NAME));
				name_l=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_NAME_L));
				address=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_ADDRESS));
				String phone=c.getString(c.getColumnIndex(Const.TABLE_PARTNERS_PHONE));
				daysdelay=c.getInt(c.getColumnIndex(Const.TABLE_PARTNERS_DAYSDELAY));
				debtsumm1=c.getDouble(c.getColumnIndex(Const.TABLE_PARTNERS_DEBTSUMM1));
				idskidka=c.getInt(c.getColumnIndex("idskidka"));
				category=c.getString(c.getColumnIndex("cat"));
				idhenkel=c.getString(c.getColumnIndex("idhenkel"));
				week_day=c.getInt(c.getColumnIndex("week_day"));
				
				 Partner temp = new Partner(id, name,name_l, address, phone, daysdelay, debtsumm1, idskidka, category, idhenkel,week_day);
				 partners.add(temp);
			} while (c.moveToNext());

		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partners.size();
	}

	@Override
	public Partner getItem(int i) {
		// TODO Auto-generated method stub
		return partners.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		View v = arg1;
		if ((v == null) || (v.getTag() == null)) {
			 v = mInflater.inflate(R.layout.partner_row, null);
			 holder = new ViewHolder();
			 holder.mPartnerId = (TextView)v.findViewById(R.id.partner_id);
			 holder.mPartnerName = (TextView)v.findViewById(R.id.partner_name);
			 holder.mPartnerDebtsumm1 = (TextView)v.findViewById(R.id.partner_debt);
			 holder.mPartnerCat = (TextView)v.findViewById(R.id.partner_cat);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
try {

		holder.mPartners = getItem(arg0);
		holder.mPartnerId.setText(String.valueOf( holder.mPartners.id));
		holder.mPartnerName.setText(holder.mPartners.name.toString());
		holder.mPartnerDebtsumm1.setText(holder.mPartners.debtsumm1.toString());
		holder.mPartnerCat.setText(holder.mPartners.category.toString());
		
} catch (Exception e) {
Toast.makeText(null, e.getMessage(), Toast.LENGTH_SHORT).show();
// TODO: handle exception
}

		v.setTag(holder);

		// TODO Auto-generated method stub
		return v;
	}

	public class ViewHolder {
		Partner mPartners;
		TextView mPartnerId;
		TextView mPartnerName;
		TextView mPartnerAddress;
		TextView mPartnerdaysdelay;
		TextView mPartnerDebtsumm1;
		TextView mPartnerCat;
		  
		
	}

	

}