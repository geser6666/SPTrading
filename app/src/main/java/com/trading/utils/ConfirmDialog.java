package com.trading.utils;

import com.trading.R;

import android.R.string;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmDialog extends Dialog {
	private ReadyListener readyListener;
	private String confirmtext;
	private Button btOk;
	private Button btCancel;

	public ConfirmDialog(Context context, String confirmtext,
			ReadyListener redyListener) {
		super(context);
		this.readyListener = readyListener;
		this.confirmtext=confirmtext;
		// TODO Auto-generated constructor stub

	}

	public interface ReadyListener {
		void ready(boolean yesno);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_confirm);
        setTitle(confirmtext);
        btOk = (Button) findViewById(R.id.dlgKolvo_btnOk);
        btCancel = (Button) findViewById(R.id.dlgKolvo_btnCancel);
        try {
        	btOk.setOnClickListener(new OKListener());
        	btCancel.setOnClickListener(new CANCELListener());
		} catch (Exception e) {

			String s=e.getMessage();
		}

	}

	private class OKListener implements android.view.View.OnClickListener
	{
        public OKListener() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            
			ConfirmDialog.this.dismiss();
			readyListener.ready(true);
		
		}
		
	}
private class CANCELListener implements android.view.View.OnClickListener {
    	
        public CANCELListener() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
        public void onClick(View v) {
			readyListener.ready(false);
			ConfirmDialog.this.dismiss();
		    
        }
    }

	
}
