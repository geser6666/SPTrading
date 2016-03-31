package com.trading.utils;

import java.util.HashMap;
import java.util.Map;

public class CC_data_item extends HashMap<String, String>  {

	
	public static final  String ID="id";
	public static final  String CARD_ID="card_id";
	public static final  String POSITION_ID="position_id";
	public static final  String OST="ost";
	public static final  String ZAKAZ="zakaz"; 
	public static final  String TOVNAME="tovname";
	public static final  String AVAIL="avail";
	public static final  String TOVID="tovid";
	
	
	public CC_data_item(String id, String card_id, String position_id, String ost, String zakaz, String tovname, String avail, String tovid)
	{
	super.put(ID, id); 
	super.put(CARD_ID, card_id); 
	super.put(POSITION_ID, position_id); 
	super.put(OST, ost); 
	super.put(ZAKAZ, zakaz); 
	super.put(TOVNAME, tovname);
	super.put(AVAIL, avail);
	super.put(TOVID, tovid);
	
	}


}
