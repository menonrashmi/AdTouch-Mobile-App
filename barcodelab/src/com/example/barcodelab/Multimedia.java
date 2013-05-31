package com.example.barcodelab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Multimedia extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.multimedia);
		VideoView vid = (VideoView) findViewById(R.id.vv_play);

		// To get Url Values from Barcode main form
		
		Intent playIntent = getIntent();
		

		String name = playIntent.getStringExtra("Url");
		String Urlname = "\"" + name + "\"";
		
		// String Url="http://webbie.org/youtube/skeleton.mp4";
		//String Url= "http://dev.hpac.dev-site.org/sites/default/files/videos/about/mobile.mp4";
		// String Url= "http://commonsware.com/misc/test2.3gp";
		//String Url= "http://project-tango.org/Projects/TangoBand/Songs/files/1-02%20Soledad.mp3";

		// Urlname = "http://mediaelementjs.com/media/echo-hereweare.mp4";

		
		// Uri video= Uri.parse(Urlname);

		// / vid.setVideoURI(Uri.parse(Urlname));

		vid.setMediaController(new MediaController(this));

		
		// vid.setVideoURI(video);
		//Get Urlname and pass it to videoview
		vid.setVideoURI(Uri.parse(Urlname));
		vid.requestFocus();
		vid.start();

	}
}
