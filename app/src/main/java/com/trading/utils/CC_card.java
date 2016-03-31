package com.trading.utils;

import java.sql.Date;
import java.util.ArrayList;

public class CC_card {
	public CC_card() {
		super();
		cc_data=new ArrayList<CC_data>();
		
	}

	private int id;
	private Date dt;
	private int client_id;
	private String client_name;
	private int clientcard_id;
	private String clientcard_name;
	private String client_cat;
	private ArrayList<CC_data> cc_data;
	private int docstate;
	private boolean ischanged;
	private int central_id=0;
	private String prim;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int clientId) {
		client_id = clientId;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String clientName) {
		client_name = clientName;
	}

	public int getClientcard_id() {
		return clientcard_id;
	}

	public void setClientcard_id(int clientcardId) {
		clientcard_id = clientcardId;
	}

	public String getClientcard_name() {
		return clientcard_name;
	}

	public void setClientcard_name(String clientcardName) {
		clientcard_name = clientcardName;
	}

	public String getClient_cat() {
		return client_cat;
	}

	public void setClient_cat(String clientCat) {
		client_cat = clientCat;
	}

	public ArrayList<CC_data> getCc_data() {
		return cc_data;
	}

	public void Add_cc_data(CC_data cc_data_element) {
		//if (this.clientcard_id == cc_data_element.getCard_id())
		int addindex=0;
		{
			for (int i = 0; i < this.cc_data.size(); i++) 
			{
				if (cc_data.get(i).getPosition_id()==cc_data_element.getPosition_id())
				{
					addindex=i;
					cc_data.remove(i);
					break;
				}
			}
//			if ((cc_data_element.getOst()>0) || (cc_data_element.getZakaz()>0))
			cc_data.add(addindex ,cc_data_element);

		}

	}

	public int getDocstate() {
		return docstate;
	}

	public void setDocstate(int docstate) {
		this.docstate = docstate;
	}

	public boolean getisIschanged() {
		return ischanged;
	}

	public void setIschanged(boolean ischanged) {
		this.ischanged = ischanged;
	}

	public int getCentral_id() {
		return central_id;
	}

	public void setCentral_id(int centralId) {
		central_id = centralId;
	}
	public String getPrim() {
		return prim;
	}

	public void setPrim(String prim) {
		if (prim ==null)
		this.prim ="";
		else
		this.prim = prim;
	}

}
