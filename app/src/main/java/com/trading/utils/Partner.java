package com.trading.utils;

import android.test.suitebuilder.annotation.SmallTest;

public class Partner { 


	public int id;
	public Partner(int id, String name, String name_l, String address, String phone,
			int daysdelay, Double debtsumm1, int idskidka, String category, String idhenkel, int week_day) {
		super();
		this.id = id;
		this.name = name;
		this.name_l = name_l;
		this.address = address;
		this.phone = phone;
		this.daysdelay = daysdelay;
		this.debtsumm1 = debtsumm1;
		this.idskidka = idskidka;
		this.category=category;
		this.idhenkel=idhenkel;
		this.week_day=week_day;
		
	}

	public String name;
	public String name_l;
	public String  address;
	public String phone; 
	public int  daysdelay;
	public Double debtsumm1; 
	public int  idskidka;
	public String category;
	public String  idhenkel;
	public int  week_day;
	public Partner() {
		super();
		// TODO Auto-generated constructor stub

	}
}
