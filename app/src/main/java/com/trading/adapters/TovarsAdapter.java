package com.trading.adapters;

import java.util.ArrayList;

import com.trading.R;
import com.trading.dao.TovarsDao;
import com.trading.utils.Const;
import com.trading.utils.Tovar;

import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TovarsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private TovarsDao td;
	private ArrayList<Tovar> tovars;
    private int typeview; //1-остаток общий(доступно), 2-кол-во, 3-оба
    private boolean onlyost =false;
    private int idgr;
    private int idskidka=-1;
	public int id;
	private String searchStr="";

	public String name;
	public String name_l;
	public String ed_izm;
	public Double cenands;
	public Double available;
	public Double kolvo;
	public int parentid;
	public Double skidka1;
	public Double skidka2;
	public Double skidka3;
	public Double seb;
	public boolean isOnlyost() {
		return onlyost;
	}
	public void setOnlyost(boolean onlyost) {
		this.onlyost = onlyost;
		tovars = new ArrayList<Tovar>();
		getdata(idskidka);
	}
/*	public TovarsAdapter(Context context, long idgr, int typeview, boolean onlyost) {
		td = new TovarsDao(context);
		this.idgr=(int)idgr;
		this.onlyost=onlyost;
		this.typeview=typeview;

		mInflater = LayoutInflater.from(context);
		tovars = new ArrayList<Tovar>();
		getdata(0);
	}
	*/
	public TovarsAdapter(Context context, long idgr, int idskidka, int typeview, boolean onlyost, String searchQuery) {
		td = new TovarsDao(context);
		this.idgr=(int)idgr;
		this.onlyost=onlyost;
		this.typeview=typeview;
		this.idskidka=idskidka;
		this.searchStr=searchQuery;
		mInflater = LayoutInflater.from(context);
		tovars = new ArrayList<Tovar>();
		getdata(idskidka);
	}
	
	public void getdata() {
		int typv;
		if (this.onlyost) 
		 typv=0;	 
		 else
			 typv=this.typeview;
		Cursor c;
		if (searchStr.equals("")){
		c= td.getTovars(this.idgr, typv);
		}
		else{
			c = td.getTovars(searchStr, typv);
		}
		// startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				id = c.getInt(c.getColumnIndex("_id"));
				name = c.getString(c.getColumnIndex("name"));
				name_l = c.getString(c.getColumnIndex("name_l"));
				ed_izm = c.getString(c.getColumnIndex("ed_izm"));
				cenands = c.getDouble(c.getColumnIndex("cenands"));
				available = c.getDouble(c.getColumnIndex("available"));
				kolvo = c.getDouble(c.getColumnIndex("ost"));
				parentid = c.getInt(c.getColumnIndex("parentid"));
				skidka1 = c.getDouble(c.getColumnIndex("skidka1"));
				skidka2 = c.getDouble(c.getColumnIndex("skidka2"));
				skidka3 = c.getDouble(c.getColumnIndex("skidka3"));
				seb = c.getDouble(c.getColumnIndex("seb"));
				
				Tovar temp = new Tovar(c.getInt(c.getColumnIndex("_id")), 
						c.getString(c.getColumnIndex("name")),
						c.getString(c.getColumnIndex("name_l")),
						c.getString(c.getColumnIndex("ed_izm")), 
						c.getDouble(c.getColumnIndex("cenands")), 
						c.getDouble(c.getColumnIndex("available")), 
						c.getDouble(c.getColumnIndex("ost")), 
						c.getInt(c.getColumnIndex("parentid")),
						c.getDouble(c.getColumnIndex("skidka1")),
						c.getDouble(c.getColumnIndex("skidka2")),
						c.getDouble(c.getColumnIndex("skidka3")),
						c.getDouble(c.getColumnIndex("seb")));
				tovars.add(temp);
			} while (c.moveToNext());

		}

	}
	//public Cursor getTovars()
	public void getdata( int idskidka) {
		int typv;
		if (this.onlyost) 
			 typv=0;	 
			 else
				 typv=this.typeview;
        Cursor c;
        if (searchStr.equals("")){
            c= td.getTovars(this.idgr, typv);
        }
        else{
            c = td.getTovars(searchStr, typv);
        }
		// startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				id = c.getInt(c.getColumnIndex("_id"));
				name = c.getString(c.getColumnIndex("name"));
				name_l= c.getString(c.getColumnIndex("name_l"));
				ed_izm = c.getString(c.getColumnIndex("ed_izm"));
				switch (idskidka) {
				case 1:
					cenands = c.getDouble(c.getColumnIndex("skidka1"));					
					break;
				case 2:
					cenands = c.getDouble(c.getColumnIndex("skidka2"));					
					break;
				case 3:
					cenands = c.getDouble(c.getColumnIndex("skidka3"));					
					break;

				default:
					cenands = c.getDouble(c.getColumnIndex("cenands"));
					break;
				}
				
				available = c.getDouble(c.getColumnIndex("available"));
				kolvo = c.getDouble(c.getColumnIndex("ost"));
				parentid = c.getInt(c.getColumnIndex("parentid"));
				Tovar temp = new Tovar(c.getInt(c.getColumnIndex("_id")),
						c.getString(c.getColumnIndex("name")),
						c.getString(c.getColumnIndex("name_l")),
						c.getString(c.getColumnIndex("ed_izm")),
						//c.getDouble(c.getColumnIndex("cenands")),
						cenands,
						c.getDouble(c
						.getColumnIndex("available")), c.getDouble(c
						.getColumnIndex("ost")), c.getInt(c
						.getColumnIndex("parentid")),
						c.getDouble(c.getColumnIndex("skidka1")),
						c.getDouble(c.getColumnIndex("skidka2")),
						c.getDouble(c.getColumnIndex("skidka3")),
						c.getDouble(c.getColumnIndex("seb")));
				tovars.add(temp);
			} while (c.moveToNext());

		}

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tovars.size();
	}

	@Override
	public Tovar getItem(int position) {
		// TODO Auto-generated method stub
		return tovars.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
private void SetHolderVisibility(ViewHolder holder)
{
	switch (typeview) {
		//показывается остаток по главному складу
	case 1:
		holder.mTovarOstName.setVisibility(View.INVISIBLE);
		holder.mTovarOst.setVisibility(View.INVISIBLE);
		holder.mTovarAvailableName.setVisibility(View.VISIBLE);
		holder.mTovarAvailable.setVisibility(View.VISIBLE);
		break;
		//показывается остаток по личному складу
	case 2:
		holder.mTovarOstName.setVisibility(View.VISIBLE);
		holder.mTovarOst.setVisibility(View.VISIBLE);
		holder.mTovarAvailableName.setVisibility(View.INVISIBLE);
		holder.mTovarAvailable.setVisibility(View.INVISIBLE);
		break;
		//показывается остаток по личному складу и по главному складу
	case 3:
		holder.mTovarOstName.setVisibility(View.VISIBLE);
		holder.mTovarOst.setVisibility(View.VISIBLE);
		holder.mTovarAvailableName.setVisibility(View.VISIBLE);
		holder.mTovarAvailable.setVisibility(View.VISIBLE);
		break;

	default:
		break;
	} 
	
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		View v = arg1;
		if ((v == null) || (v.getTag() == null)) {
			v = mInflater.inflate(R.layout.tovar_row_, null);
			holder = new ViewHolder();
			holder.mTovarId = (TextView) v.findViewById(R.id.tovarid);
			holder.mTovarName = (TextView) v.findViewById(R.id.tovarname);
			holder.mTovarEd_Izm = (TextView) v.findViewById(R.id.tovared_izm);
			holder.mTovarAvailableName = (TextView)v.findViewById(R.id.tvDostupno );
			holder.mTovarAvailable = (TextView)v.findViewById(R.id.tovar_dostupno);
			holder.mTovarOstName = (TextView)v.findViewById(R.id.tvOst);
			holder.mTovarOst = (TextView)v.findViewById(R.id.tovar_ost);
			holder.mCenands= (TextView)v.findViewById(R.id.tovarcena);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		try {

			holder.mTovars = getItem(arg0);
			holder.mTovarId.setText(String.valueOf(holder.mTovars.getId()));
			holder.mTovarName.setText(holder.mTovars.getName());
			holder.mTovarEd_Izm.setText(holder.mTovars.getEd_izm());
			holder.mTovarAvailable.setText(String.valueOf(holder.mTovars.getAvailable()));
			holder.mTovarOst.setText(String.valueOf(holder.mTovars.getKolvo()));
			holder.mCenands.setText(String.valueOf(holder.mTovars.getCenands()));
			 

			SetHolderVisibility(holder);
		} catch (Exception e) {
			Toast.makeText(null, e.getMessage(), Toast.LENGTH_SHORT).show();
			// TODO: handle exception
		}
		
		v.setTag(holder);

		// TODO Auto-generated method stub
		return v;
	}

	public class ViewHolder {
		Tovar mTovars;
		TextView mTovarId;
		TextView mTovarName;
		TextView mTovarEd_Izm;
		TextView mTovarAvailableName;
		TextView mTovarAvailable;
		TextView mTovarOstName;
		TextView mTovarOst;
		TextView mCenands;

	}

}
