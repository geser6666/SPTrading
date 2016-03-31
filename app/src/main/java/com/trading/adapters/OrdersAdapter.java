package com.trading.adapters;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.trading.R;
import com.trading.dao.OrdersDao;
import com.trading.db.DB;
import com.trading.utils.Const;
import com.trading.utils.Order;

public class OrdersAdapter extends BaseAdapter {
	private boolean onlyOpen; 
	private int typedocis;
	private Date fdate;
	private Date ldate;
	private LayoutInflater mInflater;
	private ArrayList<Order> orders;
	private Context context;
	private DB dba;
	private int typedoc;
	private int typepay;
	private int partnerid;
	
	public OrdersAdapter(Context context ) {
		this.context=context;
		mInflater = LayoutInflater.from(context);
		orders = new ArrayList<Order>();
		onlyOpen=false;
		typedocis=1;
		getdata();
		
	}
	
	public void SetPeriod(Date fd, Date ld)
	{
		this.fdate=fd;
		this.ldate=ld;
		orders = new ArrayList<Order>();
		getdata();
	}
	public void SetPeriod(String fd, String ld)
	{
		Date d;
		d=Date.valueOf(fd);
		this.fdate=d;
		d=Date.valueOf(ld);
		this.ldate=d;
		orders = new ArrayList<Order>();
		getdata();
	}
	public ArrayList<Date> GetPeriod()
	{
		ArrayList<Date> ar= new ArrayList<Date>();
		ar.add(fdate);
		ar.add(ldate);
		return ar;
	}

	public OrdersAdapter(Context context, boolean onlyopen, int typedocis, Date fdate, Date ldate,int partnerid ) {
		this.context=context;
		mInflater = LayoutInflater.from(context);
		orders = new ArrayList<Order>();
		onlyOpen=onlyopen;
		this.typedocis=typedocis;
		if ((fdate == null) || (ldate == null))
		{
			this.fdate=new Date(Calendar.getInstance().getTimeInMillis());
			this.ldate=new Date(Calendar.getInstance().getTimeInMillis());
			
		}else
		{	
		this.fdate=fdate;
		//this.ldate=ldate;
		this.ldate=new Date(Calendar.getInstance().getTimeInMillis());
		}
		this.partnerid=partnerid;
		getdata();
		
	}
	public boolean isOnlyOpen() {
		return onlyOpen;
	}
	public void setOnlyOpen(boolean onlyOpen) {
		this.onlyOpen = onlyOpen;
		orders = new ArrayList<Order>();
		getdata();
	}
	public int getTypedocis() {
		return typedocis;
	}

	public void setTypedocis(int typedocis) {
		this.typedocis = typedocis;
		orders = new ArrayList<Order>();
		getdata();
	}
	public void setPartnerId(int partnerid) {
		this.partnerid = partnerid;
		orders = new ArrayList<Order>();
		getdata();
	}

	public void getdata() {
		//dba = new DB(context);
		//dba.open();
		OrdersDao od = new OrdersDao(context);
		Cursor c = od.getOrders(onlyOpen,typedocis, fdate, ldate, partnerid);
		//startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				int id=c.getInt(c.getColumnIndex(Const.TABLE_ORDERS_ID));
				int client_id=c.getInt(c.getColumnIndex(Const.TABLE_ORDERS_CLIENT_ID)); 
				String client_name=c.getString(c.getColumnIndex(Const.TABLE_ORDERS_CLIENT_NAME));

				java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance();
				java.sql.Date docdate=java.sql.Date.valueOf(c.getString(c.getColumnIndex(Const.TABLE_ORDERS_DOCDATE)));
				 
				java.sql.Time doctime=java.sql.Time.valueOf("00:00:00"
						//c.getString(c.getColumnIndex(Const.TABLE_ORDERS_DOCTIME))
						); 
				int itemcount=c.getInt(c.getColumnIndex(Const.TABLE_ORDERS_ITEMCOUNT));
				Double mainsumm=c.getDouble(c.getColumnIndex(Const.TABLE_ORDERS_MAINSUMM));
				
				int  central_id=c.getInt(c.getColumnIndex("central_id"));
				int typedoc=c.getInt(c.getColumnIndex("typedoc"));
				int typepay=c.getInt(c.getColumnIndex("typepay"));
				String  prim=c.getString(c.getColumnIndex("prim"));
				int property=c.getInt(c.getColumnIndex("property"));

				 Order temp = new Order(id, client_id, client_name, docdate,doctime,itemcount, mainsumm,  central_id, prim, typedoc, typepay, property);
				 orders.add(temp);
			} while (c.moveToNext());

		}
		//dba.close();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orders.size();
	}

	@Override
	public Order getItem(int i) {
		// TODO Auto-generated method stub
		return orders.get(i);
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
			 v = mInflater.inflate(R.layout.orders_row, null);
			 holder = new ViewHolder();
			 holder.mOrdernumber = (TextView)v.findViewById(R.id.numorder);
			 holder.mOrderDate = (TextView)v.findViewById(R.id.orderdate);
			 holder.mOrderClientName = (TextView)v.findViewById(R.id.partnername);
			 holder.mOrderMainsumm = (TextView)v.findViewById(R.id.ordersum);
			 holder.mOrderCentralId = (TextView)v.findViewById(R.id.order_central_id);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
try {

		holder.morders = getItem(arg0);
		holder.mOrdernumber.setText(String.valueOf( holder.morders.getId()));
		holder.mOrderDate.setText(holder.morders.getDocdate().toString());
		holder.mOrderClientName.setText(holder.morders.getClient_name().toString());
		holder.mOrderMainsumm.setText(holder.morders.getMainsumm().toString());
		holder.mOrderCentralId.setText(String.valueOf(holder.morders.getCentral_id()));
} catch (Exception e) {
Toast.makeText(null, e.getMessage(), 10000);
// TODO: handle exception
}

		v.setTag(holder);

		// TODO Auto-generated method stub
		return v;
	}

	public class ViewHolder {
		Order morders;
		TextView mOrdernumber;
		TextView mOrderDate;
		TextView mOrderClientName;
		TextView mOrderMainsumm;
		TextView mOrderCentralId;
		
	}

	

}