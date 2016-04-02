package com.trading.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FilterQueryProvider;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.trading.R;
import com.trading.adapters.PartnersAdapter;
import com.trading.adapters.PartnersAdapter2;
import com.trading.utils.ClientCard;
import com.trading.utils.Partner;

public class PartnersListActivity extends ListActivity implements FilterQueryProvider{
	//PartnersAdapter myAdapter;
	private ToggleButton tb_mar;

	private PartnersAdapter2 myAdapter = null;
	private String notemptyfield="";
	private int week_day=0;

	private boolean selectmode=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			setContentView(R.layout.partners_list);
			super.onCreate(savedInstanceState);
			tb_mar=(ToggleButton)findViewById(R.id.tb_mar);
			tb_mar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked)
					myAdapter = new PartnersAdapter2(PartnersListActivity.this, R.layout.partner_row, 
								PartnersAdapter2.getdata(PartnersListActivity.this, "",notemptyfield, week_day));
					else
						myAdapter = new PartnersAdapter2(PartnersListActivity.this, R.layout.partner_row, 
								PartnersAdapter2.getdata(PartnersListActivity.this, "",notemptyfield, 0));
			        
					PartnersListActivity.this.setListAdapter(myAdapter);
				}
			});
/*
			 Intent intent = getIntent();
			    //Проверяем тип Intent
			    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			      //Берем строку запроса из экстры
			      String query = intent.getStringExtra(SearchManager.QUERY);
			      //Выполняем поиск
			      myAdapter = new PartnersAdapter(this,query);
			    }else
				      myAdapter = new PartnersAdapter(this,"");
				    this.setListAdapter(null);
				this.setListAdapter(myAdapter);
	*/
			//m_partners = new ArrayList<Partner>();
			Bundle extras = getIntent().getExtras();
			//if (extras.getBoolean("selectmode")!=null)
				selectmode=extras.getBoolean("selectmode");
			tb_mar.setChecked(false);
			if (extras!= null)
			{
				if (extras.getString("notemptyfieldname")!=null)
					notemptyfield=extras.getString("notemptyfieldname");
				if (extras.getInt("week_day")!=0)
				{
					week_day=extras.getInt("week_day");
					tb_mar.setChecked(true);
				}
			}
	      //  this.myAdapter = new PartnersAdapter2(this, R.layout.partner_row, PartnersAdapter2.getdata(this, "",notemptyfield, week_day));
	      //  setListAdapter((ListAdapter) this.myAdapter);
			// setContentView(R.layout.orders_list);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
if (selectmode)
		{
			Intent intent = new Intent();
			intent.setAction(ACTIVITY_SERVICE);
			intent.putExtra("_id", myAdapter.getItem(position).id);
			intent.putExtra("name", myAdapter.getItem(position).name);
			intent.putExtra("idskidka", myAdapter.getItem(position).idskidka);
			intent.putExtra("cat", myAdapter.getItem(position).category);
			setResult(RESULT_OK, intent);
			PartnersListActivity.this.finish();
			PartnersListActivity.this.finish();
		} else {
			try {
					Intent intent = new Intent(this, PartnerCardActivity.class);
					intent.putExtra("_ID", myAdapter.getItem(position).id);
					startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
		}
	}

	@Override
	public Cursor runQuery(CharSequence sequence) {
		// TODO Auto-generated method stub
		String s=sequence.toString();
		return null;
	}



	
	
}
