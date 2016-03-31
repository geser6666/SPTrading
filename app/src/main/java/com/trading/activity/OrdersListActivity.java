package com.trading.activity;


import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

import com.trading.R;
import com.trading.adapters.OrdersAdapter;
import com.trading.dao.OrdersDao;
import com.trading.db.DB;
import com.trading.utils.Order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OrdersListActivity extends ListActivity /*implements OnItemLongClickListener */{
	
	static final int IDM_OPEN=1;
	static final int IDM_FILTER=2;
	static final int IDM_DEL=3;
	private Bundle extras;	
	static final int DATE_DIALOG_ID = 0;
	  private int mYear;
	  private int mMonth;
	  private int mDay;

	  
	  private int clientid=0;
	  private String clientname="Все";


	private Date filterdate;
	private TextView etDate, tvOrder_central_id, tv_partnerName; 

	private ToggleButton tbAllClosed, tbZayavkaNakl;
	private Button btDateChange;
	public DB dba;
	OrdersAdapter myAdapter;
private int causedactivity;
	
@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		try {
			Intent intent= new Intent();
			intent.putExtra("_ID", ((Order)l.getItemAtPosition(position)).getId());
			intent.putExtra("typedoc", ((Order)l.getItemAtPosition(position)).getTypedoc());
			intent.putExtra("typepay", ((Order)l.getItemAtPosition(position)).getTypepay());			
			intent.setClass(this, OrderActivity.class);
			startActivityForResult(intent, 5);

			
//			startActivity(intent);	
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, e.getMessage(),10000);
		} 
	}
@Override
protected Dialog onCreateDialog(int id) {
    switch (id) {
    case DATE_DIALOG_ID:
    	
        return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
    }
    return null;
}

private DatePickerDialog.OnDateSetListener mDateSetListener =
    new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			OrdersListActivity.this.setListAdapter(null);
			  mYear=year;
		      mMonth=monthOfYear;
		      mDay=dayOfMonth;    
			updateDisplay();
			setDateAdapter();
		
		
	           
		}
	};
	   // updates the date we display in the TextView
    private void updateDisplay() {
    
    	etDate.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
            .append(mYear).append("-")        
            .append(mMonth + 1).append("-")
            .append(mDay).append(""));
    }
  private void setDateAdapter()
  {
	
      	try {
				  
			      String s=String.valueOf(mYear)+"-"+String.valueOf(mMonth+1)+"-"+String.valueOf(mDay);
			      Date d=Date.valueOf(s);
			      filterdate=d;
			      
			      myAdapter.SetPeriod(d, Date.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString()));
			      OrdersListActivity.this.setListAdapter(myAdapter);
			} catch (Exception e) {
				// TODO: handle exception
			} 

  }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
		setContentView(R.layout.orders_list);
		super.onCreate(savedInstanceState);

		 
 

		btDateChange=(Button)findViewById(R.id.btDateChange);
		btDateChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
				
			}
		});
		
		etDate=(TextView)findViewById(R.id.twFilterDate);
		
		tvOrder_central_id=(TextView)findViewById(R.id.order_central_id);
		
		filterdate=new Date(Calendar.getInstance().getTimeInMillis());
		Calendar c;
		c= Calendar.getInstance();
		c.setTime(filterdate);
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		
		
		tbAllClosed=(ToggleButton)findViewById(R.id.tbAllClosed);
		tbAllClosed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				myAdapter.setOnlyOpen(isChecked);
				OrdersListActivity.this.setListAdapter(null);
				OrdersListActivity.this.setListAdapter(myAdapter);
			}
		});
		tbZayavkaNakl=(ToggleButton)findViewById(R.id.tbZayavkaNakl);
		tbZayavkaNakl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
					myAdapter.setTypedocis(2);
				else
					myAdapter.setTypedocis(1);
				OrdersListActivity.this.setListAdapter(null);
				OrdersListActivity.this.setListAdapter(myAdapter);
			}
		});
    	/*c=Calendar.getInstance();
    	c.setTime(filterdate);
    	c.add(Calendar.DAY_OF_MONTH, 1);
    	Date sqlDate = new Date(c.getTime().getTime());
        */
		int temptd;
    	if (tbZayavkaNakl.isChecked())
    		temptd=2;
    	else
    		temptd=1;
    		
		myAdapter = new OrdersAdapter(this, tbAllClosed.isChecked(),temptd,filterdate, filterdate, clientid);
		this.setListAdapter(myAdapter);
		
		//this.getListView().setOnItemLongClickListener(this);
		
		tv_partnerName=(TextView)findViewById(R.id.tv_Partnername);

		tv_partnerName.setText(clientname);
		tv_partnerName.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				  clientid=0;
				  setClientname("Все");
				  myAdapter.setPartnerId(clientid);
				  OrdersListActivity.this.setListAdapter(myAdapter);

				return true;
			}
		});
		
		tv_partnerName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("_ID", 0);
				intent.setClass(OrdersListActivity.this, PartnersListActivity.class);
				causedactivity = 1;
				startActivityForResult(intent, 6);
			}
		});
		
		
		registerForContextMenu(this.getListView() );
			// setContentView(R.layout.orders_list);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), 10000);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 5:

			break;
		case 6:
			if (data != null) {
				extras = data.getExtras();
				if (extras != null) {
					if (extras.getInt("_id") != 0) {
						this.clientid = extras.getInt("_id");
						this.setClientname(extras.getString("name"));
					}
				}
			}
			break;

		default:
			break;
		}

		int temptd;
    	if (tbZayavkaNakl.isChecked())
    		temptd=2;
    	else
    		temptd=1;
    	
		myAdapter = new OrdersAdapter(this, tbAllClosed.isChecked(),temptd,filterdate, filterdate, clientid);
		this.setListAdapter(myAdapter);
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
		tv_partnerName.setText(this.clientname);
	}
	/*
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		/*  clientid=((Order)arg0.getItemAtPosition(position)).getClient_id();
		  setClientname(((Order)arg0.getItemAtPosition(position)).getClient_name());
		  myAdapter.setPartnerId(clientid);
		  this.setListAdapter(myAdapter);
		  
		return true;
		
		
		
	}*/
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		try {
			
		
		menu.add(Menu.NONE, IDM_OPEN, Menu.NONE, "Open"); 
		menu.add(Menu.NONE, IDM_FILTER, Menu.NONE, R.string.FILTER); 
		menu.add(Menu.NONE, IDM_DEL, Menu.NONE, R.string.DEL); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case IDM_OPEN:
			this.onListItemClick(this.getListView(), (View) this.getListView().getParent() , info.position , info.id);
			
			break;
		case IDM_FILTER:
			
			  clientid=((Order)this.getListView().getAdapter().getItem(info.position)).getClient_id();
			  setClientname(((Order)this.getListView().getAdapter().getItem(info.position)).getClient_name());
			  myAdapter.setPartnerId(clientid);
			  this.setListAdapter(myAdapter);
			  
			break;
		case IDM_DEL:
			try {
				OrdersDao od =new OrdersDao(this);
				od.DelOrder(((Order)this.getListView().getAdapter().getItem(info.position)).getId());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return super.onContextItemSelected(item);
		}
		
		return true;

	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onContextMenuClosed(menu);
	}



}
