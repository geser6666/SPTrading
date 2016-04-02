package com.trading.activity;


import java.util.Calendar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.trading.R;

public class ReferenceListActivity extends ListActivity {
	/** Called when the activity is first created. */
	private final static int REFERENCE_PARTNERS = 0;
	private final static int REFERENCE_SKLAD = 1;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			// setContentView(R.layout.main_menu);
			setListAdapter(ArrayAdapter.createFromResource(this,
					R.array.itemReference, R.layout.menu_list_item));
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}
		
		try {
			
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		} 

	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		switch (position) {
		case REFERENCE_PARTNERS:
			try {
				Intent intent= new Intent();
				intent.putExtra("_ID", 0);
				int week_day=Calendar.getInstance().getTime().getDay();
				intent.putExtra("week_day", 0);
				intent.putExtra("selectmode", false);

				intent.setClass(this, PartnersListActivity.class);
				
//				startActivityForResult(intent1, IDM_EDIT);

				startActivity(intent);	
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
			} 
			
			break;
			
		case REFERENCE_SKLAD:
			try {
				startActivity(new Intent(this,TovarsListActivity.class));	
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
			} 
			
			break;

			default:

			Toast.makeText(this, "В разработке.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}