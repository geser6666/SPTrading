package com.trading.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trading.R;
import com.trading.dao.OrdersDao;
import com.trading.dao.ParamsDao;
import com.trading.dao.PartnersDao;
import com.trading.dao.TovarsDao;
import com.trading.db.SpinnerDB;
import com.trading.transfer.DBInteraction;
import com.trading.utils.ConfirmDialog;
import com.trading.utils.KolvoDialog;
import com.trading.utils.Order;
import com.trading.utils.TOrder;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;

public class OrderActivity extends TabActivity {
	private int causedactivity;
	private int OrderId;
	private int typedoc, typepay;
	private TextView sTVZakaznumb, sTVZakazdate, zakazclient, tovid, tovname,
			toved_izm, tovkolvo, sTVZakazsumma, tvTypepay;
	private Spinner sSpFirma, sSPTorderFIler, sSGroupTov, sSpSkidka;

	private Button sBtZakazClient,  sBtAddTov, sBTSend;
	private ImageButton  ibOrderSearchBtn;
	private EditText eTZakazPrim;

	private ListView lwTOrders;
	private int lasttov=0;
	// private CursorAdapter mFirmaAdapter;

	//private SimpleCursorAdapter mFirmaAdapter;
	//private SimpleCursorAdapter tovAdapter;

	private Order ordr;
	private OrdersDao od = new OrdersDao(this);

	private PartnersDao pd = new PartnersDao(this);
	private TOrder selectedtovar;
	private int lastgridtov = -1;

	private String searchQuery="";

	private void handleIntent(Intent intent) {
		Log.d("ttt", "HandleIntent");
//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			searchQuery = intent.getStringExtra(SearchManager.QUERY);
//
//		//	Intent newintent = new Intent();
//			intent.putExtra("searchQuery", searchQuery);
//			intent.setClass(OrderActivity.this, TovarsListActivity.class);
//			causedactivity = 2;
//			startActivityForResult(intent, 0);
//			intent.setAction(Intent.ACTION_VIEW);
//			//ApplyAdapter(tb_mar.isChecked(),0);
//		}
//		searchQuery = "";

	}
	@Override
	protected void onNewIntent(Intent intentt) {
		Log.d("ttt", "OrderActivity onNewIntent");
//		setIntent(getIntent());
//
//		searchQuery = intentt.getStringExtra(SearchManager.QUERY);
//		getIntent().putExtra("searchQuery", 	searchQuery);
//		getIntent().setClass(OrderActivity.this, TovarsListActivity.class);
//		causedactivity = 2;
//		startActivityForResult(getIntent(), 0);
//


		//	handleIntent(intent);
	}
	private class OnReadyListener implements KolvoDialog.ReadyListener {
		@Override
		public void ready(Double kolvo, Double cena) {
			selectedtovar.setTovkolvo(kolvo);
			selectedtovar.setTovcenands(cena);
			ordr.addTovar(selectedtovar);
			fillTovars();

		}
	}

	private class OnSaveListener implements ConfirmDialog.ReadyListener {
		@Override
		public void ready(boolean yesno) {

		}
	}

