package com.example.barcodelab;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String LoggedInUser = "";
	public static void SetLoggedInUser(String userName)
	{
		LoggedInUser = userName;
	}
	
	Button btnSendSMS;
	Button btnPlay;
	Button btnimage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView txtLoginStatus = (TextView) findViewById(R.id.txtLoginStatus);
		if( LoggedInUser.length() == 0)
		{
			txtLoginStatus.setText("User Not Logged In");
		}
		else
		{
			txtLoginStatus.setText("Logged In : "+ LoggedInUser);			
		}

		btnPlay = (Button) findViewById(R.id.btnPlay);

		btnPlay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// to start Multimedia activity
				TextView txtvalue = (TextView) findViewById(R.id.textV_url);
				Intent playIntent = new Intent(MainActivity.this,
						Multimedia.class);
				playIntent.putExtra("Url", txtvalue.getText().toString());
				startActivity(playIntent);
			}

		});

		//btnimage = (Button) findViewById(R.id.bntview);

		//btnimage.setOnClickListener(new OnClickListener() {

			//public void onClick(View v) {
					//to start image activity
				//Intent imageIntent = new Intent(MainActivity.this,
					//	pdfreader.class);
				// playIntent.putExtra("Url", txtvalue.getText().toString());
				//startActivity(imageIntent);
		//	}
		//});

		// added
		btnSendSMS = (Button) findViewById(R.id.bntsms);

		btnSendSMS.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// To start SMS activity

				/*TextView txtvalue = (TextView) findViewById(R.id.textV_url);
				Intent myIntent = new Intent(MainActivity.this, SMS.class);
				myIntent.putExtra("Url", txtvalue.getText().toString());
				startActivity(myIntent);
*/
				
				Intent myIntent = new Intent(MainActivity.this, RestServices.class);
				startActivity(myIntent);



			}
		});

	}

	public void onClick(View view) {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {

			String barcode;
			String typ;

			barcode = scanResult.getContents();
			typ = scanResult.getFormatName();

			// Display captured decoded URL and type
			TextView textV_type = (TextView) findViewById(R.id.textV_type);
			textV_type.setText("Type: " + typ);

			TextView textV_url = (TextView) findViewById(R.id.textV_url);
			// textV_url.setText("URL: "+barcode);
			//textV_url.setText(barcode);
			
			
			//extract product id added on 9th dec
			//String Urlstring= textV_url.toString();
			String productid = barcode.substring(barcode.length()-32);
			
			//Added comment on 9th dec
			//textV_url.setText(barcode);
			textV_url.setText(productid);
	

		}

	}
	
	public void onClick_AddToCart(View view) {
		// Show Login Screen if not Logged In
		if (LoggedInUser.length() == 0) {
			Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(myIntent);
		}
	}
}
