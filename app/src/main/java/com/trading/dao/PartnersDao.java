package com.trading.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.trading.db.DB;
import com.trading.utils.Const;
import com.trading.utils.Partner;
import com.trading.utils.Tovar;

public class PartnersDao {
private DB dba;
	
	//private SQLiteDatabase db;
	private final Context context;
	//private final DBHelper dbhelper;

	public PartnersDao(Context c) {
		context = c;
		dba= new DB(c);
		
		//dba.getDB(context);
	}
	
	public Cursor getPartners(int week_day)
	{
		Cursor c =  DB.getDB(context).query(Const.TABLE_PARTNERS,  null, "(week_day="+String.valueOf(week_day)+") or (0="+String.valueOf(week_day)+")", null, null, null, "name");
		//Cursor c =  dba.getDB(context).query(Const.TABLE_PROPS, null, null, null, null, null, null);
		//c.deactivate();
		return c;
	}
	public Cursor getPartners(String name, int week_day)
	{
		Cursor c =  DB.getDB(context).query(Const.TABLE_PARTNERS, null, "name like "+ "'%" + name + "%' and ((week_day="+String.valueOf(week_day)+") or (0="+String.valueOf(week_day)+"))", null, null, null, "name");
		//Cursor c =  dba.getDB(context).query(Const.TABLE_PROPS, null, null, null, null, null, null);
		//c.deactivate();
		return c;
	}
	public Cursor getPartners(String name, String notnullfieldname,int week_day)
	{
		Cursor c =  DB.getDB(context).query(Const.TABLE_PARTNERS, null, "(name like "+ "'%" + name + "%') and (("+notnullfieldname+" <>''))  and ((week_day="+String.valueOf(week_day)+") or (0="+String.valueOf(week_day)+"))", null, null, null, "name");
		//Cursor c =  dba.getDB(context).query(Const.TABLE_PROPS, null, null, null, null, null, null);
		//c.deactivate();
		return c;
	}
	public void clearPartnersWeekDay()
	{
		DB.getDB(context).execSQL("update partner set week_day=?",
			     new Object[]{null});
		
	}
	public void setInsertUpdatePartners(ArrayList<Partner> partnerlist)	{
	Partner p;
	
     for (int i=0;i<partnerlist.size();i++)
     {
     p=partnerlist.get(i);
     if (DB.isExist(p.id, Const.TABLE_PARTNERS))
     {
     DB.getDB(context).execSQL("update partner set name=?,address=?,phone=?, daysdelay=?,debtsumm1=?, idskidka=?, cat=?, idhenkel=?, week_day=? where _id=?",
     new Object[]{p.name, p.address, p.phone, p.daysdelay, p.debtsumm1, p.idskidka,p.category,p.idhenkel,p.week_day,  p.id});
     }
     else
     {
     DB.getDB(context).execSQL("insert into partner(_id,name,address,phone, daysdelay,debtsumm1, idskidka, cat, idhenkel, week_day) values(?,?,?,?,?,?,?,?,?,?)",
     new Object[]{p.id, p.name, p.address, p.phone, p.daysdelay, p.debtsumm1, p.idskidka, p.category, p.idhenkel, p.week_day});
     }
      
      
     }	
	
	}
}