	/*
	 * private class OnReadyConfirmListener implements
	 * ConfirmDialog.ReadyListener {
	 *
	 * @Override public void ready(boolean yesno) {
	 *
	 * } }
	 */
	private void prepareDataView() {
		sTVZakaznumb = (TextView) findViewById(R.id.sTVZakaznumb);
		sTVZakazdate = (TextView) findViewById(R.id.sTVZakazdate);
		sTVZakazsumma = (TextView) findViewById(R.id.zakazsumma);
		// ///-------------------------------------------------
		sSpFirma = (Spinner) findViewById(R.id.sSpFirma);

		ArrayList<SpinnerDB> items;
		items = new ArrayList<SpinnerDB>();
		ArrayAdapter<SpinnerDB> adapter = new ArrayAdapter<SpinnerDB>(this,
				R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		Cursor cursor = od.getFirms();
		if (cursor.moveToFirst())
			do {
				items.add(new SpinnerDB(cursor.getInt(0), cursor.getString(1)));
			} while (cursor.moveToNext());
		cursor.close();
		sSpFirma.setAdapter(adapter);

		/*
		 * sSpFirma = (Spinner) findViewById(R.id.sSpFirma); Cursor cur =
		 * od.getFirms(); mFirmaAdapter = new SimpleCursorAdapter(this,
		 * R.layout.simple_spinner_item, // Use a template cur, // Give the
		 * cursor to the list adapter new String[] { "name" }, // Map the NAME
		 * column in the new int[] { android.R.id.text1 }); // The "text1" view
		 * // defined in mFirmaAdapter
		 * .setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		 * sSpFirma.setAdapter(mFirmaAdapter);
		 */
		sSpFirma.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				ordr.setProperty(((SpinnerDB) sSpFirma.getSelectedItem()).id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		//////////////////////////////////////////////////////////////
		sSpSkidka = (Spinner) findViewById(R.id.sSpSkidka);
		sSpSkidka.setAdapter(ArrayAdapter.createFromResource(
				this,R.array.itemSkidki, R.layout.simple_spinner_item));
		sSpSkidka.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int position, long arg3) {
				// TODO Auto-generated method stub
				ordr.setIdskidka(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		//
		////////////////////////////////////////////////////////////
		// /-----------------------------------------------------------------------------
		zakazclient = (TextView) findViewById(R.id.zakazclient);
		sBtZakazClient = (Button) findViewById(R.id.sBtZkazClient);
		sBtZakazClient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				intent.putExtra("_ID", 0);
				intent.putExtra("week_day", Calendar.getInstance().getTime().getDay());
				intent.putExtra("selectmode", true);
				intent.setClass(OrderActivity.this, PartnersListActivity.class);
				causedactivity = 1;
				startActivityForResult(intent, 0);

			}
		});

		sBtAddTov = (Button) findViewById(R.id.sBtAddTov);
		sBtAddTov.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("grid", ordr.getId());
				// intent.putExtra("lastgridtov", lastgridtov);
				intent.putExtra("lastgridtov", ((SpinnerDB) sSGroupTov
						.getSelectedItem()).id);
				intent.putExtra("idskidka", ordr.getIdskidka());
				intent.putExtra("typedoc", ordr.getTypedoc());
				intent.putExtra("idpartner", ordr.getClient_id());
				intent.putExtra("lasttov", lasttov);

				intent.setClass(OrderActivity.this, TovarsListActivity.class);

				causedactivity = 2;
				startActivityForResult(intent, 0);
				lastgridtov = ((SpinnerDB) sSGroupTov.getSelectedItem()).id;

			}
		});
		ibOrderSearchBtn=(ImageButton)findViewById(R.id.ibOrderSearchBtn);
		ibOrderSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//		onSearchRequested();
				//Toast.makeText(OrderActivity.this,"Нажат поиск",Toast.LENGTH_LONG).show();
			}
		});

		/*---------------------tovars----------------------*/
		lwTOrders = (ListView) findViewById(R.id.listTorder);
		tovid = (TextView) findViewById(R.id.torder_tovid);
		tovname = (TextView) findViewById(R.id.torder_tovname);
		toved_izm = (TextView) findViewById(R.id.torder_toved_izm);
		tovkolvo = (TextView) findViewById(R.id.torder_tovkolvo);
		lwTOrders.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
				// TODO Auto-generated method stub
				TOrder temp=new TOrder();
				temp.setId(Integer.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("tovid").toString()));
				ordr.delTovar(temp);
				fillTovars();
				return true;
			}
		});

		/* отправка заявки */
		sBTSend = (Button) findViewById(R.id.sBTSend);
		sBTSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ordr.getCentral_id() > 0)
					Toast.makeText(OrderActivity.this,
							"Не могу повторно отправить!!!", Toast.LENGTH_SHORT).show();
				else
					//SendOrder();
					new SendOrderTask().execute();
			}
		});
		/* ------------------------------- */
		eTZakazPrim = (EditText) findViewById(R.id.eTZakazPrim);
		eTZakazPrim.setText("");

		eTZakazPrim.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!ordr.getPrim().toString().equalsIgnoreCase(s.toString()))
					ordr.setPrim(s.toString());

			}
		});

		// tvTypepay = (TextView) findViewById(R.id.tvTypepay);
		// ///////////////////////////////////////////////////////////////////////////
		TovarsDao td = new TovarsDao(this);
		sSGroupTov = (Spinner) findViewById(R.id.sSGroupTov);
		sSGroupTov.setOnItemSelectedListener(grselectListener);
		items = new ArrayList<SpinnerDB>();
		ArrayAdapter<SpinnerDB> tovadapter = new ArrayAdapter<SpinnerDB>(this,
				R.layout.simple_spinner_item, items);
		tovadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		Cursor tovcursor = td.getGroups();
		if (tovcursor.moveToFirst())
			do {
				items.add(new SpinnerDB(tovcursor.getInt(0), tovcursor
						.getString(1)));
			} while (tovcursor.moveToNext());
		tovcursor.close();
		sSGroupTov.setAdapter(tovadapter);
		sSGroupTov.setSelection(-1);
		//sSGroupTov.setOnItemSelectedListener(grselectListener);
		//lastgridtov = ((SpinnerDB) sSGroupTov.getSelectedItem()).id;
		// ///////////////////////////////////////////////////////////////////////////
	}

	OnItemSelectedListener grselectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
								   long arg3) {
			if (lastgridtov!=-1)
			{
				Intent intent = new Intent();
				intent.putExtra("grid", ordr.getId());
				intent.putExtra("lastgridtov", ((SpinnerDB) sSGroupTov
						.getSelectedItem()).id);
				intent.putExtra("idskidka", ordr.getIdskidka());
				intent.putExtra("typedoc", ordr.getTypedoc());
				intent.putExtra("idpartner", ordr.getClient_id());
				intent.setClass(OrderActivity.this, TovarsListActivity.class);
				causedactivity = 2;
				startActivityForResult(intent, 0);
			}
			lastgridtov = ((SpinnerDB) sSGroupTov.getSelectedItem()).id;


		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	private class SendOrderTask extends AsyncTask<Void, Void, Void> {
		int cc;
		DBInteraction dbitem;
		@Override
		protected Void doInBackground(Void... params) {
			try {

				if (ordr.getIsIschanged() == false) {
					cc = dbitem.UploadOrder(ordr.getId(), OrderActivity.this);
					return null;
				} else {
					SaveClick2(true);

				}
			}
			catch (Exception e) {
				Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dbitem = new DBInteraction(ParamsDao.getAgentId(OrderActivity.this), ordr.getProperty(), OrderActivity.this);
			//Toast.makeText(OrderActivity.this,"Begin",Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			ordr.setCentral_id(cc);
			if (ordr.getIsIschanged() == false)
			{
				switch (cc) {
					case -1:
						Toast.makeText(OrderActivity.this,
								"Ошибка при работе с БД.", Toast.LENGTH_SHORT)
								.show();
						break;
					case -2:
						Toast.makeText(OrderActivity.this,
								"Неверный XML. Обратитесь к администратору.",
								Toast.LENGTH_SHORT).show();
						break;

					default:
						od.SaveOrder(ordr);
						try {
							Toast.makeText(OrderActivity.this,
									"Заявка успешно отправлена.", Toast.LENGTH_SHORT)
									.show();
						}
						catch (Exception e)
						{
							String s=e.getMessage();
							s=e.getMessage();
						}



						finish();
						break;
				}
			}
			//	Toast.makeText(OrderActivity.this,"End",Toast.LENGTH_SHORT).show();
		}
	}
	private void SendOrder()  {
		DBInteraction dbitem = new DBInteraction(ParamsDao.getAgentId(this), ordr.getProperty(), this);
		try {

			if (ordr.getIsIschanged() == false) {


				int cc = dbitem.UploadOrder(ordr.getId(), OrderActivity.this);
				ordr.setCentral_id(cc);

				switch (cc) {
					case -1:
						Toast.makeText(OrderActivity.this,
								"Ошибка при работе с БД.", Toast.LENGTH_SHORT)
								.show();
						break;
					case -2:
						Toast.makeText(OrderActivity.this,
								"Неверный XML. Обратитесь к администратору.",
								Toast.LENGTH_SHORT).show();
						break;

					default:
						od.SaveOrder(ordr);
						try {
							Toast.makeText(OrderActivity.this,
									"Заявка успешно отправлена.", Toast.LENGTH_SHORT)
									.show();
						}
						catch (Exception e)
						{
							String s=e.getMessage();
							s=e.getMessage();
						}



						finish();
						break;
				}

			} else {
				SaveClick2(true);

			}
		} catch (Exception e) {
			Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void fillTovars() {
		List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
		Map<String, Object> map;
		for (int i = 0; i < ordr.getTovarlist().size(); i++) {
			map = new HashMap<String, Object>();
			map.put("tovid", ordr.getTovarlist().get(i).getTovid());
			map.put("tovname", ordr.getTovarlist().get(i).getTovname());
			map.put("ed_izm", ordr.getTovarlist().get(i).getToved_izm());
			map.put("kolvo", ordr.getTovarlist().get(i).getTovkolvo());
			map.put("cena", ordr.getTovarlist().get(i).getTovcenands());
			items.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.torder_item, new String[] { "tovid", "tovname",
				"ed_izm", "kolvo", "cena" }, new int[] {
				R.id.torder_tovid, R.id.torder_tovname,
				R.id.torder_toved_izm, R.id.torder_tovkolvo,
				R.id.torder_tovcena });

		lwTOrders.setAdapter(adapter);

		OnItemClickListener tovarlistener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long id) {
				// ((java.util.HashMap)arg0.getItemAtPosition(position)).get("kolvo");

				// TODO Auto-generated method stub


				try {
					// lwTOrders.getSelectedItem()
					String s = "ssss";
					selectedtovar = new TOrder(0, ordr.getId(),
							Integer.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("tovid").toString()),
							((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("tovname")
									.toString(), ((java.util.HashMap) arg0
							.getItemAtPosition(position)).get("ed_izm")
							.toString(), Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("cena")
									.toString()), Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("kolvo")
									.toString()));

					KolvoDialog myDialog = new KolvoDialog(OrderActivity.this,
							Double.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("kolvo")
									.toString()), Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get(
									"cena").toString()),
							new OnReadyListener());

					myDialog.show();

				} catch (Exception e) {
					String s = e.getMessage();
				}
			}

		};
		lwTOrders.setOnItemClickListener(tovarlistener);

	}

	private void setData() {
		sTVZakaznumb.setText(String.valueOf(ordr.getId()));
		sTVZakazdate.setText(ordr.getDocdate().toString());
		zakazclient.setText(ordr.getClient_name());
		// sSpFirma.setSelection(ordr.getProperty() - 1);

		ArrayAdapter<?> myAdap = (ArrayAdapter<?>) sSpFirma.getAdapter();
		// sSpFirma.setSelection(-1);
		boolean x = false;

		for (int i = 0; i < myAdap.getCount(); i++) {
			int spinnerPosition = ((SpinnerDB) myAdap.getItem(i)).id;
			if (spinnerPosition == ordr.getProperty()) {
				sSpFirma.setSelection(i);
				x = true;
				break;
			}
			if (!x) {
				sSpFirma.setSelection(0);
				ordr.setProperty(((SpinnerDB) sSpFirma.getSelectedItem()).id);
			}

		}

		eTZakazPrim.setText(ordr.getPrim().toString());

		fillTovars();
		sTVZakazsumma.setText(ordr.getMainsumm().toString());

		// tvTypepay.setText(String.valueOf(ordr.getTypepay()));
	}

	private void SaveClick2(final boolean sendaftersave) {
		if (ordr.getCentral_id() > 0) {
			Toast.makeText(OrderActivity.this,
					"Не могу сохранить закрытый документ!", Toast.LENGTH_SHORT).show();

			finish();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Сохранить заявку?");
		// кнопка "Yes", при нажатии на которую приложение закроется
		builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (ordr.getClient_id() == 0) {
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							OrderActivity.this);
					builder2.setMessage("Не выбран партнер! Нельзя сохранять!")
							.setNeutralButton("Ок",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated
											// method stub
										}
									}).show();

				} else {
					OrderId = od.SaveOrder(ordr);
					ordr = od.getOrder(OrderId);
					ordr.setIschanged(false);
					if (sendaftersave)
						//	SendOrder();
						new SendOrderTask();
					finish();
					OrderActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK,
							new KeyEvent(KeyEvent.ACTION_DOWN,
									KeyEvent.KEYCODE_BACK));

				}
			}
		});
		// кнопка "No", при нажатии на которую ничего не произойдет
		builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		builder.show();

	}

	private boolean SaveClick() {

		if (ordr.getIsIschanged()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Сохранить заявку?");
			// кнопка "Yes", при нажатии на которую приложение закроется
			builder.setPositiveButton("Да",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
											int whichButton) {
							if (ordr.getClient_id() == 0) {
								AlertDialog.Builder builder2 = new AlertDialog.Builder(
										OrderActivity.this);
								builder2
										.setMessage(
												"Не выбран партнер! Нельзя сохранять!")
										.setNeutralButton(
												"Ок",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
													}
												}).show();

							} else {
								od.SaveOrder(ordr);
								ordr.setIschanged(false);

								finish();
							}
						}
					});
			// кнопка "No", при нажатии на которую ничего не произойдет
			builder.setNegativeButton("Нет",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
											int whichButton) {
							finish();
						}
					});
			builder.show();
		}

		return ordr.getIsIschanged() == false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			/*
			 * ConfirmDialog myDialog = new
			 * ConfirmDialog(this,"Сохранить заявку?", new ReadyListener() {
			 *
			 * @Override public void ready(boolean yesno) {
			 *
			 * if (yesno) { Toast.makeText(OrderActivity.this,
			 * String.valueOf(yesno), 30000).show(); }
			 *
			 * } }); myDialog.show();
			 */

			if (ordr.getIsIschanged()) {
				SaveClick2(false);
				keyCode = KeyEvent.KEYCODE_0;
			}
			/*
			 * if (SaveClick()) {
			 *
			 * keyCode = KeyEvent.KEYCODE_BACK;
			 *
			 * } else { keyCode = KeyEvent.KEYCODE_0; }
			 */
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//	Toast.makeText(OrderActivity.this, "stop", Toast.LENGTH_SHORT).show();

		Log.d("ttt", "OrderActivity onStop");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//	Toast.makeText(OrderActivity.this, "pause", Toast.LENGTH_SHORT).show();

		Log.d("ttt", "OrderActivity onPause");
	}


