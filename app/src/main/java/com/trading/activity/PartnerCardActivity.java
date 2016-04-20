package com.trading.activity;

import android.app.Activity;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trading.R;
import com.trading.dao.PartnersDao;
import com.trading.utils.Partner;


public class PartnerCardActivity extends Activity {

    private int PartnerId;
    private TextView tvPartnerName;
    private TextView tvPartnerAddress;
    private TextView tvPartnerPhone;

    Partner partner;
    PartnersDao pd=new PartnersDao(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_card);
        Bundle extras = getIntent().getExtras();
        PartnerId = extras.getInt("_ID");


        partner = pd.getPartner(PartnerId);
          PrepareDataView();
          //setData();

    }
    private  void PrepareDataView()
    {
        tvPartnerName=(TextView)findViewById(R.id.tvPartnerName);
        tvPartnerAddress=(TextView)findViewById(R.id.tvPartnerAddress);
        tvPartnerPhone=(TextView)findViewById(R.id.tvPartnerPhone);
        tvPartnerName.setText(partner.name);
        tvPartnerAddress.setText(partner.address);
        tvPartnerPhone.setText(partner.phone);
        tvPartnerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+tvPartnerPhone.getText().toString()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
    }




/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_partner_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
