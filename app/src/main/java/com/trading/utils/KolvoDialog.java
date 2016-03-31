package com.trading.utils;



import com.trading.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KolvoDialog extends Dialog implements OnClickListener, OnTouchListener{

	private Double kolvo;
	private Double cena;

	private String name;
    private ReadyListener readyListener;
    TextView tvKolvo;
    TextView tvCena;
    EditText etKolvo;
    EditText etCena;
    Button btOk;
    Button btCancel;

    
    private Button mBtQtyPoint;
	private Vibrator mVibrator;
	private TextView mTvMessage;
	private Bundle mParams;
	private StringBuffer mQtyBuffer;

    

	public KolvoDialog(Context context, Double kolvo, Double cena,ReadyListener readyListener) {
		super(context);
		
		this.kolvo = (double)(Math.round(kolvo*100))/100;
		this.cena = (double)(Math.round(cena*100))/100;
		this.readyListener = readyListener;
	}

	public KolvoDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

    public interface ReadyListener {
        void ready(Double kolvo, Double cena);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_kolvo);
        
        tvKolvo = (TextView) findViewById(R.id.textView1);
        tvCena = (TextView) findViewById(R.id.textView2);
        
        setTitle("Введите кол-во и цену ");
        etKolvo = (EditText) findViewById(R.id.dlgKolvo_kolvo);
        etKolvo.setText(this.kolvo.toString());
        //etKolvo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etKolvo.setInputType(InputType.TYPE_NULL);
        etKolvo.setCursorVisible(false);
        etKolvo.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				mQtyBuffer = new StringBuffer();
	
			}
		});
        etCena = (EditText) findViewById(R.id.dlgKolvo_cena);
        etCena.setText(this.cena.toString());
        //etCena.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etCena.setInputType(InputType.TYPE_NULL);
        etCena.setCursorVisible(false);
        etCena.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				mQtyBuffer = new StringBuffer();
			}
		});
        
        btOk = (Button) findViewById(R.id.dlgKolvo_btnOk);
        btCancel = (Button) findViewById(R.id.dlgKolvo_btnCancel);
        try {
        	btOk.setOnClickListener(new OKListener());
        	btCancel.setOnClickListener(new CANCELListener());
		} catch (Exception e) {

			String s=e.getMessage();
		}
        
		mQtyBuffer = new StringBuffer();

		mBtQtyPoint = (Button) findViewById(R.id.btQtyPoint); 
				
		
		findViewById(R.id.btQty0).setOnClickListener(this);
		findViewById(R.id.btQty1).setOnClickListener(this);
		findViewById(R.id.btQty2).setOnClickListener(this);
		findViewById(R.id.btQty3).setOnClickListener(this);
		findViewById(R.id.btQty4).setOnClickListener(this);
		findViewById(R.id.btQty5).setOnClickListener(this);
		findViewById(R.id.btQty6).setOnClickListener(this);
		findViewById(R.id.btQty7).setOnClickListener(this);
		findViewById(R.id.btQty8).setOnClickListener(this);
		findViewById(R.id.btQty9).setOnClickListener(this);
		findViewById(R.id.btQtyC).setOnClickListener(this);
		findViewById(R.id.btQtyPoint).setOnClickListener(this);
		findViewById(R.id.btBackspace).setOnClickListener(this);

		
		
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
				kolvo=Double.valueOf(etKolvo.getText().toString());	
			} catch (NumberFormatException e) {
				kolvo=0.0;
			} 
			try {
				cena=Double.valueOf(etCena.getText().toString());	
			} catch (NumberFormatException e) {
				cena=0.0;
			} 
			
            
			KolvoDialog.this.dismiss();
			readyListener.ready(kolvo,cena);
            
        }
    }
    private class CANCELListener implements android.view.View.OnClickListener {
    	
        public CANCELListener() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
        public void onClick(View v) {
			KolvoDialog.this.dismiss();
		    
        }
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btQty0:
			if (mQtyBuffer.length() > 0) mQtyBuffer.append("0");
			break;
		case R.id.btQty1:
			mQtyBuffer.append("1");
			break;
		case R.id.btQty2:
			mQtyBuffer.append("2");
			break;
		case R.id.btQty3:
			mQtyBuffer.append("3");
			break;
		case R.id.btQty4:
			mQtyBuffer.append("4");
			break;
		case R.id.btQty5:
			mQtyBuffer.append("5");
			break;
		case R.id.btQty6:
			mQtyBuffer.append("6");
			break;
		case R.id.btQty7:
			mQtyBuffer.append("7");
			break;
		case R.id.btQty8:
			mQtyBuffer.append("8");
			break;
		case R.id.btQty9:
			mQtyBuffer.append("9");
			break;
		case R.id.btQtyC:
			cleanQty();
			break;
		case R.id.btQtyPoint:
			if (mQtyBuffer.length() > 0) mQtyBuffer.append(".");
			else mQtyBuffer.append("0.");
			mBtQtyPoint.setEnabled(false);
			break;
		case R.id.btBackspace:
			int l = mQtyBuffer.length();
			if (l > 1) {
				if (mQtyBuffer.charAt(l -1) == '.') mBtQtyPoint.setEnabled(true);
				mQtyBuffer.deleteCharAt(l -1);
			} else {
				cleanQty();
			}
			break;
		}
		Buf2Screen();
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) mVibrator.vibrate(30);
		return false;
	}
	private void Buf2Screen() {
		 if (mQtyBuffer.length() > 0) {
			 ((EditText)this.getCurrentFocus()).setText(mQtyBuffer.toString());
			 
		 }
	}
	private void cleanQty() {
		if (mQtyBuffer.length() > 0) mQtyBuffer.delete(0,mQtyBuffer.length());
		((EditText)this.getCurrentFocus()).setText("0.0");
		mQtyBuffer.append("0.0");
		mBtQtyPoint.setEnabled(false);
	}



	public void settvKolvo(String etKolvo) {
		this.tvKolvo.setText(etKolvo);
	}

	

	public void settvCena(String etCena) {
		this.tvCena.setText(etCena);
	}
}
