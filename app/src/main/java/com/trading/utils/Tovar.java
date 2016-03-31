package com.trading.utils;

public class Tovar {
private int id;

private String name;
private String ed_izm;
private Double cenands;
private Double available;
private Double kolvo;
private Integer parentid;
private Double skidka1;
private Double skidka2;
private Double skidka3;
private Double seb;
public void setSeb(Double seb) {
	this.seb = seb;
}

public Double getSeb() {
	return seb;
}

public Integer getParentid() {
	return parentid;
}

public void setParentid(Integer parentid) {
	this.parentid = parentid;
}

public Double getKolvo() {
  return kolvo;
}

public void setKolvo(Double kolvo) {
  this.kolvo = kolvo;
}


public Tovar()
{
}
public Tovar(int id, String name, String edIzm, Double cenands, Double available, Double kolvo, Integer parentid, Double skidka1, Double skidka2, Double skidka3, Double seb) {
  super();
  this.id = id;
  this.name = name;
  this.ed_izm = edIzm;
  this.cenands = cenands;
  this.available = available;
  this.kolvo = kolvo;
  this.parentid = parentid;
  this.skidka1 = skidka1;
  this.skidka2 = skidka2;
  this.skidka3 = skidka3;
  this.seb = seb;
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
public String getEd_izm() {
  return ed_izm;
}
public void setEd_izm(String edIzm) {
  ed_izm = edIzm;
}
public Double getCenands() {
  return cenands;
}
public void setCenands(Double cenands) {
  this.cenands = (double)(Math.round(cenands*100))/100;
}
public Double getAvailable() {
  return available;
}
public void setAvailable(Double avilable) {
  this.available = avilable;
}

public Double getSkidka1() {
	return skidka1;
}

public void setSkidka1(Double skidka1) {
	this.skidka1 = (double)(Math.round(skidka1*100))/100;}

public Double getSkidka2() {
	return skidka2;
}

public void setSkidka2(Double skidka2) {
	this.skidka2 = (double)(Math.round(skidka2*100))/100;
}

public Double getSkidka3() {
	return skidka3;
}

public void setSkidka3(Double skidka3) {
	this.skidka3 = (double)(Math.round(skidka3*100))/100;
}


}
