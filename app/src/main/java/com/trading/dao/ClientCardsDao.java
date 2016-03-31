package com.trading.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AnalogClock;
import android.widget.Toast;

import com.trading.db.DB;
import com.trading.db.SpinnerDB;
import com.trading.utils.CC_card;
import com.trading.utils.CC_data;
import com.trading.utils.ClientCard;
import com.trading.utils.Const;
import com.trading.utils.Order;
import com.trading.utils.TOrder;
import com.trading.utils.Tovar;

public class ClientCardsDao {
	
	private DB dba;

	private SQLiteDatabase db;
	// private SQLiteDatabase db;
	private final Context context;
	
	public ClientCardsDao(Context c) {
		context = c;
		dba = new DB(c);
	}
	public void delAllCards()
	{
		if (db == null) db= DB.getDB(context);
		try {
			db.execSQL("delete from cc_card");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public int createNewClientCard(int partnerid)
	{
		if (db == null) db= DB.getDB(context);
		int newclientcardid=0;
		int oldcardid;
		try {
			db.beginTransaction();
			
			Cursor c = db.rawQuery("select max(_id) from cc_card",
					null);	
			c.moveToFirst();
			int newnumber;
			if (c.isNull(0)) {
				newnumber = 1;
		} else
			
			newnumber =c.getInt(0) + 1;
			
			
			c = db.rawQuery("select _id from cc_card where client_id=? order by dt desc, _id desc",
					new String[]{String.valueOf(partnerid)});
			c.moveToFirst();
			if (!c.isNull(0))
			{
			oldcardid=c.getInt(0);
			
			
			db.execSQL("insert into cc_card(_id, client_id, card_id) select ?, client_id, card_id from cc_card where _id=? ", 
					new String[]{String.valueOf(newnumber), String.valueOf(oldcardid)});
			db.execSQL("insert into cc_data(position_id, card_id, ost) select position_id, ?, ost from cc_data where card_id=? ",
					new String[]{String.valueOf(newnumber), String.valueOf(oldcardid)});
			newclientcardid=newnumber;
			db.setTransactionSuccessful();
			}
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			// TODO: handle exception
		} 
		
		
		
		return newclientcardid;
	}
	public int createOrderFromCard(int cardid)
	{
		OrdersDao od= new OrdersDao(context);
		Order order=new Order();
		CC_card cc_card=getCC_card(cardid, false, 0, true);
		order.setClient_id(cc_card.getClient_id());
		order.setClient_name(cc_card.getClient_name());
		order.setDocdate(cc_card.getDt());
		order.setDocstate(Const.DOC_STATE_NEW);
		order.setIschanged(true);
		order.setProperty(1);
		order.setTypedoc(1);
		order.setTypepay(2);
		
		TOrder torder=new TOrder();
		for (int i=0;i<cc_card.getCc_data().size();i++)
		{
			torder=new TOrder();
			torder.setTovid(cc_card.getCc_data().get(i).getTovar_id());
			torder.setTovname(cc_card.getCc_data().get(i).getTovname());
			torder.setTovkolvo(cc_card.getCc_data().get(i).getZakaz());
			
			double cenands;
			HashMap<String, Double> hm = new HashMap<String, Double>();
			TovarsDao td = new TovarsDao(context);
			Tovar selectedtovar = td.getTovar(torder.getTovid());
			cenands = 0;
			hm = td.getLastCena2(order.getClient_id(), selectedtovar.getId(), order.getTypedoc());
				cenands = hm.get("newcena");
			if (cenands == 0)
				cenands = selectedtovar.getCenands();
			torder.setTovcenands(cenands);
			torder.setToved_izm(selectedtovar.getEd_izm());
			if (torder.getTovkolvo()>0)
			order.addTovar(torder);
		}
		
		if (order.getItemcount()>0)
		order.setId(od.SaveOrder(order));
		else
		order.setId(0);
		
		return order.getId();
		
	}
	
	public int getNextNumb(SQLiteDatabase db) {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DAY_OF_MONTH, 0);
		Date sqlDate = new Date(cl.getTime().getTime());

		Cursor c = db.rawQuery("select max(_id) from cc_card",
				null);
		c.moveToFirst();
		if (c.isNull(0)) {
			return 1;
		} else
			return c.getInt(0) + 1;
	}
	public int getNextNumb() {
		return getNextNumb(DB.getDB(context));
}
	public CC_card getCC_card(int lid, boolean isall, int grid, boolean isallcat)
	{
		int isall_i;
		if (isall)
			isall_i=1;
		else
			isall_i=0;
		
		
		
		CC_card ccCard=new CC_card();
		CC_data ccData=new CC_data();
		
		if (lid > 0) {
			Cursor 	curCC_card= DB.getDB(context).rawQuery(
					"select ccc.*, P.name AS clientname, CC.NAME AS clientcardname, p.cat, ccc.prim from cc_card ccc "+
					"left join partner p on p._id= ccc.client_id "+
					"left join clientcard cc on cc._id= ccc.card_id "+

					"where ccc._id=? ", 
					new String[]{String.valueOf(lid)});

			curCC_card.moveToFirst();
			ccCard.setId(lid);
			ccCard.setClient_id(curCC_card.getInt(curCC_card.getColumnIndex("client_id")));
			ccCard.setClient_name(curCC_card.getString(curCC_card.getColumnIndex("clientname")));
			ccCard.setClientcard_id(curCC_card.getInt(curCC_card.getColumnIndex("card_id")));
			ccCard.setClientcard_name(curCC_card.getString(curCC_card.getColumnIndex("clientcardname")));
			ccCard.setPrim(curCC_card.getString(curCC_card.getColumnIndex("prim")));
			ccCard.setCentral_id(curCC_card.getInt(curCC_card.getColumnIndex("central_id")));
			
			try {
				ccCard.setDt(java.sql.Date.valueOf(curCC_card.getString(curCC_card.getColumnIndex("dt"))));	
			} catch (Exception e) {
				Calendar calendar = Calendar.getInstance();
				ccCard.setDt(new java.sql.Date(calendar.getTimeInMillis()));
			}
			
			ccCard.setClient_cat(curCC_card.getString(curCC_card.getColumnIndex("cat")));
			Cursor curCC_data = DB.getDB(context).rawQuery(
					"select ccd._id , ccd.ost, ccd.card_id, ccd.zakaz , cct._id as position_id, CCT.tovar_id , S.name as tovname, "+
					"cct.cat_a,cct.cat_b,cct.cat_c "+
					"from cc_tovari cct "+
					"left join  cc_data ccd  on cct._id=ccd.position_id and ccd.card_id=?"+ 
					"left join sklad s on s._id = cct.tovar_id  "+
					"left join grsklad gs on gs._id=s.parentid "+
					"where (((ccd.card_id is not null) or (1="+String.valueOf(isall_i)+")) and ((s.parentid=?)or(0="+
					String.valueOf(grid)+"))) order by gs.name desc, S.name desc", 
					new String[]{String.valueOf(lid),
						String.valueOf(grid)} );
			curCC_data.moveToFirst();
			if (curCC_data.moveToFirst()) {
				do {
					ccData=new CC_data();
					ccData.setCard_id(lid);
					ccData.setPosition_id(curCC_data.getInt(curCC_data.getColumnIndex("position_id")));
					ccData.setOst(curCC_data.getDouble(curCC_data.getColumnIndex("ost")));
					ccData.setZakaz(curCC_data.getDouble(curCC_data.getColumnIndex("zakaz")));
					ccData.setTovname(curCC_data.getString(curCC_data.getColumnIndex("tovname")));
					ccData.setTovar_id(curCC_data.getInt(curCC_data.getColumnIndex("tovar_id")));
					Double avail;
					
					if (ccCard.getClient_cat().equalsIgnoreCase("A"))
					{
						avail=curCC_data.getDouble(curCC_data.getColumnIndex("cat_a"));
					}else
					if (ccCard.getClient_cat().equalsIgnoreCase("B"))
					{
						avail=curCC_data.getDouble(curCC_data.getColumnIndex("cat_b"));
					}else
					if (ccCard.getClient_cat().equalsIgnoreCase("C"))
					{
						avail=curCC_data.getDouble(curCC_data.getColumnIndex("cat_c"));
					}else
						avail=(double)0;
						ccData.setAvail(avail);					
					
					if (isallcat)	
					ccCard.Add_cc_data(ccData);
					else
						if (avail>0.0)
							ccCard.Add_cc_data(ccData);
				
				} while (curCC_data.moveToNext());
			}
			curCC_card.close();
			curCC_data.close();
		} else {
			ccCard.setId(getNextNumb());
			Calendar calendar = Calendar.getInstance();
			ccCard.setDt(new java.sql.Date(calendar.getTimeInMillis()));
			ccCard.setDocstate(Const.DOC_STATE_NEW);
			ccCard.setIschanged(true);
		}
		
		
		
		
		return ccCard;
	}
	public Cursor get_cards(int client_id)
	{
		
		Cursor curCC_card = DB.getDB(context).rawQuery(
				
				"select ccc._id as _id, ccc.*, p.name as clientname, cc.name as clientcardname "+ 
				"from cc_card ccc "+
				"left join partner p on p._id= ccc.client_id "+
				"left join clientcard cc on cc._id= ccc.card_id "+
				"where ccc.client_id=?", new String[]{String.valueOf(client_id)});
		return curCC_card;
		
	}
	public Cursor get_cards(java.sql.Date selecteddate)
	{
		Cursor curCC_card = DB.getDB(context).rawQuery(
				
				"select ccc._id as _id, ccc.*, p.name as clientname, cc.name as clientcardname "+ 
				"from cc_card ccc "+
				"left join partner p on p._id= ccc.client_id "+
				"left join clientcard cc on cc._id= ccc.card_id "+
				"where ccc.dt=?", new String[]{String.valueOf(selecteddate.toString())});
		return curCC_card;
		
	}
	public void add_CC_data(CC_data cc_data) throws Exception
	{
		try {
			
		
		DB.getDB(context).execSQL("insert or replace  into cc_data(position_id,card_id, ost, zakaz ) "+
				"values(?,?,?,?)", new String[]{String.valueOf(cc_data.getPosition_id()),
				String.valueOf(cc_data.getCard_id()),
				String.valueOf(cc_data.getOst()),
				String.valueOf(cc_data.getZakaz())
				});
		
		if ((cc_data.getOst()==0)&&(cc_data.getZakaz()==0 ))
		{
			DB.getDB(context).execSQL("delete from cc_data where position_id=? and card_id=?",
			new String[]{String.valueOf(cc_data.getPosition_id()),
			String.valueOf(cc_data.getCard_id())});
			
		}
		} catch (Exception e) {
			throw new Exception("Не могу изменить карту клиента!!") ;
		}
	}
	/*
	public void add_CC_data(CC_data cc_data)
	{
		dba.getDB(context).execSQL("delete from cc_data where position_id=? and card_id=?", 
				new String[]{String.valueOf(cc_data.getPosition_id()),
				String.valueOf(cc_data.getCard_id())});
		if ((cc_data.getOst()>0)||(cc_data.getZakaz()>0 ))
		{
		dba.getDB(context).execSQL("insert into cc_data(position_id,card_id, ost, zakaz ) "+
				"values(?,?,?,?)", new String[]{String.valueOf(cc_data.getPosition_id()),
				String.valueOf(cc_data.getCard_id()),
				String.valueOf(cc_data.getOst()),
				String.valueOf(cc_data.getZakaz())
				});
		}
	}
// */	
	public int SaveCC_card(CC_card cc_card) throws Exception {
		int nn = 0;
		try {
			if (cc_card.getId() == 0) {
				nn = getNextNumb();

				DB.getDB(context).execSQL(
						"insert into cc_card(_id, dt, client_id, card_id,central_id, prim)"
								+ "values(?,?,?,?,?,?)",
						new String[] { String.valueOf(nn),
								cc_card.getDt().toString(),
								String.valueOf(cc_card.getClient_id()),
								String.valueOf(cc_card.getClientcard_id()), 
								String.valueOf(cc_card.getCentral_id()),
								""// String.valueOf(cc_card.getPrim())
						});

				for (int i = 0; i < cc_card.getCc_data().size(); i++) {
					
					cc_card.getCc_data().get(i).setCard_id(nn);
					add_CC_data(cc_card.getCc_data().get(i));
				}
			} else {
				DB.getDB(context).execSQL(
								"update cc_card set prim=?, card_id=?, central_id=? where _id=?",
								new String[] {
										cc_card.getPrim(),
										String.valueOf(cc_card
												.getClientcard_id()),
										String.valueOf(cc_card.getCentral_id()),
										String.valueOf(cc_card.getId()) });
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage()) ;
		}
		return nn;

	}
	public void SaveCC_cardTemplate(ClientCard clientcard)
	{
		boolean ispresent=false;
		Cursor c = DB.getDB(context).rawQuery("select count(_id) from clientcard where _id=?",new String[]{String.valueOf(clientcard.getId())});
		c.moveToFirst();
		ispresent = c.getInt(0) > 0;
		if (ispresent)
		{
			DB.getDB(context).execSQL("update clientcard set name = ?", new String[]{clientcard.getName()});
			for (int i=0; i<clientcard.getTovarlist().size();i++)
			{
				c = DB.getDB(context).rawQuery("select count(_id) from cc_tovari where card_id=? and tovar_id=?",
						new String[]{String.valueOf(clientcard.getId()), String.valueOf(clientcard.getTovarlist().get(i).getTovar_id())});
				c.moveToFirst();
				if (c.getInt(0)>0)
					DB.getDB(context).execSQL("update cc_tovari set _id=?, cat_a = ?, cat_b=?, cat_c=? where card_id=? and tovar_id=?",
							new String[]{
							String.valueOf(clientcard.getTovarlist().get(i).getId()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_a()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_b()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_c()),
							String.valueOf(clientcard.getId()), String.valueOf(clientcard.getTovarlist().get(i).getTovar_id())});
				else
					try {
						
					 
					DB.getDB(context).execSQL("insert into cc_tovari(_id, card_id, tovar_id,cat_a, cat_b, cat_c) values(?,?,?,?,?,?)",
							new String[]{
							String.valueOf(clientcard.getTovarlist().get(i).getId()),
							String.valueOf(clientcard.getId()), 
							String.valueOf(clientcard.getTovarlist().get(i).getTovar_id()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_a()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_b()),
							String.valueOf(clientcard.getTovarlist().get(i).getCat_c())});
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			
		}
		else
		{
			DB.getDB(context).execSQL("insert into clientcard(_id, name) values(?,?)", new String[]{String.valueOf(clientcard.getId()), clientcard.getName()});
			for (int i=0; i<clientcard.getTovarlist().size();i++)
			{
				DB.getDB(context).execSQL("insert into cc_tovari('_id, card_id, tovar_id,cat_a, cat_b, cat_c) values(?,?,?,?,?)",
						new String[]{
						String.valueOf(clientcard.getId()), 
						String.valueOf(clientcard.getTovarlist().get(i).getTovar_id()),
						String.valueOf(clientcard.getTovarlist().get(i).getCat_a()),
						String.valueOf(clientcard.getTovarlist().get(i).getCat_b()),
						String.valueOf(clientcard.getTovarlist().get(i).getCat_c())});
			}
		}
		
	}
	public Cursor getClientCards() {

		Cursor c = DB.getDB(context).query("clientcard", null, null,
				null, null, null, null);
		// Cursor c = dba.getDB(context).query(Const.TABLE_PROPS, null, null,
		// null, null, null, null);
		// c.deactivate();
		return c;
	}
	public void checkEmpty(CC_card cc_card)
	{
		cc_card = getCC_card(cc_card.getId(), false,0 ,true);
		for (int i=cc_card.getCc_data().size()-1; i>=0;i--)
		{
			if ((cc_card.getCc_data().get(i).getOst()==0) &&(cc_card.getCc_data().get(i).getZakaz()==0))
				cc_card.getCc_data().remove(i);
		}
		if (cc_card.getCc_data().size()==0)
		{
			try {
				DB.getDB(context).execSQL("PRAGMA foreign_keys = OFF;");

				DB.getDB(context).execSQL("delete from cc_card where _id=?", new String[]{String.valueOf(cc_card.getId())});
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
}
