package com.trading.activity;

import java.sql.Date;
import java.util.Calendar;

import com.trading.R;
import com.trading.adapters.ClientCardDayListAdapter;
import com.trading.dao.ClientCardsDao;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class ClientCardDayListActivity extends ListActivity{
 static final int DATE_DIALOG_ID = 0;
 private int mYear;
 private int mMonth;
 private int mDay;

private int partnerId;
private ListView lw;
private Date filterdate;
private TextView etDate;
private Button btn_setdate;
  ClientCardsDao ccd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			setContentView(R.layout.cc_cardlist);
			super.onCreate(savedInstanceState);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		ccd= new ClientCardsDao(ClientCardDayListActivity.this);
		etDate=(TextView)findViewById(R.id.twDate);
		
		filterdate=new Date(Calendar.getInstance().getTimeInMillis());
		Calendar c;
		c= Calendar.getInstance();
		c.setTime(filterdate);
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		
		try {
			
			
			
			// setContentView(R.layout.main_menu);
			Cursor cc=ccd.get_cards(filterdate);
			/*SimpleCursorAdapter ca = new SimpleCursorAdapter(ClientCardDayListActivity.this,
					R.layout.three_col_item, cc,
					new String[]{"clientcardname","clientname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 }); 
			*/
			ClientCardDayListAdapter ca = new ClientCardDayListAdapter(ClientCardDayListActivity.this,
					R.layout.three_col_item, cc,
					new String[]{"clientcardname","clientname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 });
			
			 setListAdapter(ca);
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
			t.show();
		}
		
		btn_setdate=(Button)findViewById(R.id.btn_setdate);
		btn_setdate.setOnClickListener(new OnClickListener()  {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		int uu=((SimpleCursorAdapter)l.getAdapter()).getCursor().getInt(((SimpleCursorAdapter)l.getAdapter()).getCursor().getColumnIndex("_id"));
		//Toast.makeText(ClientCardListActivity.this, String.valueOf(uu), 10000).show();
		Intent intent = new Intent();
		intent.putExtra("_ID", uu);
		intent.setClass(this, ClientCardActivity.class);
//		setResult(RESULT_OK, intent);
//		ClientCardListActivity.this.finish();
		startActivityForResult(intent, 1) ;

		
		
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
				ClientCardDayListActivity.this.setListAdapter(null);
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

	  			      
	  			      Cursor cc=ccd.get_cards(filterdate);
	  			    cc.moveToFirst();
	  			//	Toast.makeText(this, String.valueOf(cc.getCount()), 10000).show();
	  				
/*	  				SimpleCursorAdapter ca = new SimpleCursorAdapter(ClientCardDayListActivity.this,
	  						R.layout.three_col_item, cc,
	  						new String[]{"clientcardname","clientname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 }); 
	*/
	  				ClientCardDayListAdapter ca = new ClientCardDayListAdapter(ClientCardDayListActivity.this,
	  						R.layout.three_col_item, cc,
	  						new String[]{"clientcardname","clientname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 });

	  				setListAdapter(ca);

	  			      
	  			      
	  			      
//	  			      myAdapter.SetPeriod(d, Date.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString()));
	//  			      OrdersListActivity.this.setListAdapter(myAdapter);
	  			} catch (Exception e) {
	  				// TODO: handle exception
	  			} 

	    }

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case 1:
			      Cursor cc=ccd.get_cards(filterdate);
	  			    cc.moveToFirst();

				ClientCardDayListAdapter ca = new ClientCardDayListAdapter(ClientCardDayListActivity.this,
  						R.layout.three_col_item, cc,
  						new String[]{"clientcardname","clientname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 });

  				setListAdapter(ca);
				
				break;

			default:
				break;
			}
		}

}
