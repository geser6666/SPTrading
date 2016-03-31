package com.trading.utils;

import java.util.ArrayList;

import android.R.bool;

import com.trading.activity.MainActivity;
import com.trading.dao.OrdersDao;

public class Order {

	private int id;
	private int client_id;
	private String client_name;
	private java.sql.Date docdate;
	private java.sql.Time doctime;
	private int itemcount;
	private Double mainsumm = 0.00;
	private int docstate;
	private int property;
	private int central_id;
	private String prim="";
	
	private ArrayList<TOrder> tovarlist;
	private boolean ischanged;
	private int idskidka;
	private int typepay;
	private int typedoc;
	
	private void CheckChangeble() throws Exception
	{
		if (central_id>0)
		throw new InterruptedException("Нельзя изменять закрытый документ!!!");
		
	}
	
	
	public ArrayList<TOrder> getTovarlist() {
		return tovarlist;
	}

	/*
	 * public void setTovarlist(ArrayList<TOrder> tovarlist) { this.tovarlist =
	 * tovarlist; }
	 */
	
	private void CalcMainsum()
	{
		Double ms;
		ms=0.00;
		for (int i = 0; i < tovarlist.size(); i++) {
		 ms=ms+(tovarlist.get(i).getTovkolvo()*tovarlist.get(i).getTovcenands());
		}
		mainsumm=(double)Math.round(ms*100)/100;
	}
	public String getPrim() {
		return prim;
	}

	public void setPrim(String prim) {
		if (prim!=this.prim.toString())
		{
			ischanged=true;
		}
		this.prim = prim;
	}

	private boolean IsExistTov(ArrayList<TOrder> arr, int id) {
		boolean ret = false;
		if (arr != null) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getTovid() == id) {
					ret = true;
					break;
				}
			}
		}
		return ret;

	}

	private void UpdateTov(ArrayList<TOrder> arr, TOrder t) {
		boolean ret = false;
		if (arr != null) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getTovid() == t.getTovid()) {
					arr.get(i).setTovname(t.getTovname());
					arr.get(i).setToved_izm(t.getToved_izm());
					arr.get(i).setTovkolvo(t.getTovkolvo());
					arr.get(i).setTovcenands(t.getTovcenands());

				}
			}
		}

	}

	public void addTovar(TOrder tord) {

		if (IsExistTov(tovarlist, tord.getTovid()))
			UpdateTov(tovarlist, tord);
		else
			tovarlist.add(tord);
		itemcount++;
		//mainsumm = mainsumm + (tord.getTovkolvo() * tord.getTovcenands());
		CalcMainsum();
		ischanged=true;
	}

	public void delTovar(TOrder tord) {

		//mainsumm = mainsumm - (tord.getTovkolvo() * tord.getTovcenands());
		
		for (int i=0;i<tovarlist.size();i++)
		{
		 if (tovarlist.get(i).getTovid()==tord.getId())
		 {
			 tovarlist.remove(tovarlist.get(i));
			 itemcount--;
			 CalcMainsum();
			 ischanged=true;
		 }
		}
		
		
				
	}

	public Order() {
		super();
		ischanged=false;
		doctime= java.sql.Time.valueOf("00:00:00"); 
		tovarlist = new ArrayList<TOrder>();
		// TODO Auto-generated constructor stub
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setClient_id(int clientId) {
		 
		if (clientId!=client_id)
		{
			ischanged=true;
		}
		client_id = clientId;
	}

	public void setClient_name(String clientName) {
		if (client_name != clientName)
			ischanged=true;
		client_name = clientName;
	}

	public void setDocdate(java.sql.Date docdate) {
		if (this.docdate != docdate)
			ischanged=true;
		this.docdate = docdate;
	}

	public void setDoctime(java.sql.Time doctime) {
		if (this.doctime != doctime)
			ischanged=true;
		this.doctime = doctime;
	}

	public void setDocstate(int docstate) {
		if (this.docstate != docstate)
			ischanged=true;
		this.docstate = docstate;
	}

	public int getId() {
		return id;
	}

	public int getClient_id() {
		return client_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public java.sql.Date getDocdate() {
		return docdate;
	}

	public java.sql.Time getDoctime() {
		return doctime;
	}

	public int getItemcount() {
		return itemcount;
	}

	public Double getMainsumm() {
		return mainsumm;
	}

	public int getDocstate() {
		return docstate;
	}

	public Order(int l_id, int lclient_id, String lclient_name, java.sql.Date ldocdate, java.sql.Time ldoctame,
			int litemcount, Double lmainsumm,  int central_id, String prim, int typedoc, int typepay, int property) {
		this.id = l_id;
		this.client_id = lclient_id;
		this.client_name = lclient_name;
		this.docdate = ldocdate;
		this.doctime = ldoctame;
		this.itemcount = litemcount;
		this.mainsumm = lmainsumm;
		this.docstate = docstate;
		this.tovarlist = new ArrayList<TOrder>();
		this.prim=prim;
		this.central_id=central_id;
		this.typedoc=typedoc;
		this.typepay=typepay;
		this.property=property;
		this.ischanged=false;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
	//	if (tovarlist.size()==0)
		
		if (this.property != property)
		{
			
		 ischanged=true;
		}
		
		this.property = property;
		
	}

	public boolean getIsIschanged() {
		return ischanged;
	}

	


	public void setIschanged(boolean ischanged) {
		this.ischanged = ischanged;
	}

	public int getIdskidka() {
		return idskidka;
	}

	public int getTypedoc() {
		return typedoc;
	}

	public void setTypedoc(int typedoc) {
		this.typedoc = typedoc;
	}

	public int getTypepay() {
		return typepay;
	}

	public void setTypepay(int typepay) {
		this.typepay = typepay;
	}

	public void setIdskidka(int idskidka) {
		this.idskidka = idskidka;
	}

	public int getCentral_id() {
		return central_id;
	}

	public void setCentral_id(int central_id) {
		this.central_id = central_id;
	}

}