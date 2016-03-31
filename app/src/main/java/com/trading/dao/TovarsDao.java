package com.trading.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.trading.db.DB;
import com.trading.utils.Const;
import com.trading.utils.Grsklad;
import com.trading.utils.Order;
import com.trading.utils.Partner;
import com.trading.utils.Tovar;

import android.content.Context;
import android.database.Cursor;

public class TovarsDao {
	private DB dba;

	// private SQLiteDatabase db;
	private final Context context;

	// private final DBHelper dbhelper;
	public TovarsDao(Context c) {
		context = c;
		dba = new DB(c);

		// dba.getDB(context);
	}
    public double getLastCena(int idpartner, int idtovar, int typedoc)
    {
    	double ret;
    	Cursor c = DB.getDB(context).rawQuery("select so.cena,  round(so.cena/so.cenaprice*s.cenands,2) as newcena "+
				"from torders so "+
				"left join orders n on n._id=so.grid "+
				"left join sklad s on s._id= so.tovid "+
				

				"where so.tovid=? "+
				"and n.clientid=? "+
				"and n.typedoc=? "+
				"and n.central_id>0 "+
				"order by n.docdate desc "+
				"limit 1 ", new String[]{String.valueOf(idtovar), 
    			String.valueOf(idpartner), 
    			String.valueOf(typedoc)});
    	c.moveToFirst();
    	if (c.getCount()==0)
    		ret= 0.0;
    		else ret= c.getDouble(1);
    	dba.close();
    	
    	return ret;							
    	
    }
    public HashMap<String, Double> getLastCena2(int idpartner, int idtovar, int typedoc)
    {
    	HashMap<String, Double> ret=new HashMap<String, Double>();
    	
    	Cursor c = DB.getDB(context).rawQuery("select so.cena,  round(so.cena/so.cenaprice*s.cenands,2) as newcena "+
				"from torders so "+
				"left join orders n on n._id=so.grid "+
				"left join sklad s on s._id= so.tovid "+
				

				"where so.tovid=? "+
				"and n.clientid=? "+
				"and n.typedoc=? "+
				"and n.central_id>0 "+
				"order by n.docdate desc "+
				"limit 1 ", new String[]{String.valueOf(idtovar), 
    			String.valueOf(idpartner), 
    			String.valueOf(typedoc)});
    	c.moveToFirst();
    	if (c.getCount()==0)
    	{
    		ret.put("oldcena", 0.0);
    		ret.put("newcena", 0.0);
    	}
    		else 
    			{
        		ret.put("oldcena", c.getDouble(0));
        		ret.put("newcena", c.getDouble(1));
    			}
    	dba.close();
    	
    	return ret;							
    	
    }
    public Tovar getTovar(int lid)
    {
    	Tovar ret=new Tovar();
		if (lid > 0) {
			Cursor curTovar = DB.getDB(context).query(Const.TABLE_SKLAD,
					null, "_id=" + String.valueOf(lid), null, null, null, null);
			curTovar.moveToFirst();
			        ret = new Tovar(curTovar.getInt(curTovar.getColumnIndex("_id")), 
            curTovar.getString(curTovar.getColumnIndex("name")), 
            curTovar.getString(curTovar.getColumnIndex("ed_izm")), 
            curTovar.getDouble(curTovar.getColumnIndex("cenands")), 
            curTovar.getDouble(curTovar.getColumnIndex("available")), 
            curTovar.getDouble(curTovar.getColumnIndex("ost")), 
            curTovar.getInt(curTovar.getColumnIndex("parentid")),
            curTovar.getDouble(curTovar.getColumnIndex("skidka1")),
            curTovar.getDouble(curTovar.getColumnIndex("skidka2")),
            curTovar.getDouble(curTovar.getColumnIndex("skidka3")),
            curTovar.getDouble(curTovar.getColumnIndex("seb")));

			dba.close();
		}
    	return ret;
    }
	public Cursor getTovars(int idgr,int typeview) {
		String filter="";
		switch (typeview) {
		case 1:
			filter="parentid=? and available>0";
			break;
		case 2:
			filter="parentid=? and ost>0";
			break;
		case 3:
			filter="parentid=? and (available>0)";
			break;
		default:
			filter="parentid=?";
			break;
		} 
		Cursor c = DB.getDB(context).query(Const.TABLE_SKLAD, null,
				filter,
				new String[]{String.valueOf(idgr)}, 
				null, null, "name");
		// Cursor c = dba.getDB(context).query(Const.TABLE_PROPS, null, null,
		// null, null, null, null);
		// c.deactivate();
		
		return c;
	}

	
	public void setInsertUpdateGroups(ArrayList<Grsklad> grouplist) {
		Grsklad p;

		for (int i = 0; i < grouplist.size() ; i++) {
			p = grouplist.get(i);
			if (DB.isExist(p.id, Const.TABLE_GRSKLAD)) {
				DB.getDB(context).execSQL("update grsklad set name=? where _id=? ",
						new Object[] { p.name, p.id });
			} else {
				DB.getDB(context).execSQL(
						"insert into grsklad(_id,name) values(?,?)",
						new Object[] { p.id, p.name });
			}

		}
		dba.close();

	}

	public void setInsertUpdateTovars(ArrayList<Tovar> tovarlist) {
		Tovar p;

		for (int i = 0; i < tovarlist.size(); i++) {
			p = tovarlist.get(i);
			if (DB.isExist(p.getId(), Const.TABLE_SKLAD)) {
				DB.getDB(context).execSQL("update sklad set name=?,ed_izm=?,cenands=?,available=?,ost=?,parentid=?, skidka1=?,skidka2=?,skidka3=?, seb=? where _id=?",
						new Object[] { p.getName(),p.getEd_izm(), p.getCenands(),p.getAvailable(),p.getKolvo(),p.getParentid(),p.getSkidka1(),p.getSkidka2(),p.getSkidka3(), p.getSeb(), p.getId()});
			} else {
				DB.getDB(context).execSQL(
						"insert into sklad(_id,name,ed_izm,cenands,available,ost,parentid, skidka1,skidka2,skidka3, seb) values(?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { p.getId(), p.getName(),p.getEd_izm(), p.getCenands(),p.getAvailable(),p.getKolvo(),p.getParentid(),p.getSkidka1(),p.getSkidka2(),p.getSkidka3(), p.getSeb() });
			}

		}
		dba.close();
	}
	public Cursor getGroups() {
		Cursor c = DB.getDB(context).query(Const.TABLE_GRSKLAD, new String[]{"_id","name"}, null,
				null, null, null, null);
		// Cursor c = dba.getDB(context).query(Const.TABLE_PROPS, null, null,
		// null, null, null, null);
		// c.deactivate();
			
		return c;
		
	}
}
