package com.trading.utils;

import java.util.ArrayList;

public class ClientCard {
	private int id;
	private String name;
	private ArrayList<cc_tovari> tovarlist;
	
	
	private boolean IsExistTov(ArrayList<cc_tovari> arr, int id) {
		boolean ret = false;
		if (arr != null) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getTovar_id() == id) {
					ret = true;
					break;
				}
			}
		}
		return ret;

	}
	private void UpdateTov(ArrayList<cc_tovari> arr, cc_tovari t) {
		boolean ret = false;
		if (arr != null) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getTovar_id() == t.getTovar_id()) {
					arr.get(i).setCard_id(t.getCard_id());
					arr.get(i).setCat_a(t.getCat_a());
					arr.get(i).setCat_b(t.getCat_b());
					arr.get(i).setCat_c(t.getCat_c());

				}
			}
		}

	}
	public void AddTovar(cc_tovari tovar)
	{
		if (IsExistTov(tovarlist, tovar.getTovar_id()))
			UpdateTov(tovarlist, tovar );
		else
			tovarlist.add(tovar);
		
	}
	
	public ClientCard() {
		super();
		tovarlist=new ArrayList<cc_tovari>(); 
		// TODO Auto-generated constructor stub
	}

	
	public ClientCard(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		tovarlist=new ArrayList<cc_tovari>();		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<cc_tovari> getTovarlist() {
		return tovarlist;
	}




	

}
