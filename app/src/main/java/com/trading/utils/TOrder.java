package com.trading.utils;

public class TOrder {
	private int id;
	private int grid;
	
	private int tovid;
	private String tovname;
	private String toved_izm;
	private Double tovcenands;
	private Double tovkolvo;
	private Double cenaprice;
	
	public TOrder(int id, int grid, int tovid, String tovname, String tovedIzm,
			Double tovcenands, Double tovkolvo) {
		super();
		this.id = id;
		this.grid = grid;
		this.tovid = tovid;
		this.tovname = tovname;
		this.toved_izm = tovedIzm;
		this.tovcenands = tovcenands;
		this.tovkolvo = tovkolvo;
		
	}

	public TOrder()
	{
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGrid() {
		return grid;
	}
	public void setGrid(int grid) {
		this.grid = grid;
	}
	public int getTovid() {
		return tovid;
	}
	public void setTovid(int tovid) {
		this.tovid = tovid;
	}
	public String getTovname() {
		return tovname;
	}
	public void setTovname(String tovname) {
		this.tovname = tovname;
	}
	public String getToved_izm() {
		return toved_izm;
	}
	public void setToved_izm(String tovedIzm) {
		toved_izm = tovedIzm;
	}
	public Double getTovcenands() {
		return tovcenands;
	}
	public void setTovcenands(Double tovcenands) {
		this.tovcenands = (double)(Math.round(tovcenands*100))/100;
		
	}
	public Double getTovkolvo() {
		return tovkolvo;
	}
	public void setTovkolvo(Double tovkolvo) {
		this.tovkolvo = tovkolvo;
	}
	
}
