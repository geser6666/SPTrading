package com.trading.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.trading.R;

import com.trading.adapters.TovarsAdapter;
import com.trading.dao.TovarsDao;
import com.trading.db.*;
import com.trading.utils.KolvoDialog;
import com.trading.utils.TOrder;
import com.trading.utils.Tovar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TovarsListActivity extends ListActivity implements
		OnItemSelectedListener {
	TovarsAdapter myAdapter;
	Spinner grtovar;
	ToggleButton tbTovOnlyOst;
	private LinearLayout llFilter; 
	private ArrayList<SpinnerDB> items;
	private ArrayList<TOrder> tovars;

	protected Tovar selectedtovar;

	private Intent data;
	private Bundle extras;
	private int typeview;
	private int typedoc;
	private int idskidka=-1;
	private int idpartner = 0;
	private int poslw=0;

	public class OnReadyListener implements KolvoDialog.ReadyListener {
		@Override
		public void ready(Double kolvo, Double cena) {
			Intent intent = new Intent();
			intent.putExtra("idtov", selectedtovar.getId());
			intent.putExtra("name", selectedtovar.getName());
			intent.putExtra("ed_izm", selectedtovar.getEd_izm());
			intent.putExtra("kolvo", kolvo);
			intent.putExtra("cena", cena);
			// intent.putExtra("lastgridtov", ((SpinnerDB)
			// grtovar.getSelectedItem()).id);

			TovarsListActivity.this.setResult(RESULT_OK, intent);
			TovarsListActivity.this.finish();

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			TovarsDao td = new TovarsDao(this);
			setContentView(R.layout.tovars_list);
			super.onCreate(savedInstanceState);
			extras = getIntent().getExtras();
			if (extras != null) {
				if (extras.getInt("idpartner") != 0) {
					idpartner = extras.getInt("idpartner");
				}
				typedoc = extras.getInt("typedoc");
				idskidka=extras.getInt("idskidka");
				
			}

			grtovar = (Spinner) findViewById(R.id.grtovar_filter);
			
			
			llFilter=(LinearLayout)findViewById(R.id.ll_filter);
			if (extras != null) {
				//grtovar.setVisibility(View.INVISIBLE);
				//((TextView)findViewById(R.id.textView1)).setVisibility(View.INVISIBLE);
				llFilter.setVisibility(View.GONE);
			} else {
				grtovar.setOnItemSelectedListener(this);

				items = new ArrayList<SpinnerDB>();
				ArrayAdapter<SpinnerDB> adapter = new ArrayAdapter<SpinnerDB>(
						this, R.layout.simple_spinner_item, items);
				adapter
						.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
				Cursor cursor = td.getGroups();
				if (cursor.moveToFirst())
					do {
						items.add(new SpinnerDB(cursor.getInt(0), cursor
								.getString(1)));
					} while (cursor.moveToNext());
				cursor.close();
				grtovar.setAdapter(adapter);
				if (extras != null) {
					if (extras.getInt("lastgridtov") != -1) {

						ArrayAdapter<?> myAdap = (ArrayAdapter<?>) grtovar
								.getAdapter();
						for (int i = 0; i < myAdap.getCount(); i++) {
							int spinnerPosition = ((SpinnerDB) myAdap
									.getItem(i)).id;
							if (spinnerPosition == extras.getInt("lastgridtov")) {
								grtovar.setSelection(i);
								break;
							}
						}
					}

				}
			}
			// ////////////////////////////////
			tbTovOnlyOst = (ToggleButton) findViewById(R.id.tbTovOnlyOst);
			tbTovOnlyOst
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							myAdapter.setOnlyost(isChecked);
							TovarsListActivity.this.setListAdapter(null);
							TovarsListActivity.this.setListAdapter(myAdapter);

						}
					});
			// ///////////////////////////////
			if (extras != null)
				if (extras.get("typedoc") != null)
					typeview = extras.getInt("typedoc");
				else
					typeview = 3;
			else
				typeview = 3;

			// myAdapter = new TovarsAdapter(this, ((SpinnerDB)
			// grtovar.getSelectedItem()).id,typeview, true );
			// this.setListAdapter(myAdapter);

			// tovars = new ArrayList<TOrder>();

			if (extras.get("searchQuery") != null)
			{
				myAdapter = new TovarsAdapter(this, extras.getInt("lastgridtov"),
						idskidka,typeview, tbTovOnlyOst.isChecked(),extras.get("searchQuery").toString());
			}else
				myAdapter = new TovarsAdapter(this, extras.getInt("lastgridtov"),
					idskidka,typeview, tbTovOnlyOst.isChecked(),"");
			this.setListAdapter(myAdapter);
			
			if (extras != null) 
			{
				if (extras.getInt("lasttov") != 0) 
				{
					for (int i=0;i<myAdapter.getCount();i++)
					{
						if (myAdapter.getItem(i).getId()==extras.getInt("lasttov"))
						{
							this.getListView().setSelection(i);
							break;
							
						}
					}
				    	
				}
			}
			
			tovars = new ArrayList<TOrder>();
			

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		double cenands;
		HashMap<String, Double> hm = new HashMap<String, Double>();
		selectedtovar = (Tovar) l.getItemAtPosition(position);
		TovarsDao td = new TovarsDao(this);
		selectedtovar = td.getTovar(selectedtovar.getId());
		cenands = 0;
		if (idpartner != 0 && typedoc != 0) {
			hm = td.getLastCena2(idpartner, selectedtovar.getId(), typedoc);
			// cenands = td.getLastCena(idpartner, selectedtovar.getId()
			// ,typedoc);
			cenands = hm.get("newcena");
		}
		if (typedoc==1 )
		{
			switch (idskidka) {
			case 1:
				cenands = selectedtovar.getSkidka1();
				break;
			case 2:
				cenands = selectedtovar.getSkidka2();
				break;

			default:
				cenands = selectedtovar.getCenands();
				break;
			}
		}
		if (cenands == 0)
			cenands = selectedtovar.getCenands();
		try {
			if (extras != null) {
				KolvoDialog myDialog = new KolvoDialog(TovarsListActivity.this,
						(double) 1, cenands, new OnReadyListener());
				myDialog.show();

				if (Double.compare(hm.get("oldcena"), hm.get("newcena")) != 0) {
					final AlertDialog dlg = new AlertDialog.Builder(
							TovarsListActivity.this).setTitle(
							"Новая цена с учетом изменения розницы").
					// setView(v).
							create();

					dlg.setCanceledOnTouchOutside(true);
					dlg
							.setButton(
									DialogInterface.BUTTON_POSITIVE,
									this.getText(android.R.string.ok),
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
					dlg.show();

					// Toast.makeText(TovarsListActivity.this,
					// "Новая цена с учетом изменения розницы", 80000).show();
				}

			}

		} catch (Exception e) {
			String s = e.getMessage();
			Toast.makeText(TovarsListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		myAdapter = new TovarsAdapter(this, ((SpinnerDB) grtovar.getSelectedItem()).id,idskidka, typeview, tbTovOnlyOst.isChecked(),"");
		this.setListAdapter(myAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}
