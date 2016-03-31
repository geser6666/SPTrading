package com.trading.dao;

import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.widget.Toast;

import com.trading.R.string;
import com.trading.db.DB;
import com.trading.db.DBHelper;
import com.trading.utils.Const;
import com.trading.utils.Order;
import com.trading.utils.TOrder;

public class OrdersDao {
	private DB dba;

	private SQLiteDatabase db;
	// private SQLiteDatabase db;
	private final Context context;

	// private final DBHelper dbhelper;

	public OrdersDao(Context c) {
		context = c;
		dba = new DB(c);
	//	db=dba.getDB(context);

		// dba.getDB(context);
	}

	public int getNextNumb() {
			return getNextNumb(DB.getDB(context));
	}
	public int getNextNumb(SQLiteDatabase db) {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DAY_OF_MONTH, 0);
		Date sqlDate = new Date(cl.getTime().getTime());
		
		Cursor c = db.rawQuery("select max(_id) from orders",
				null);
		c.moveToFirst();
		if (c.isNull(0)) {
			return 1;
		} else
			return c.getInt(0) + 1;
	}

	public Cursor getFirms() {

		Cursor c = DB.getDB(context).query(Const.TABLE_PROPS, null, null,
				null, null, null, null);
		// Cursor c = dba.getDB(context).query(Const.TABLE_PROPS, null, null,
		// null, null, null, null);
		// c.deactivate();
		return c;
	}

	public Cursor getTovars(int grid) {

		Cursor c = DB.getDB(context).query(Const.TABLE_TORDERS, null,
				"grid=" + String.valueOf(grid), null, null, null, null);

		return c;
	}

	public Order getOrder(int lid) {
		Order or = new Order();
		if (lid > 0) {
			Cursor curOrder = DB.getDB(context).query(Const.TABLE_ORDERS,
					null, "_id=" + String.valueOf(lid), null, null, null, null);
					Cursor curTOrder = DB.getDB(context).query(Const.TABLE_TORDERS, null,
							"grid=" + String.valueOf(lid), null, null, null,
							null);
			curOrder.moveToFirst();
			
			
			//or.setId(curOrder.getInt(curOrder.getColumnIndex("_id")));
			//or.setDocdate(java.sql.Date.valueOf(curOrder.getString(curOrder.getColumnIndex("docdate"))));
			//or.setDoctime(java.sql.Time.valueOf(curOrder.getString(curOrder.getColumnIndex("doctime"))));
			//or.setClient_id(curOrder.getInt(curOrder.getColumnIndex("clientid")));
			//or.setClient_name(curOrder.getString(curOrder.getColumnIndex("clientname")));
			//or.setDocstate(Const.DOC_STATE_EDITED);
			//or.setCentral_id(curOrder.getInt(curOrder.getColumnIndex("central_id")));
			//or.setPrim(curOrder.getString(curOrder.getColumnIndex("prim")));
			//or.setTypedoc(curOrder.getInt(curOrder.getColumnIndex("typedoc")));
			//or.setTypepay(curOrder.getInt(curOrder.getColumnIndex("typepay")));
			//or.setProperty(curOrder.getInt(curOrder.getColumnIndex("property")));
			
			or= new Order(curOrder.getInt(curOrder.getColumnIndex("_id")), 
					curOrder.getInt(curOrder.getColumnIndex("clientid")),
					curOrder.getString(curOrder.getColumnIndex("clientname")), 
					java.sql.Date.valueOf(curOrder.getString(curOrder.getColumnIndex("docdate"))), 
					java.sql.Time.valueOf(curOrder.getString(curOrder.getColumnIndex("doctime"))), 
					0, 0.0, 
					curOrder.getInt(curOrder.getColumnIndex("central_id")), 
					curOrder.getString(curOrder.getColumnIndex("prim")), 
					curOrder.getInt(curOrder.getColumnIndex("typedoc")), 
					curOrder.getInt(curOrder.getColumnIndex("typepay")), 
					curOrder.getInt(curOrder.getColumnIndex("property")));
			or.setDocstate(Const.DOC_STATE_EDITED);
			
			if (curTOrder.moveToFirst()) {
				do {

					TOrder temp = new TOrder();
					temp.setGrid(curOrder.getInt(curOrder.getColumnIndex("_id")));
					temp.setTovid(curTOrder.getInt(curTOrder.getColumnIndex("tovid")));
					temp.setTovname(curTOrder.getString(curTOrder.getColumnIndex("tovname")));
					temp.setToved_izm(curTOrder.getString(curTOrder.getColumnIndex("ed_izm")));
					temp.setTovkolvo(curTOrder.getDouble(curTOrder.getColumnIndex("kolvo")));
					temp.setTovcenands(curTOrder.getDouble(curTOrder.getColumnIndex("cena")));

					or.addTovar(temp);
				} while (curTOrder.moveToNext());
				or.setIschanged(false);
			}
		} else {
			or.setId(getNextNumb());
			Calendar calendar = Calendar.getInstance();
			or.setDocdate(new java.sql.Date(calendar.getTimeInMillis()));
			or.setDoctime(new java.sql.Time(calendar.getTimeInMillis()));
			or.setProperty(1);
			or.setDocstate(Const.DOC_STATE_NEW);

			// or.setDocdate( )
		}
		or.setIschanged(false);
		return or;

	}
	public void DelOrders() {
		int retval=0;
		SQLiteDatabase db;
		db= DB.getDB(context);
		Calendar calendar = Calendar.getInstance();
		java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
		db.execSQL("PRAGMA foreign_keys = OFF;");
		db.execSQL("update orders set central_id=0 where central_id>0 and (docdate<? or typedoc=1)", new String[]{sqlDate.toString()});
		db.execSQL("delete from orders where central_id=0 and (docdate<? or typedoc=1)", new String[]{sqlDate.toString()}); 
		db.execSQL("PRAGMA foreign_keys = ON;");				
		db.close();
				
	}
	public void DelOrder(int orderid) {
		int retval=0;
		SQLiteDatabase db;
		db= DB.getDB(context);
		db.execSQL("DROP TRIGGER bd_orders;");
		db.execSQL("CREATE TRIGGER bd_orders BEFORE DELETE ON orders BEGIN select case when old.central_id>0 then RAISE (ABORT, 'NOT DELETE!!!') end; delete from torders where grid = old._id; END");
		try {
			
		
		db.execSQL("delete from orders where _id=?", new String[]{String.valueOf(orderid)});
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), 10000).show(); 
		}
		db.close();
				
	}
	public int SaveOrder(Order ordr) {
		int retval=0;
		int nn=0;
		SQLiteDatabase db;
		db= DB.getDB(context);
		db.beginTransaction();
		try {
			if (ordr.getDocstate() == Const.DOC_STATE_NEW) {
				db.execSQL("PRAGMA foreign_keys = OFF;");
				nn = getNextNumb(db);
				db.execSQL("insert into orders(_id, clientid,clientname,docdate,doctime, "
										+ "itemcount,mainsumm,paymentdate,typepay, "
										+ "central_id,property, prim, typedoc) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new String[] { String.valueOf(nn),
										String.valueOf(ordr.getClient_id()),
										ordr.getClient_name(),
										ordr.getDocdate().toString(),
										ordr.getDoctime().toString(),
										String.valueOf(ordr.getItemcount()),
										String.valueOf(ordr.getMainsumm()),
										null, 
										String.valueOf(ordr.getTypepay()),
										String.valueOf(ordr.getCentral_id()),
										String.valueOf(ordr.getProperty()),
										String.valueOf(ordr.getPrim()),
										String.valueOf(ordr.getTypedoc())
										});
				// grid, tovid, tovname, ed_izm, kolvo, cena
				for (int i = 0; i < ordr.getTovarlist().size(); i++) {
					String[] ss=new String[] {
							String.valueOf(nn),
							String.valueOf(ordr.getTovarlist()
									.get(i).getTovid()),
							ordr.getTovarlist().get(i).getTovname(),
							ordr.getTovarlist().get(i).getToved_izm(),
							String.valueOf(ordr.getTovarlist()
									.get(i).getTovkolvo()),
							String.valueOf(ordr.getTovarlist()
									.get(i).getTovcenands()) };
					db.execSQL("insert into torders(grid, tovid, tovname, ed_izm, kolvo, cena) "
											+ "values(?,?,?,?,?,?)",
									ss);
				}
				retval=nn;
			} else {
				db
						.execSQL(
								"update orders "
										+ "set clientid=?, clientname=?, "
										+ "itemcount=?,mainsumm=?,paymentdate=?,typepay=?, "
										+ "central_id=?, prim=?, typedoc=?, property=? where _id=?",
								new String[] {
										String.valueOf(ordr.getClient_id()),
										ordr.getClient_name(),
										String.valueOf(ordr.getItemcount()),
										String.valueOf(ordr.getMainsumm()),
										null, 
										String.valueOf(ordr.getTypepay()),
										String.valueOf(ordr.getCentral_id()),
										String.valueOf(ordr.getPrim()),										
										String.valueOf(ordr.getTypedoc()),
										String.valueOf(ordr.getProperty()),
										String.valueOf(ordr.getId())});

				db.execSQL("delete from torders where grid=?",
						new String[] { String.valueOf(ordr.getId()) });
				for (int i = 0; i < ordr.getTovarlist().size(); i++) {

					if (ordr.getTovarlist().get(i).getTovkolvo()>0)
					{
					
					db.execSQL(
									"insert into torders(grid, tovid, tovname, ed_izm, kolvo, cena) "
											+ "values(?,?,?,?,?,?)",
											new String[] {
													String.valueOf(ordr.getId()),
													String.valueOf(ordr.getTovarlist()
															.get(i).getTovid()),
													ordr.getTovarlist().get(i).getTovname(),
													ordr.getTovarlist().get(i).getToved_izm(),
													String.valueOf(ordr.getTovarlist()
															.get(i).getTovkolvo()),
													String.valueOf(ordr.getTovarlist()
															.get(i).getTovcenands()) });
						//по-старому было так, остатки на планшете изменялись только когда продажа отправлена
/*
						if ((ordr.getCentral_id()>0 ) &&(ordr.getTypedoc()==2))
					{
						db.execSQL(
								"update sklad set ost=ost-? where _id=?",
										new String[] {
										String.valueOf(ordr.getTovarlist()
												.get(i).getTovkolvo()),
												String.valueOf(ordr.getTovarlist()
														.get(i).getTovid())
												
												 });
						
					}*/
					}

				}
				retval=ordr.getId();
			}
			
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			String s = e.getMessage();
			Toast.makeText(context, e.getMessage()+String.valueOf(nn), 10000).show();
			
		} finally {
			ordr.setIschanged(false);
			

		}
		return retval;
		
	}
	
	
	public Cursor getOrders(boolean onlyopen,int typedoc, Date fdate, Date ldate, int partnerid)
	{
		Cursor c=null;
		int typed=0;
		switch (typedoc) {
		case 1:
			typed=1;
			break;
		case 2:
			typed=3;
			break;
		default:
			break;
		}
		try {
			
		
		if (onlyopen)
			c =  DB.getDB(context).query(Const.TABLE_ORDERS, null,
					"((typedoc=?)or (typedoc=?)) and (docdate between ? and ?) and ((clientid=?) or (0="+String.valueOf(partnerid)+"))", 
					new String[]{String.valueOf(typedoc),String.valueOf(typed),fdate.toString(), ldate.toString(), String.valueOf(partnerid)}, null, null, null);
		else
		{
			String s="((typedoc=?)or (typedoc=?)) and (docdate between ? and ?) and (central_id<=0) and ((clientid=?) or (0="+String.valueOf(partnerid)+"))";
		 c=  DB.getDB(context).query(Const.TABLE_ORDERS, null, "((typedoc=?)or (typedoc=?)) and (docdate between ? and ?) and (central_id<=0) and ((clientid=?) or (0="+String.valueOf(partnerid)+"))", new String[]{String.valueOf(typedoc),String.valueOf(typed), fdate.toString(), ldate.toString(),
			 String.valueOf(partnerid)}, null, null, null);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//c.deactivate();
		return c;
	}
}
