package com.trading.db;

import java.sql.Date;

import com.trading.utils.Const;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class DB {
	private static SQLiteDatabase db;
	private static Context context;
	private static DBHelper dbhelper;
	public DB(Context c){
		context = c;
		dbhelper = new DBHelper(context);
	}
	public void close()
	{
		db.close();
	}
	public static SQLiteDatabase getDB(Context c)
	{
		context = c;
		dbhelper = new DBHelper(context);
		open();
		return db;
	}
	public static void open() throws SQLiteException
	{

		try{
			db = dbhelper.getWritableDatabase();
		}catch(Exception ex)
		{
			Log.v("open exception caught",ex.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}
/*	public Cursor getOrders(boolean onlyopen)
	{
		Cursor c;
		if (onlyopen)
			c =  db.query(Const.TABLE_ORDERS, null, null, null, null, null, null);
		else
		 c=  db.query(Const.TABLE_ORDERS, null, "central_id<=0", null, null, null, null);
		
		//c.deactivate();
		return c;
	}
*/
	/*
	public Cursor getOrders(boolean onlyopen,int typedoc, Date fdate, Date ldate, int partnerid)
	{
		Cursor c;
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
		if (onlyopen)
			c =  db.query(Const.TABLE_ORDERS, null, 
					"((typedoc=?)or (typedoc=?)) and (docdate between ? and ?) and ((clientid=?) or (0="+String.valueOf(partnerid)+"))", 
					new String[]{String.valueOf(typedoc),String.valueOf(typed),fdate.toString(), ldate.toString(), String.valueOf(partnerid)}, null, null, null);
		else
		 c=  db.query(Const.TABLE_ORDERS, null, "((typedoc=?)or (typedoc=?)) and (docdate between ? and ?) and central_id<=0 and ((clientid=?) or (0="+String.valueOf(partnerid)+"))", new String[]{String.valueOf(typedoc),String.valueOf(typed), fdate.toString(), ldate.toString(),
			 String.valueOf(partnerid)}, null, null, null);
		
		//c.deactivate();
		return c;
	}
	*/
	
	public static boolean isExist(int id, String tablename) {
		Cursor c;
		try {
			open();
			//c = db.query(tablename, null,"_id=" + String.valueOf(id), null, null, null, null);
			c =  db.rawQuery("select _id from "+tablename+ " where _id="+ String.valueOf(id),null);
				
		} catch (Exception e) {
           e.setStackTrace(e.getStackTrace());
           return false;
		} 
		int x=c.getCount();
		db.close();
		return x != 0;
	}

public static void ClearDatabase() {
		
		db.execSQL("delete from cc_data");
		db.execSQL("delete from cc_card");
		db.execSQL("delete from cc_tovari");
		db.execSQL("delete from clientcard");
		db.execSQL("delete from torders");
		db.execSQL("delete from orders");
		db.execSQL("delete from partner");
		db.execSQL("delete from sklad");
		
		
	}
}
