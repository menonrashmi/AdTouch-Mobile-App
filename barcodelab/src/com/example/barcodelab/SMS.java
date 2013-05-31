package com.example.barcodelab;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SMS extends Activity {

	   Button btnSendSMS;
	    EditText txtPhoneNo;
	    TextView txtMessage;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_sms);
        
        // To get Url Values from Barcode main form
        //Intent myIntent = getIntent();
        //TextView textview1= (TextView) findViewById(R.id.myTextView);
        
        //String name = myIntent.getStringExtra("Url");
        // textview1.setText(name);
        
         //---sends an SMS message to another device---
         
          //  Button  buttonSend = (Button) findViewById(R.id.btnSMS);
 		    //buttonSend.setOnClickListener(new OnClickListener() {
 			//EditText textPhoneNo = (EditText) findViewById(R.id.etnumber);
 			
 			//TextView textSMS = (TextView) findViewById(R.id.myTextView);
 				
 			//public void onClick(View v) {
  
 			  //String phoneNo = textPhoneNo.getText().toString();
 			  //String sms = textSMS.getText().toString();
  
 			  //try {
 				//SmsManager smsManager = SmsManager.getDefault();
 				//smsManager.sendTextMessage(phoneNo, null, sms, null, null);
 				//Toast.makeText(getApplicationContext(), "SMS Sent!",
 				//			Toast.LENGTH_LONG).show();
 			  //} catch (Exception e) {
 				//Toast.makeText(getApplicationContext(),
 					//"SMS faild, please try again later!",
 					//Toast.LENGTH_LONG).show();
 				//e.printStackTrace();
 			  //}
  
 //			}
 		//});
 	}
         
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.activity_sms, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
