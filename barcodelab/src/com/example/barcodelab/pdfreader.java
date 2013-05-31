package com.example.barcodelab;

import java.io.File;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.app.Activity;


public class pdfreader extends Activity implements View.OnClickListener{

	ImageView display;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdfreader);

		display = (ImageView) findViewById(R.id.IVDisplay);
		ImageView image1 = (ImageView) findViewById(R.id.IVimage1);
		ImageView image2 = (ImageView) findViewById(R.id.IVimage2);
		ImageView image3 = (ImageView) findViewById(R.id.IVimage3);
		
		ImageView image4 = (ImageView) findViewById(R.id.IVimage4);
		ImageView image5 = (ImageView) findViewById(R.id.IVimage5);
		ImageView image6 = (ImageView) findViewById(R.id.IVimage6);
		ImageView image7 = (ImageView) findViewById(R.id.IVimage7);
		ImageView image8 = (ImageView) findViewById(R.id.IVimage8);
		ImageView image9 = (ImageView) findViewById(R.id.IVimage9);
		ImageView image10 = (ImageView) findViewById(R.id.IVimage10);
		
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);

		image4.setOnClickListener(this);
		image5.setOnClickListener(this);
		image6.setOnClickListener(this);
		image7.setOnClickListener(this);
		image8.setOnClickListener(this);
		image9.setOnClickListener(this);
		image10.setOnClickListener(this);
	}

	public void onClick(View v) {
	
		switch(v.getId()){
		
		case R.id.IVimage1:
			display.setImageResource(R.drawable.j1);
			break;
		case R.id.IVimage2:
			display.setImageResource(R.drawable.j2);
			break;
		case R.id.IVimage3:
			display.setImageResource(R.drawable.j3);
			break;
			
		case R.id.IVimage4:
			display.setImageResource(R.drawable.j4);
			break;
		case R.id.IVimage5:
			display.setImageResource(R.drawable.j5);
			break;
		case R.id.IVimage6:
			display.setImageResource(R.drawable.j6);
			break;
		case R.id.IVimage7:
			display.setImageResource(R.drawable.j7);
			break;
		case R.id.IVimage8:
			display.setImageResource(R.drawable.j8);
			break;
		case R.id.IVimage9:
			display.setImageResource(R.drawable.j9);
			break;
		case R.id.IVimage10:
			display.setImageResource(R.drawable.j10);
			break;
		}
		
		
		
	}
}
