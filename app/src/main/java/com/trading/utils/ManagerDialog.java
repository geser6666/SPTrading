package com.trading.utils;

import com.trading.R;
import com.trading.utils.ConfirmDialog.ReadyListener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ManagerDialog extends Dialog implements OnClickListener, OnTouchListener{
	private int managerid;
	private String managername;
	private ReadyListener readyListener;
    EditText etManagerid;
    EditText etSurname;
    Button btOk;

    public ManagerDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_manager);
        
        setCancelable(false);
        etManagerid = (EditText) findViewById(R.id.eT_managerId);
        
        etSurname = (EditText) findViewById(R.id.eT_Surname);
        setTitle("Введите данные пользователя.");
        etManagerid.setInputType(InputType.TYPE_CLASS_NUMBER);
        btOk = (Button) findViewById(R.id.btOk);
        try {
        	btOk.setOnClickListener(new OKListener());
		} catch (Exception e) {

			String s=e.getMessage();
		}
		etManagerid.setText(String.valueOf(this.managerid));
		etSurname.setText(this.managername);
        
	}
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
			managerid=Integer.valueOf(etManagerid.getText().toString());	
		} catch (NumberFormatException e) {
			managerid=0;
		} 
		try {
			managername=String.valueOf(etSurname.getText().toString());	
		} catch (NumberFormatException e) {
			cena=0.0;
		} 
		
        
		ManagerDialog.this.dismiss();
		readyListener.ready(managerid,managername );
        
    }
}
	public ManagerDialog(Context context, int managerid, String managername,ReadyListener readyListener) {
		super(context);
		
		this.managerid = managerid;
		this.managername=managername;
		this.readyListener = readyListener;
	}
	  public interface ReadyListener {
	        void ready(int managerid, String managername);
	    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
