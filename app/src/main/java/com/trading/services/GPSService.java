package com.trading.services;

import java.util.Calendar;
import java.util.Date;

import com.trading.activity.MainActivity;
import com.trading.activity.SyncActivity;
import com.trading.dao.ParamsDao;
import com.trading.transfer.DBInteraction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class GPSService extends Service {
	private LocationManager locationManager;
	private boolean mIsRunning;
	private final LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			//   Toast.makeText(GPSService.this, "Статус"+String.valueOf(status) , 20000).show();
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			 //  Toast.makeText(MainActivity.this, "Включен провайдер"+provider , 20000).show();
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			 //  Toast.makeText(MainActivity.this, "Выключен провайдер"+provider , 20000).show();
			
		}
		
		@Override
		public void onLocationChanged(final Location location) {
			// TODO Auto-generated method stub
			// создаем новый поток
			try {
			Thread background = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (mIsRunning) 
						try {
							if (location!=null)
							{
								//stopService(new Intent(GPSService.this, GPSService.class));
								double lat = location.getLatitude();
								double lng = location.getLongitude();
								DBInteraction dbitem = new DBInteraction(
										ParamsDao.getAgentId(GPSService.this), 1, GPSService.this);
								dbitem.UploadGPS( lat, lng);
							//	stopService(new Intent(GPSService.this, GPSService.class));
							}
						} 
						catch (Exception e) { 
							String s = e.getMessage();								
						}
						finally
						{
							mIsRunning = false;
						}
				}
			});
			mIsRunning = true; 
			background.start();
			Calendar cl = Calendar.getInstance();
			Date sqlDate = new Date(cl.getTime().getTime());
			if (Calendar.getInstance().getTime().getHours()>21)
			{
			stopService(new Intent(GPSService.this, GPSService.class));
			}
			   
		} catch (Exception e) {
			// TODO: handle exception
		}
		}
	};


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//super.onCreate();
		//Toast.makeText(this, "Service Created",Toast.LENGTH_SHORT).show(); 

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	//	super.onDestroy();
		//Toast.makeText(this, "Service Stopped",Toast.LENGTH_SHORT).show(); 

		if (locationManager!=null)
			locationManager.removeUpdates(locationListener);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
//		super.onStart(intent, startId);
		//Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show(); 

		try {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		   //String provider = LocationManager.GPS_PROVIDER;
		// Get the best provider using the default criteria
		   String provider = locationManager.getBestProvider(new Criteria(), true);
		   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 2000, locationListener);
		   Location location = locationManager.getLastKnownLocation(provider);
		
		} catch (Exception e) {
			   Toast.makeText(this, "Ошибка:"+e.getMessage() , 20000).show();

		} 
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
