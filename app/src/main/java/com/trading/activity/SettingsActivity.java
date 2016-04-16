 package com.trading.activity;

import java.util.Map;

import com.trading.R;
import com.trading.dao.ParamsDao;
import com.trading.db.DB;
import com.trading.transfer.DBInteraction;
import com.trading.utils.ConfirmDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends ListActivity{

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0:
			try {

				/*
				Intent intent = new Intent();
				intent.putExtra("_ID", 0);
				intent.putExtra("typedoc", 1);
				intent.putExtra("typepay", 2);
				intent.setClass(this, ServerActivity.class);
				// startActivityForResult(intent1, IDM_EDIT);

				startActivity(intent);
				*/
				final String urlString =PreferenceManager.getDefaultSharedPreferences(this).getString("server", "Сервер не задан!!Обновлений не будет!!!");
				final String serv_localString =PreferenceManager.getDefaultSharedPreferences(this).getString("serv_local", "serv_local");
				final String serv_inetString =PreferenceManager.getDefaultSharedPreferences(this).getString("serv_inet", "serv_inet");
				Map<String, ?> allprefs=	PreferenceManager.getDefaultSharedPreferences(this).getAll();
				final AlertDialog.Builder alert = new AlertDialog.Builder(this);
				final EditText input = new EditText(this);
				alert.setView(input);
				input.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("server", "Сервер не задан!!Обновлений не будет!!!"));
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString().trim();
						if ((value.equals(serv_localString.toString())&&urlString.equals(serv_inetString.toString()))||
								(value.equals(serv_inetString.toString())&&urlString.equals(serv_localString.toString())))
						{
							Toast.makeText(SettingsActivity.this,"Такое имя сервера уже введено в другой настройке.", Toast.LENGTH_SHORT).show();

						} else {
							PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString("server", value).commit();
							if (urlString.equals(serv_inetString.toString())) {

								PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString("serv_inet", value).commit();
							} else {
								PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString("serv_local", value).commit();
							}
						}



											}
				});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.cancel();
							}
						});
				alert.show();

				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case 1:
			try {
				SQLiteDatabase db;
				try {
					startActivity(new Intent(this, ServersListActivity.class));
					
					/*DBInteraction dbitem = new DBInteraction(ParamsDao.getAgentId(SettingsActivity.this), 1,SettingsActivity.this);
					String s;

					dbitem.GetProgram();
*/					
					
					db = DB.getDB(this);
					// db.execSQL("delete from orders where _id=69");

					// db.execSQL("CREATE UNIQUE INDEX cc_data_pos_card ON cc_data(POSITION_ID,CARD_ID)");
					 
					 //db.execSQL("alter table partner add column upd integer");
					
					 //db.execSQL("DROP TRIGGER bd_orders;");
					 //db.execSQL("CREATE TRIGGER bd_orders BEFORE DELETE ON orders BEGIN select case when old.central_id>0 then RAISE (ABORT, 'NOT DELETE!!!') end; delete from torders where grid = old._id; END");
				/*	db.execSQL("drop table torders_temp;");
					 db.execSQL("drop table orders_temp;");
					 try {
						db.execSQL("CREATE TABLE orders_temp ( "+
								  "'_id' INTEGER , "+
								  "'clientid' INTEGER ,"+ 
								  "'clientname' VARCHAR(100),  "+
								  "'docdate' DATE , "+
								  "'doctime' TIME ,"+ 
								  "'itemcount' DOUBLE, "+
								  "'mainsumm' DOUBLE, "+
								  "'paymentdate' DATE, "+
								  "'typepay' INTEGER ,"+ 
								  "'central_id' INTEGER,"+
								  "'property' INTEGER,"+
								  "'prim' varchar(50),"+
								  "'typedoc' INTEGER );");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, e.getMessage(), 10000).show();
					}
					 try {
						db.execSQL("CREATE TABLE torders_temp ("+ 
								"_id INTEGER,"+ 
								"grid INTEGER,"+ 
								"tovid INTEGER,"+ 
								"tovname VARCHAR(100),"+ 
								"ed_izm VARCHAR(10), "+
								"kolvo DOUBLE,"+ 
								"cena DOUBLE, "+
								"cenaprice DOUBLE);");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, e.getMessage(), 10000).show();
					}
					 try {
						db.execSQL("insert into torders_temp select * from torders");  
						 db.execSQL("insert into orders_temp select * from orders");
						 db.execSQL("DROP TRIGGER bd_orders");
						 db.execSQL("delete from torders;");
						 db.execSQL("delete from orders;");
						 db.execSQL("drop table torders;");
						 db.execSQL("drop table orders;");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, e.getMessage(), 10000).show();
					}
					 
					 try {
						db.execSQL("CREATE TABLE orders ( "+
								  "'_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
								  "'clientid' INTEGER CONSTRAINT 'fk_partner' REFERENCES 'partner'('_id') on delete cascade on update cascade,"+ 
								  "'clientname' VARCHAR(100),  "+
								  "'docdate' DATE DEFAULT (CURRENT_DATE), "+
								  "'doctime' TIME DEFAULT (CURRENT_TIME),"+ 
								  "'itemcount' DOUBLE, "+
								  "'mainsumm' DOUBLE, "+
								  "'paymentdate' DATE, "+
								  "'typepay' INTEGER CONSTRAINT 'fk_paytypes' REFERENCES 'paytypes'('_id') DEFAULT 2,"+ 
								  "'central_id' INTEGER,"+
								  "'property' INTEGER,"+
								  "'prim' varchar(50),"+
								  "'typedoc' INTEGER );");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, e.getMessage(), 10000).show();
					}
					 try {
						db.execSQL("CREATE TABLE torders ("+ 
								"_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ 
								"grid INTEGER NOT NULL CONSTRAINT fk_orders REFERENCES orders(_id) on delete cascade on update cascade,"+ 
								"tovid INTEGER NOT NULL CONSTRAINT fk_sklad REFERENCES sklad(_id),"+ 
								"tovname VARCHAR(100),"+ 
								"ed_izm VARCHAR(10), "+
								"kolvo DOUBLE NOT NULL,"+ 
								"cena DOUBLE, "+
								"cenaprice DOUBLE);");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(this, e.getMessage(), 10000).show();
					}

					  try {
							db.execSQL("insert into orders select * from orders_temp");
							 db.execSQL("insert into torders select * from torders_temp");
							 db.execSQL("CREATE TRIGGER bd_orders BEFORE DELETE ON orders BEGIN select case when old.central_id>0 then RAISE (ABORT, \"NOT DELETE!!!\") end; delete from torders where grid = old._id; END");
							 db.execSQL("CREATE TRIGGER \"ai_torders\" " +
							 		" AFTER INSERT " +
							 		" ON TORDERS " +
							 		" BEGIN " +
							 		"update torders set cenaprice=(select cenands from sklad where _id=new.tovid ) where _id=new._id; " +
							 		" END");
							 db.execSQL("drop trigger if exists ai_orders");
							 db.execSQL("drop trigger if exists ai_torders_ei");
							db.execSQL("drop trigger if exists ai_torders_nt");
							db.execSQL("CREATE TRIGGER ai_orders after INSERT ON ORDERS when new.clientname is null " +
									"BEGIN " +
									" update ORDERS " +
									"set clientname= (select name from partner where _id=new.clientid) " +
									" where _id=new._id; " +
									" END ");
							db.execSQL("CREATE TRIGGER ai_torders_ei after INSERT ON tORDERS when new.ed_izm is null " +
									" BEGIN " +
									" update TORDERS " +
									" set ed_izm= (select ed_izm from sklad where _id=new.tovid) " +
									" where _id=new._id; " +
									" END ");
							db.execSQL("CREATE TRIGGER ai_torders_nt after INSERT ON tORDERS when new.tovname is null " +
									" BEGIN " +
									" update TORDERS " +
									" set tovname= (select name from sklad where _id=new.tovid) " +
									" where _id=new._id; " +
									" END");
							 
							 db.execSQL("drop table torders_temp;");
							 db.execSQL("drop table orders_temp;");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Toast.makeText(this, e.getMessage(), 10000).show();
						}
					/*
					 * String[] ddl =
					 * this.getResources().getStringArray(R.array.ddl_cc_data); for
					 * (String ddl_txt: ddl) { try { db.execSQL(ddl_txt); } catch
					 * (Exception e) { String xx; Toast.makeText(MainActivity.this,
					 * e.getMessage(), 10000).show(); // TODO: handle exception } }
					 * /* ddl =
					 * this.getResources().getStringArray(R.array.ddl_cc_data); for
					 * (String ddl_txt: ddl) { try { db.execSQL(ddl_txt); } catch
					 * (Exception e) { String xx; Toast.makeText(MainActivity.this,
					 * e.getMessage(), 10000).show(); // TODO: handle exception } }
					 * // db.execSQL("DELETE from CLIENTCARD ");
					 * //db.execSQL("update orders set typedoc=1");
					 */

					db.close(); 
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				} finally {

				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
			case 2:
			try {
				Toast.makeText(this, "Программа SPTrading. Версия "+getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName,Toast.LENGTH_LONG).show();
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			break;
		}		
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			// setContentView(R.layout.main_menu);
			setListAdapter(ArrayAdapter.createFromResource(this,
					R.array.itemSettings, R.layout.menu_list_item));
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

	}
	

}
