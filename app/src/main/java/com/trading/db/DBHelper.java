package com.trading.db;


import com.trading.utils.Const;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.BuildConfig;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
	private static Context mContext;
	private static  String LOG_TAG = DBHelper.class.getName();

	private static  String DB_NAME = "DTRTrading.sqlite";
	private static  String DB_FOLDER = "/data/data/com.trading/databases/";
	private static  String DB_PATH = DB_FOLDER + DB_NAME;
	private static  String DB_ASSETS_PATH = "db/" + DB_NAME;
	private static  int DB_VERSION = 3;
	private static  int DB_FILES_COPY_BUFFER_SIZE = 8192;

	public static void Initialize() {
		if (isInitialized() == false) {
			copyInialDBfromAssets();
		}
	}

	private static boolean isInitialized() {

		SQLiteDatabase checkDB = null;
		Boolean correctVersion = false;

		try {
			checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
					SQLiteDatabase.OPEN_READONLY);
			correctVersion = checkDB.getVersion() == DB_VERSION;
		} catch (SQLiteException e) {
			Log.w(LOG_TAG, e.getMessage());
		} finally {
			if (checkDB != null)
				checkDB.close();
		}

		return checkDB != null && correctVersion;
	}

	/**
	 * Копирует файл базы данных из Assets в директорию для баз данных этого
	 * приложения
	 *
	 * @throws ChainedSQLiteException
	 *             если что-то пошло не так при компировании
	 */
	private static void copyInialDBfromAssets() {

		Context appContext = mContext.getApplicationContext();
		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = new BufferedInputStream(appContext.getAssets().open(DB_ASSETS_PATH), DB_FILES_COPY_BUFFER_SIZE);
			File dbDir = new File(DB_FOLDER);
			if (dbDir.exists() == false)
				dbDir.mkdir();
			outStream = new BufferedOutputStream(new FileOutputStream(DB_PATH),
					DB_FILES_COPY_BUFFER_SIZE);

			byte[] buffer = new byte[DB_FILES_COPY_BUFFER_SIZE];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			outStream.flush();
			outStream.close();
			inStream.close();

		} catch (IOException ex) {
			// Что-то пошло не так
			Log.e(LOG_TAG, ex.getMessage());
			throw new SQLiteException(
					"Fail to copy initial db from assets", ex);
		} finally {

		//	IOUtils.closeSilent(outStream);
			//IOUtils.closeSilent(inStream);
		}
	}
	public DBHelper(Context context) {

	//	super(context, Const.DATABASE_NAME, null, Const.DATABASE_VERSION);
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("onCreate", String.format("in the onCreate"));
		throw new SQLiteException(
				"Call OlimpicRaceSQLhelper.Initialize first. This method should never be called.");

		//Initialize();

		// TODO Auto-generated method stub
	/*	String[] ddl = mContext.getResources().getStringArray(R.array.ddl_of_database);
		for (String ddl_txt: ddl)
		{
			try {
				db.execSQL(ddl_txt);	
			} catch (Exception e) {
				String xx;
				xx=e.getMessage();
				// TODO: handle exception
			} 
			
		}
		 ddl = mContext.getResources().getStringArray(R.array.ddl_of_cc);
		for (String ddl_txt: ddl)
		{
			try {
				db.execSQL(ddl_txt);	
			} catch (Exception e) {
				String xx;
				xx=e.getMessage();
				// TODO: handle exception
			} 
			
		}*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		Log.d("onUpgradeDB",String.format("New version: %d, Old version: %d", new_version, old_version));
		throw new SQLiteException(
				"Call OlimpicRaceSQLhelper.Initialize first. This method should never be called.");

			/*if (old_version != new_version) {
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_PARAMS);
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_PARTNERS);
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_SKLAD);
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_ORDERS);
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_TORDERS);
			db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_PROPS);
			db.execSQL("DROP TABLE IF EXISTS " + "clientcard");
			db.execSQL("DROP TABLE IF EXISTS " + "cc_tovari");
			db.execSQL("DROP TABLE IF EXISTS " + "cc_card");
			db.execSQL("DROP TABLE IF EXISTS " + "cc_data");
			onCreate(db);
		}
		*/
		
	}
	public SQLiteDatabase Open() {
		return getWritableDatabase();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys = ON;");
	}
	

}
