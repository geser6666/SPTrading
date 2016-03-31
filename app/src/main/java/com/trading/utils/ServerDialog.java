package com.trading.utils;

import com.trading.R;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

public class ServerDialog  extends Dialog implements OnClickListener, OnTouchListener{
	private String servername;
	private String serverurl;



	public void setEditable(Boolean e) {

		if (e == true) {
			etServerName.setEnabled(true);

		} else {
			etServerName.setEnabled(false);
		}
	}

	private Boolean editable;
	private ReadyListener  readyListener;
    EditText etServerName;
    EditText etServerUrl;
    Button btOk;
	Button btCancel;
    
    private class OKListener implements android.view.View.OnClickListener {
    	
        public OKListener() {
    		super();
    		// TODO Auto-generated constructor stub
    	}

    	@Override
        public void onClick(View v) {
    		Double kolvo;
    		Double cena;
    		try {
    			servername=String.valueOf(etServerName.getText().toString());	
    		} catch (NumberFormatException e) {
    			
    		} 
    		try {
    			serverurl=String.valueOf(etServerUrl.getText().toString());	
    		} catch (NumberFormatException e) {
    			
    		} 
    		
            
    		ServerDialog.this.dismiss();
    		readyListener.ready(servername,serverurl, editable );
            
        }
    }

	public ServerDialog(Context context,String servname, String servurl,Boolean e,ReadyListener rl) {
		super(context);
		this.servername=servname;
		this.serverurl=servurl;
		this.editable=e;
		this.readyListener=rl;

		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_server);
        
        setCancelable(false);
        etServerName = (EditText) findViewById(R.id.e_servname);
        
        etServerUrl = (EditText) findViewById(R.id.e_servurl);
        setTitle("Введите данные пользователя.");
        etServerName.setInputType(InputType.TYPE_CLASS_TEXT);
        btOk = (Button) findViewById(R.id.btn_ok);
        try {
        	btOk.setOnClickListener(new OKListener());
		} catch (Exception e) {

			String s=e.getMessage();
		}
		btCancel = (Button) findViewById(R.id.btn_cancel);
		try {
			btCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ServerDialog.this.dismiss();
				}
			});
		} catch (Exception e) {

			String s=e.getMessage();
		}
		etServerUrl.setText(this.serverurl);
		etServerName.setText(this.servername);
		setEditable(editable);
        
	}	
	  public interface ReadyListener {
	        void ready(String servername, String serverurl, Boolean editable);
	    }

}
