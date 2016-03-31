package com.trading.activity;

import java.util.ArrayList;
import java.util.Map;

import com.trading.R;
import com.trading.activity.TovarsListActivity.OnReadyListener;
import com.trading.db.DB;
import com.trading.utils.ManagerDialog;
import com.trading.utils.ServerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ServersListActivity extends Activity implements  android.widget.RadioGroup.OnCheckedChangeListener
{
	private ServerDialog servD;
	public ServerDialog getDialog(){return  servD;}
	private ArrayList<String> servs;

	private class OnReadyListener implements ServerDialog.ReadyListener {

		@Override
		public void ready(String servername, String serverurl,Boolean editable) {
			try {
				PreferenceManager.getDefaultSharedPreferences(ServersListActivity.this).edit().putString("serv_" + servername, serverurl).commit();

				fillRadioGroup(rg_servers, getServs());

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		
	}

	private RadioGroup rg_servers;


	private ArrayList<String> getServs() {
		ArrayList<String> ar_servers;
		ar_servers = null;
		try {
			Map<String, ?> allprefs = PreferenceManager.getDefaultSharedPreferences(this).getAll();
			//allprefs.keySet().toArray()[0]

			ar_servers = new ArrayList<String>();
			for (int i = 0; i < allprefs.keySet().size(); i++) {

				if (String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(), 0, 5).equals("serv_")) {
					ar_servers.add(String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(), 5, allprefs.keySet().toArray()[i].toString().length() - 5));
				}
			}
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}
		return ar_servers;
	}

	private  void fillRadioGroup(RadioGroup rg, ArrayList<String> ar)
	{
		CharSequence checkedname;
		try
		{
			if (rg.getCheckedRadioButtonId()==-1 )
			{
				checkedname="";
			}
			else
			{
				if ((RadioButton) findViewById(rg.getCheckedRadioButtonId())!=null)
				checkedname = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText();
				else
					checkedname="";
			}
			rg.removeAllViews();
			for (int i=0;i<ar.size(); i++)
			{
				RadioButton new_rb=new RadioButton(this);
				new_rb.setText(ar.get(i).toString());
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT,
						RadioGroup.LayoutParams.WRAP_CONTENT);

				String urlString =PreferenceManager.getDefaultSharedPreferences(this).getString("server", "Сервер не задан!!Обновлений не будет!!!");

				rg.addView(new_rb, 0, layoutParams);
				rg.setOnCheckedChangeListener(this);
				if (urlString.equals(PreferenceManager.getDefaultSharedPreferences(this).getString("serv_" + ar.get(i).toString(), "Сервер не задан!!Обновлений не будет!!!")))
				{
					new_rb.setChecked(true);
					checkedname=new_rb.getText();
				}
			}
			if (!checkedname.equals(""))
			{
				for (int i=0;i<rg.getChildCount();i++)
				{
					if (((RadioButton)rg.findViewById(rg.getChildAt(i).getId())).getText().equals(checkedname)) {
						((RadioButton) rg.getChildAt(i)).setChecked(true);
						checkedname=((RadioButton) rg.getChildAt(i)).getText();
						break;
					}
				}

			}
			if ((checkedname=="") &&(ar.size()>0))
				((RadioButton) rg.getChildAt(0)).setChecked(true);


		}catch (Exception e)
		{
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servers);		
		try {
			rg_servers = (RadioGroup) findViewById(R.id.rg_servers);
			ArrayList<String> ar= new  ArrayList<String>();
			ar=getServs();
			fillRadioGroup(rg_servers,ar);

		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.serversmenu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selectionaaaa

	    switch (item.getItemId()) {
	    case R.id.new_server:
	         servD = new ServerDialog(this,"","", true, new OnReadyListener());
	         servD.show();
	        return true;
	    case R.id.edit_server:
	    	String sn="";
	    	String su="";
	    	try {
	    		sn=((RadioButton)findViewById(rg_servers.getCheckedRadioButtonId())).getText().toString();
				su=PreferenceManager.getDefaultSharedPreferences(this).getString("serv_" + sn, "Сервер не задан!!Обновлений не будет!!!");
				servD = new ServerDialog(this,sn,su, false, new OnReadyListener());
				servD.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
	     /*    servD = new ServerDialog(this,
	        		 sn ,
	        		 su, false, new OnReadyListener());
	         servD.show();
			servD.hide();
	       */
	        return true;
	    case R.id.del_server:
			 sn=((RadioButton)findViewById(rg_servers.getCheckedRadioButtonId())).getText().toString();
			PreferenceManager.getDefaultSharedPreferences(ServersListActivity.this).edit().remove("serv_" + sn).commit();
			rg_servers.removeView(((RadioButton) findViewById(rg_servers.getCheckedRadioButtonId())));

			fillRadioGroup(rg_servers, getServs());
			return true;

	    default:

	        return super.onOptionsItemSelected(item);

	    }

	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		Map<String, ?> allprefs=	PreferenceManager.getDefaultSharedPreferences(this).getAll();
		for (int i=0;i<allprefs.keySet().size();i++)
		{
			
		  if (String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(),0,5).equals("serv_"))
		  {
			 //servs.add(String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(),5 , allprefs.keySet().toArray()[i].toString().length()-5)) ;
			 //new_rb.setText(String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(),5 , allprefs.keySet().toArray()[i].toString().length()-5));
//			  group.findViewById(group.getCheckedRadioButtonId())
			  if (group.findViewById(group.getCheckedRadioButtonId())!=null) {
				  if ((String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(), 5, allprefs.keySet().toArray()[i].toString().length() - 5).toString().
						  equals(((RadioButton) group.findViewById(group.getCheckedRadioButtonId())).getText().toString()))
						  ) {
					  String value = allprefs.values().toArray()[i].toString();
					  PreferenceManager.getDefaultSharedPreferences(ServersListActivity.this).edit().putString("server", value).commit();
					  value = null;
				  }
			  }
		  }
		}		
//		PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString("server",value).commit();

	}
	
}
