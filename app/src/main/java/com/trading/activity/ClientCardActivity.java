package com.trading.activity;

import java.util.ArrayList;

import com.trading.R;
import com.trading.adapters.CC_DataAdapter;
import com.trading.dao.ClientCardsDao;
import com.trading.dao.ParamsDao;
import com.trading.dao.TovarsDao;
import com.trading.db.SpinnerDB;
import com.trading.services.GPSService;
import com.trading.transfer.DBInteraction;
import com.trading.utils.CC_card;
import com.trading.utils.CC_data;
import com.trading.utils.CC_data_item;
import com.trading.utils.KolvoDialog;
import com.trading.utils.Order;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ClientCardActivity extends TabActivity {

	private ClientCardsDao ccd;
	private Bundle extras;

	private TextView cc_numb, cc_numb_text, cc_date, cc_date_text, cc_cardname,
			cc_cardname_text, cc_clientname, cc_clientname_text;
	private CC_card cc_cd;
	private CC_data selectedcc_data;
	private ListView cc_data_listview;
	private ToggleButton cc_card_alltov, tb_CatAll;
	private EditText etPrim;
	private int cc_cdId;
	private Spinner spCardName, spgrTov;
	private Button bSendCard;
	private int poslw=0;

	private class OnReadyListener implements KolvoDialog.ReadyListener {
		@Override
		public void ready(Double kolvo, Double cena) {
			selectedcc_data.setOst(kolvo);
			selectedcc_data.setZakaz(cena);
			cc_cd.Add_cc_data(selectedcc_data);
			ClientCardsDao cd = new ClientCardsDao(ClientCardActivity.this);
			try {
				
			
			if (cc_cd.getisIschanged())
				cd.SaveCC_card(cc_cd);
			else
				cd.add_CC_data(selectedcc_data);
			} catch (Exception e) {
				Toast.makeText(ClientCardActivity.this, e.getMessage(), 10000).show();
				cc_cd = ccd.getCC_card(cc_cdId, cc_card_alltov
						.isChecked(), ((SpinnerDB) spgrTov
						.getSelectedItem()).id,tb_CatAll.isChecked());
			}
			fillTovars();
			
			cc_data_listview.setSelection(poslw);
			
		}
	}

	private void prepareDataView() {
		cc_numb = (TextView) findViewById(R.id.cc_numb);
		cc_numb_text = (TextView) findViewById(R.id.cc_numb_text);
		cc_date = (TextView) findViewById(R.id.cc_date);
		cc_date_text = (TextView) findViewById(R.id.cc_date_text);

		cc_cardname_text = (TextView) findViewById(R.id.cc_cardname_text);
		cc_clientname = (TextView) findViewById(R.id.cc_clientname);
		cc_clientname_text = (TextView) findViewById(R.id.cc_clientname_text);
		cc_card_alltov = (ToggleButton) findViewById(R.id.cc_card_alltov);
		tb_CatAll = (ToggleButton) findViewById(R.id.tb_CatAll);
		etPrim=(EditText)findViewById(R.id.etPrim);
			etPrim.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!cc_cd.getPrim().toString().equalsIgnoreCase(s.toString()))
				{
					cc_cd.setPrim(s.toString());
				ClientCardsDao cd = new ClientCardsDao(ClientCardActivity.this);
				try {
				 cd.SaveCC_card(cc_cd);
				} catch (Exception e) {
					Toast.makeText(ClientCardActivity.this, e.getMessage(), 10000).show();
				}
				}

				
			}
		});

		
		// /////////////////////////////////////////////////////////////////////////
		spCardName = (Spinner) findViewById(R.id.spCardName);

		ArrayList<SpinnerDB> items;
		items = new ArrayList<SpinnerDB>();
		ArrayAdapter<SpinnerDB> adapter = new ArrayAdapter<SpinnerDB>(this,
				R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		Cursor cursor = ccd.getClientCards();
		if (cursor.moveToFirst())
			do {
				items.add(new SpinnerDB(cursor.getInt(0), cursor.getString(1)));
			} while (cursor.moveToNext());
		cursor.close();
		spCardName.setAdapter(adapter);
		spCardName.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				try {
					int i=((SpinnerDB) spCardName.getSelectedItem()).id;
					
					cc_cd.setClientcard_id(i);
					ccd.SaveCC_card(cc_cd);
					
				} catch (Exception e) {
					Toast.makeText(ClientCardActivity.this, e.getMessage(),10000).show();
				} 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// /////////////////////////////////////////////////////////////////////
		bSendCard = (Button) findViewById(R.id.bSendCard);
		bSendCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (ordr.getCentral_id() > 0)
				// Toast.makeText(OrderActivity.this,
				// "Не могу повторно отправить!!!", 10000).show(); else
				if (cc_cd.getCentral_id() > 0)
					Toast.makeText(ClientCardActivity.this,
							"Не могу повторно отправить!!!", 10000).show();
				else {

					SendCard();
					int orderid = ccd.createOrderFromCard(cc_cd.getId());
					if (orderid > 0) {
						Intent intent = new Intent();
						intent.putExtra("_ID", orderid);
						intent.putExtra("typedoc", 1);
						intent.putExtra("typepay", 2);
						intent.setClass(ClientCardActivity.this,
								OrderActivity.class);
						finish();
						startActivity(intent);
					}
				}

			}
		});
		// /////////////////////////////////////////////////////////////////////////
		TovarsDao td = new TovarsDao(this);
		spgrTov = (Spinner) findViewById(R.id.spgrTov);
		try {

			spgrTov.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					cc_cd = ccd.getCC_card(cc_cdId, cc_card_alltov
							.isChecked(), ((SpinnerDB) spgrTov
							.getSelectedItem()).id,tb_CatAll.isChecked());
					fillTovars();
					

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		items = new ArrayList<SpinnerDB>();
		ArrayAdapter<SpinnerDB> tovadapter = new ArrayAdapter<SpinnerDB>(this,
				R.layout.simple_spinner_item, items);
		tovadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		Cursor tovcursor = td.getGroups();
		items.add(new SpinnerDB(0, "Все"));
		if (tovcursor.moveToFirst())
			do {
				items.add(new SpinnerDB(tovcursor.getInt(0), tovcursor
						.getString(1)));
			} while (tovcursor.moveToNext());
		tovcursor.close();
		spgrTov.setAdapter(tovadapter);
		spgrTov.setSelection(0);
		// ///////////////////////////////////*/
	}

	private void fillTovars() {
		CC_DataAdapter mAdapter = new CC_DataAdapter(ClientCardActivity.this,
				CC_DataAdapter.get_List_CC_Data(cc_cd.getCc_data()),
				R.layout.cc_data_row, new String[] { CC_data_item.TOVNAME,
						CC_data_item.OST, CC_data_item.ZAKAZ,
						CC_data_item.AVAIL, CC_data_item.TOVID }, new int[] { R.id.cc_data_tovname,
						R.id.cc_data_ost, R.id.cc_data_zakaz,
						R.id.cc_data_avail, R.id.cc_data_tovid });
		cc_data_listview.setAdapter(mAdapter);

	}

	private void fillData() {
		cc_numb.setText(String.valueOf(cc_cd.getId()));
		cc_date.setText(cc_cd.getDt().toString());

		cc_clientname.setText(cc_cd.getClient_name());

		etPrim.setText(cc_cd.getPrim().toString());
		// /////////////////////////////////////////////////////////////////////
		ArrayAdapter<?> myAdap = (ArrayAdapter<?>) spCardName.getAdapter();
		// sSpFirma.setSelection(-1);
		boolean x = false;

		for (int i = 0; i < myAdap.getCount(); i++) {
			int spinnerPosition = ((SpinnerDB) myAdap.getItem(i)).id;
			if (spinnerPosition == cc_cd.getClientcard_id()) {
				spCardName.setSelection(i);
				x = true;
				break;
			}
			if (!x) {
				spCardName.setSelection(0);
				cc_cd.setClientcard_id(((SpinnerDB) spCardName
						.getSelectedItem()).id);
			}

		}
		// /////////////////////////////////////////////////////////////////////

		cc_data_listview = (ListView) findViewById(R.id.cc_data_listView);
		cc_data_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				try {

					String s = "ssss";
					selectedcc_data = new CC_data(0, cc_cd.getId(), 
							Integer.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("position_id").toString()), 
							Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("ost")
									.toString()), Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("zakaz")
									.toString()), Double
							.valueOf(((java.util.HashMap) arg0
									.getItemAtPosition(position)).get("avail")
									.toString()), 
									((java.util.HashMap) arg0.getItemAtPosition(position)).get("tovname").toString(), 
									Integer.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("tovid").toString()));

					poslw=position;
					
					double tempkolvo=Double.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("ost").toString());
					if (tempkolvo==0.0)
						tempkolvo=1.0;
					KolvoDialog myDialog = new KolvoDialog(
							ClientCardActivity.this, 
							//Double.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("ost").toString()),
							tempkolvo,
							Double.valueOf(((java.util.HashMap) arg0.getItemAtPosition(position)).get("zakaz").toString()),
							new OnReadyListener());
					myDialog.show();
					myDialog.setTitle("Введите остаток и количество:");
					myDialog.settvKolvo("Остаток");
					myDialog.settvCena("Заказ");

				} catch (Exception e) {
					Toast.makeText(ClientCardActivity.this, e.getMessage(),
							10000).show();
				}
			}
		});
		// ///////////////////////////////////////////////////////////////////////////
		cc_card_alltov
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						cc_cd = ccd.getCC_card(cc_cdId, cc_card_alltov
								.isChecked(), ((SpinnerDB) spgrTov
								.getSelectedItem()).id,tb_CatAll.isChecked());
						fillTovars();
						
					}
				});
		// ////////////////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////////////////////////////
		tb_CatAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						cc_cd = ccd.getCC_card(cc_cdId, cc_card_alltov
								.isChecked(), ((SpinnerDB) spgrTov
								.getSelectedItem()).id,tb_CatAll.isChecked());
						fillTovars();
						
					}
				});
		// ////////////////////////////////////////////////////////////////

		fillTovars();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {

			// this.setTitle("Карта клиента");
			setContentView(R.layout.clientcard);
			extras = getIntent().getExtras();
			ccd = new ClientCardsDao(this);
			cc_cdId = extras.getInt("_ID");

			prepareDataView();
			if (extras.getInt("_ID") == 0) {
				cc_cd = ccd.getCC_card(0, false, 0, true);
				cc_cd.setClient_id(extras.getInt("partnerid"));
				cc_cd.setClient_name(extras.getString("partnername"));
				cc_cd.setClient_cat(extras.getString("partnercat"));
				cc_cd.setClientcard_id(((SpinnerDB) spCardName
						.getSelectedItem()).id);
				cc_cd.setId(0);
				cc_cdId = ccd.SaveCC_card(cc_cd);
				cc_card_alltov.setChecked(true);
			}
			cc_cd = ccd.getCC_card(cc_cdId, cc_card_alltov.isChecked(),
					((SpinnerDB) spgrTov.getSelectedItem()).id,tb_CatAll.isChecked());
			fillData();

			// ///////////////
			TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
			tabs.setup();

			TabHost.TabSpec spec = tabs.newTabSpec("tag1");

			spec.setContent(R.id.cc_tabview1);
			spec.setIndicator("Данные");
			tabs.addTab(spec);

			spec = tabs.newTabSpec("tag2");
			spec.setContent(R.id.cc_tabview2);
			spec.setIndicator("Товары");
			tabs.addTab(spec);

			tabs.setCurrentTab(0);
			tabs.getTabWidget().getChildAt(0).getLayoutParams().height = 40;
			tabs.getTabWidget().getChildAt(1).getLayoutParams().height = 40;
		} catch (Exception e) {
			Toast.makeText(ClientCardActivity.this, e.getMessage(), 10000)
					.show();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		ccd.checkEmpty(cc_cd);

	}

	private void SendCard() {
		DBInteraction dbitem = new DBInteraction(ParamsDao.getAgentId(this),1, ClientCardActivity.this);
		try {
			ClientCardsDao cd=new ClientCardsDao(this);
			int cc = dbitem.UploadCard(cc_cd.getId(), ClientCardActivity.this);
			 cc_cd.setCentral_id(cc);
			 cd.SaveCC_card(cc_cd);

					switch (cc) {
			case -1:
				Toast.makeText(ClientCardActivity.this,
						"Ошибка при работе с БД.", Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(ClientCardActivity.this,
						"Неверный XML. Обратитесь к администратору.",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				Toast.makeText(ClientCardActivity.this,
						"Заявка успешно отправлена.", Toast.LENGTH_SHORT)
						.show();
				// od.SaveOrder(ordr);
				break;
			}

		} catch (Exception e) {
			Toast.makeText(ClientCardActivity.this, e.getMessage(), 10000)
					.show();
		}
	}

}
