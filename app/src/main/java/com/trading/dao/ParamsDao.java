package com.trading.dao;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.trading.activity.MainActivity;
import com.trading.db.DB;

public class ParamsDao {
	private DB dba;

	private SQLiteDatabase db;
	// private SQLiteDatabase db;
	private final Context context;

	// private final DBHelper dbhelper;

	public ParamsDao(Context c) {
		context = c;
		dba = new DB(c);
	//	db=dba.getDB(context);

		// dba.getDB(context);
	}
public HashMap<Integer,String> getAgent()
 {
	HashMap hm=new HashMap<Integer,String>();
	hm.put("id", PreferenceManager.getDefaultSharedPreferences(context).getString("manager_name", "НЕТ НИКОГО!!! непорядок!!"));
	hm.put("name", PreferenceManager.getDefaultSharedPreferences(context).getString("manager_id", "0"));
	
	return hm;
	
	}
public static int getAgentId(Context context)
{
	return PreferenceManager.getDefaultSharedPreferences(context).getInt("manager_id", 0);
	}
}
