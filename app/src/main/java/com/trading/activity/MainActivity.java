package com.trading.activity;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.trading.R;
import com.trading.dao.ParamsDao;
import com.trading.db.DB;
import com.trading.db.DBHelper;
import com.trading.services.GPSService;
import com.trading.transfer.DBInteraction;
import com.trading.utils.ManagerDialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	/** Called when the activity is first created. */
	private final static int MAINMENU_NEW_ORDER = 0;
	private final static int MAINMENU_NEW_NAKL = 1;
	private final static int MAINMENU_NEW_INVENTA = 2;
	private final static int MAINMENU_CC_MENU = 3;
	private final static int MAINMENU_REFERENCE = 4;
	private final static int MAINMENU_JOURNAL = 5;
	private final static int MAINMENU_TRASFER = 6;
	private final static int MAINMENU_EXIT = 8;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private class OnReadyListener implements ManagerDialog.ReadyListener {

		@Override
		public void ready(int managerid, String managername) {
			// TODO Auto-generated method stub
			SharedPreferences prefs = 
	            PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
	        try {
				prefs.edit().putInt("manager_id",managerid).commit();
				prefs.edit().putString("manager_name",managername).commit();
				DB.ClearDatabase();
				MainActivity.this.setTitle("DTRTrading "+managername);
			} catch (Exception e) {
				// TODO: handle exception
			}  

			
			
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
        SharedPreferences prefs = 
            PreferenceManager.getDefaultSharedPreferences(this);
        
        // читаем установленное значение 
        int m;
        m=prefs.getInt("manager_id", 0);

        if (m==0) {
			ManagerDialog myDialog = new ManagerDialog(MainActivity.this,0,"",
					new OnReadyListener());
			
			myDialog.show();

        }
        this.setTitle("DTRTrading "+PreferenceManager.getDefaultSharedPreferences(this).getString("manager_name", "НЕТ НИКОГО!!! непорядок!!"));  	

        try {
			  LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		       if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		//            createGpsDisabledAlert();
		       }
		} catch (Exception e) {
			String s = e.getMessage();
		}

	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//провеhztv версию
		try {

		 	final DBInteraction dbitem = new DBInteraction(ParamsDao.getAgentId(this),1, MainActivity.this);

			final String[] servver = new String[1];
			String localver;
			AsyncTask at=new AsyncTask<Object,Integer,String>() {
				@Override
				protected String doInBackground(Object...objects) {
					final String output;
					DBInteraction dbitem = new DBInteraction(ParamsDao.getAgentId(MainActivity.this),1, MainActivity.this);
					output = dbitem.GetNewProgramVersion2();
					return output;
				}
				@Override
				protected void onPostExecute(String o) {
					servver[0] = (String) o;
				}
  		};

			try {
				servver[0]=at.execute().get(2, TimeUnit.SECONDS).toString();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				throw new Exception("Скорее всего нет связи с сервером.");
			}
			localver=getPackageManager().getPackageInfo(getPackageName(),0).versionName;
			if (Float.valueOf(servver[0])>Float.valueOf(localver))
			{

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Обнаружена новая версия приложения. Обновить?")
						.setCancelable(false)
						.setPositiveButton("Обновить",
								new DialogInterface.OnClickListener(){
									@TargetApi(Build.VERSION_CODES.LOLLIPOP)
									public void onClick(DialogInterface dialog, int id){
										File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "SPTrading.apk");
										file.delete();

										try {
											dbitem.GetProgram();
										} catch (IOException e) {
											e.printStackTrace();
										}
										Intent intent = new Intent(Intent.ACTION_VIEW);

										intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
										startActivity(intent);
										MainActivity.this.finish();

									}
								});
				builder.setNegativeButton("Не обновлять",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();




			};


		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(MainActivity.this, "неудача" + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

/////////////////////////////////////////////////////////////
		try {
			// setContentView(R.layout.main_menu);
			setListAdapter(ArrayAdapter.createFromResource(this,
					R.array.itemMain, R.layout.menu_list_item));
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

		try {
//инитим базу
			(new DBHelper(this)).Initialize();
			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.DAY_OF_MONTH, 0);
			Date sqlDate = new Date(cl.getTime().getTime());
			SQLiteDatabase db = (new DBHelper(this)).Open();

			db.execSQL("update orders set docdate=? where central_id<=0",
					new Object[] { sqlDate.toString() });
			db.close();
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

		try {
			
			
			
			startService(new Intent(MainActivity.this, GPSService.class));

		} catch (Exception e) {
			String s = e.getMessage();
		}

	}
	private void createGpsDisabledAlert(){  
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		builder.setMessage("У вас выключен GPS! Возможен пистон от начальства! Включить GPS?")  
		     .setCancelable(false)  
		     .setPositiveButton("Включить GPS",  
		          new DialogInterface.OnClickListener(){  
		          public void onClick(DialogInterface dialog, int id){  
		               showGpsOptions();
		               
		               
		          }  
		     });  
		     builder.setNegativeButton("Не включать",  
		          new DialogInterface.OnClickListener(){  
		          public void onClick(DialogInterface dialog, int id){  
		               dialog.cancel();  
		          }  
		     });  
		AlertDialog alert = builder.create();  
		alert.show();  
		}  
		  
		private void showGpsOptions(){  
		        Intent gpsOptionsIntent = new Intent(  
		                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
		        startActivity(gpsOptionsIntent);  
		}  
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		try {
			startService(new Intent(MainActivity.this, GPSService.class));

		} catch (Exception e) {
			String s = e.getMessage();
		}
		

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		switch (position) {
		case MAINMENU_NEW_ORDER:

			try {

				Intent intent = new Intent();
				intent.putExtra("_ID", 0);
				intent.putExtra("typedoc", 1);
				intent.putExtra("typepay", 2);
				intent.setClass(this, OrderActivity.class);
				// startActivityForResult(intent1, IDM_EDIT);
				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case MAINMENU_NEW_NAKL:
			try {
				Intent intent = new Intent();
				intent.putExtra("_ID", 0);
				intent.putExtra("typedoc", 2);
				intent.putExtra("typepay", 2);
				intent.setClass(this, OrderActivity.class);
				// startActivityForResult(intent1, IDM_EDIT);

				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case MAINMENU_NEW_INVENTA:
			try {
				Intent intent = new Intent();
				intent.putExtra("_ID", 0);
				intent.putExtra("typedoc", 3);
				intent.putExtra("typepay", 2);
				intent.setClass(this, OrderActivity.class);
				// startActivityForResult(intent1, IDM_EDIT);

				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case MAINMENU_CC_MENU:
			try {
				startActivity(new Intent(this, ClientCardMenuActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;

		case MAINMENU_REFERENCE:
			try {
				startActivity(new Intent(this, ReferenceListActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;

		case MAINMENU_JOURNAL:
			try {
				startActivity(new Intent(this, OrdersListActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;

		case MAINMENU_TRASFER:
			try {
				startActivity(new Intent(this, SyncActivity.class));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case 7:
			startActivity(new Intent(this, SettingsActivity.class));
/*			//*/
			break;
		case MAINMENU_EXIT:
			finish();

			break;
		default:

			Toast.makeText(this, "В разработке.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// stopService(new Intent(MainActivity.this, GPSService.class));
	}

}