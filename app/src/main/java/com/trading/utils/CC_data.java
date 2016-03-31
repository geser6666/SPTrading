package com.trading.utils;

public class CC_data {

private int id;
private int card_id;
private int position_id;
private int tovar_id;
private double ost;
private double zakaz;
private double avail;//количестов положенное по категории
private String tovname;


public CC_data() {
	super();
}

public CC_data(int id, int cardId, int positionId, double ost,
			double zakaz, double avail, String tovname, int tovar_id ) {
		this.id = id;
		this.card_id = cardId;
		this.position_id = positionId;
		this.ost = ost;
		this.zakaz = zakaz;
		this.avail = avail;
		this.tovname = tovname;
		this.tovar_id = tovar_id;
	
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
public int getPosition_id() {
	return position_id;
}
public void setPosition_id(int positionId) {
	position_id = positionId;
}
public double getOst() {
	return ost;
}
public void setOst(double ost) {
	this.ost = ost;
}
public double getZakaz() {
	return zakaz;
}
public void setZakaz(double zakaz) {
	this.zakaz = zakaz;
}
public String getTovname() {
	return tovname;
}
public void setTovname(String tovname) {
	this.tovname = tovname;
}
public double getAvail() {
	return avail;
}
public void setAvail(double avail) {
	this.avail = avail;
}

public int getTovar_id() {
	return tovar_id;
}

public void setTovar_id(int tovarId) {
	tovar_id = tovarId;
}


}
