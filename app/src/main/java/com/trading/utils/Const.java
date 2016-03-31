package com.trading.utils;

public class Const {
	public static final int DOCSTATE_EDIT		  =	0;
	public static final int DOCSTATE_PREPARE_SEND =	1;
	public static final int DOCSTATE_SEND		  =	2;

	public static final String DATABASE_NAME = "db/DTRTrading.sqlite";
	public static final int DATABASE_VERSION = 3;
	public static final int PROGRAMM_VERSION = 1;
	

	
	public static final String TABLE_PARAMS 		=	"params";
	public static final String TABLE_PARTNERS 		=	"partner";
	public static final String TABLE_SKLAD			=	"sklad";
	public static final String TABLE_GRSKLAD		=	"grsklad";	
	public static final String TABLE_ORDERS			=	"orders";
	public static final String TABLE_TORDERS		=	"torders";
	public static final String TABLE_NAKLS			=	"nakls";
	public static final String TABLE_TNAKLS			=	"tnakls";
	public static final String TABLE_PROPS 			=	"props";
//поля таблицы ORDERS
	public static final String TABLE_ORDERS_ID 			=	"_id";
	public static final String TABLE_ORDERS_CLIENT_ID	=	"clientid";
	public static final String TABLE_ORDERS_CLIENT_NAME	=	"clientname";
	public static final String TABLE_ORDERS_DOCDATE		=	"docdate";
	public static final String TABLE_ORDERS_DOCTIME		=	"doctime";
	public static final String TABLE_ORDERS_ITEMCOUNT	=	"itemcount";
	public static final String TABLE_ORDERS_MAINSUMM	=	"mainsumm";
	public static final String TABLE_ORDERS_DOCSTATE	=	"docstate";

	//поля таблицы NAKLS
	public static final String TABLE_NAKLS_ID 			=	"_id";
	public static final String TABLE_NAKLS_CLIENT_ID	=	"clientid";
	public static final String TABLE_NAKLS_CLIENT_NAME	=	"clientname";
	public static final String TABLE_NAKLS_DOCDATE		=	"docdate";
	public static final String TABLE_NAKLS_DOCTIME		=	"doctime";
	public static final String TABLE_NAKLS_ITEMCOUNT	=	"itemcount";
	public static final String TABLE_NAKLS_MAINSUMM	=	"mainsumm";
	public static final String TABLE_NAKLS_DOCSTATE	=	"docstate";
	 
	//поля таблицы PARTNERS
	public static final String TABLE_PARTNERS_ID 		=	"_id";
	public static final String TABLE_PARTNERS_NAME		=	"name";
	public static final String TABLE_PARTNERS_ADDRESS	=	"address";
	public static final String TABLE_PARTNERS_PHONE		=	"phone";
	public static final String TABLE_PARTNERS_DAYSDELAY	=	"daysdelay";
	public static final String TABLE_PARTNERS_MAINSUMM	=	"mainsumm";
	public static final String TABLE_PARTNERS_DEBTSUMM1	=	"debtsumm1";
	  
	
	
	
	////состояние документа
	public static final int DOC_STATE_NEW=-1;
	public static final int DOC_STATE_EDITED=0;
	public static final int DOC_STATE_SAVED=1;
	public static final int DOC_STATE_SENDED=2;
	
	
}
