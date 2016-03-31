package com.trading.activity;

import com.trading.R;
import com.trading.dao.ClientCardsDao;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class ClientCardListActivity extends ListActivity{
private int partnerId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			ClientCardsDao ccd= new ClientCardsDao(ClientCardListActivity.this);
			// setContentView(R.layout.main_menu);
			Bundle extras = getIntent().getExtras();
			partnerId = extras.getInt("partnerid");
			this.setTitle(extras.getString("partnername"));
			Cursor cc=ccd.get_cards(partnerId);
			SimpleCursorAdapter ca = new SimpleCursorAdapter(ClientCardListActivity.this,
					R.layout.three_col_item, cc,
					new String[]{"dt", "clientcardname", "_id"}, new int[]{R.id.text1,R.id.text2, R.id.text3 }); 
			
			setListAdapter(ca);
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
			t.show();
		}
		
		
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
		startActivity(intent);

		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			ClientCardsDao ccd= new ClientCardsDao(ClientCardListActivity.this);
			// setContentView(R.layout.main_menu);
			Bundle extras = getIntent().getExtras();
			partnerId = extras.getInt("partnerid");
			this.setTitle(extras.getString("partnername"));
			Cursor cc=ccd.get_cards(partnerId);
			SimpleCursorAdapter ca = new SimpleCursorAdapter(ClientCardListActivity.this,
					R.layout.two_col_item, cc,
					new String[]{"dt", "clientcardname"}, new int[]{R.id.text1,R.id.text2 }); 
			
			
			setListAdapter(ca);
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
			t.show();
		}
	}
	

}
