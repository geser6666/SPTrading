package com.trading.utils;

public class cc_tovari {
	private int id; 
	private int card_id;
	private int tovar_id;
	private double cat_a;
	private double cat_b;
	private double cat_c;
	public cc_tovari() {
		super();
		// TODO Auto-generated constructor stub
	}
	public cc_tovari(int id, int cardId, int tovarId, double catA, double catB,	double catC) {
		super();
		this.id = id;
		card_id = cardId;
		tovar_id = tovarId;
		cat_a = catA;
		cat_b = catB;
		cat_c = catC;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int cardId) {
		card_id = cardId;
	}
	public int getTovar_id() {
		return tovar_id;
	}
	public void setTovar_id(int tovarId) {
		tovar_id = tovarId;
	}
	public double getCat_a() {
		return cat_a;
	}
	public void setCat_a(double catA) {
		cat_a = catA;
	}
	public double getCat_b() {
		return cat_b;
	}
	public void setCat_b(double catB) {
		cat_b = catB;
	}
	public double getCat_c() {
		return cat_c;
	}
	public void setCat_c(double catC) {
		cat_c = catC;
	}



}