//	@Override
//	public boolean onSearchRequested() {
//	//	Toast.makeText(this,"SearchRequested", Toast.LENGTH_SHORT).show();
//		Log.d("ttt", "OrderActivity onSearchRequest");
//		return super.onSearchRequested();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub


		try {
			//	requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.order);
			super.onCreate(savedInstanceState);
			//handleIntent(getIntent());
			Log.d("ttt", "OrderActivity onCreate");

			Bundle extras = getIntent().getExtras();
			OrderId = extras.getInt("_ID");
			typedoc = extras.getInt("typedoc");
			typepay = extras.getInt("typepay");

			ordr = od.getOrder(OrderId);
			ordr.setTypedoc(typedoc);
			ordr.setTypepay(typepay);

			prepareDataView();
			setData();
			// ///////////////

			TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
			tabs.setup();
			tabs.setOnTabChangedListener(new OnTabChangeListener() {

				@Override
				public void onTabChanged(String tabId) {
					if (tabId=="tag1")
					{
						sTVZakazsumma.setText(ordr.getMainsumm().toString());
					}

				}
			});

			TabHost.TabSpec spec = tabs.newTabSpec("tag1");

			spec.setContent(R.id.tabview1);
			spec.setIndicator(getResources().getText(R.string.ZAKAZ_CLIENT));
			tabs.addTab(spec);

			spec = tabs.newTabSpec("tag2");
			spec.setContent(R.id.tabview2);
			spec.setIndicator("Товары");
			tabs.addTab(spec);

			spec = tabs.newTabSpec("tag3");
			spec.setContent(R.id.tabview3);
			spec.setIndicator("Разное");
			tabs.addTab(spec);

			tabs.setCurrentTab(0);
			tabs.getTabWidget().getChildAt(0).getLayoutParams().height = 60;
			tabs.getTabWidget().getChildAt(1).getLayoutParams().height = 60;
			tabs.getTabWidget().getChildAt(2).getLayoutParams().height = 60;
			// ///////////////
			/*
			 * TabHost mTabHost = this.getTabHost();
			 *
			 *
			 * mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(
			 * getResources().getText(R.string.ZAKAZ_CLIENT)).setContent(
			 * R.id.tabview1));
			 * mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(
			 * "Товары").setContent(R.id.tabview2));
			 * mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(
			 * "Разное").setContent(R.id.tabview3)); mTabHost.setCurrentTab(0);
			 * mTabHost.getTabWidget().getChildAt(0).getLayoutParams().height =
			 * 40;
			 * mTabHost.getTabWidget().getChildAt(1).getLayoutParams().height =
			 * 40;
			 * mTabHost.getTabWidget().getChildAt(2).getLayoutParams().height =
			 * 40;
			 */
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("ttt", "OrderActivity onActivityResult result="+String.valueOf(resultCode));
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			// extras.getString("_id")
			switch (causedactivity) {
				case 1:
					zakazclient.setText(extras.getString("name").toString());
					ordr.setClient_id(extras.getInt("_id"));
					ordr.setClient_name(extras.getString("name"));
					ordr.setIdskidka(extras.getInt("idskidka"));

					break;
				case 2:
					// lastgridtov = extras.getInt("lastgridtov");
					ordr.addTovar(new TOrder(0, ordr.getId(), extras
							.getInt("idtov"), extras.getString("name"), extras
							.getString("ed_izm"), extras.getDouble("cena"), extras
							.getDouble("kolvo")));
					setData();
					lasttov=extras.getInt("idtov");

					break;

				default:
					break;
			}

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("ttt", "OrderActivity onResume");


	}

}
