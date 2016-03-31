package com.trading.db;

public class SpinnerDB {
  public int id;
  public String title;
  
  public SpinnerDB(int _id, String _title) {
    id = _id;
    title = _title;
  }
  
  public String toString(){
    return title;
  }
}
