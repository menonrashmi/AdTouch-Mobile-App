package com.example.barcodelab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.*;

@TargetApi(11)
@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
public class RestServices extends Activity implements OnClickListener {
	
	Button btnResponse;
	String URL_Adtouch="http://adtouch.cloudfoundry.com/rest/ad/barcode/7ced2de7674d4c80b055142013743440"; 
	String Response;
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_restservices);
findViewById(R.id.my_button).setOnClickListener(this);
}
public void onClick(View arg0) {
Button b = (Button)findViewById(R.id.my_button);
b.setClickable(false);
new LongRunningGetIO().execute();


//Show Json
btnResponse=(Button)findViewById(R.id.bnt_response);
btnResponse.setOnClickListener(new OnClickListener() {

	public void onClick(View v) {
		
		try {
			JsonReader reader = new JsonReader(new FileReader(URL_Adtouch));
		 
			reader.beginObject();
			while (reader.hasNext()) {
				 
				  String contentKeys = reader.nextName();
			 
				  if (contentKeys.equals("contentKeys")) {
			 
					//System.out.println(reader.nextString());
					 TextView text_Response = (TextView) findViewById(R.id.textView1);
					 //Response=reader.nextString();
					 text_Response.setText(reader.nextString()); 
			 
				  } else {
						reader.skipValue(); //avoid some unhandle events
				  }
			        }
			reader.endObject();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		     } catch (IOException e) {
			e.printStackTrace();
		     }
		}
		
		
	
	
});

}

public class LongRunningGetIO extends AsyncTask <Void, Void, String> {
protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
InputStream in = entity.getContent();
StringBuffer out = new StringBuffer();
int n = 1;
while (n>0) {
byte[] b = new byte[4096];
n =  in.read(b);
if (n>0) out.append(new String(b, 0, n));
}
return out.toString();
}
@Override
protected String doInBackground(Void... params) {
HttpClient httpClient = new DefaultHttpClient();
HttpContext localContext = new BasicHttpContext();
HttpGet httpGet = new HttpGet("http://adtouch.cloudfoundry.com/rest/ad/barcode/7ced2de7674d4c80b055142013743440");
String text = null;
try {
HttpResponse response = httpClient.execute(httpGet, localContext);
HttpEntity entity = response.getEntity();
text = getASCIIContentFromEntity(entity);
} catch (Exception e) {
return e.getLocalizedMessage();
}
return text;
}

		protected void onPostExecute(String results) {

			try {
				
				if (results != null) {

					JSONObject json = new JSONObject(results);
					
					String imageLink = json.optString("imageLink");
					String audioLink = json.optString("audioLink");
					String videoLink = json.optString("videoLink");
					String linkedUrl = json.optString("linkedUrl");
					
					EditText et = (EditText) findViewById(R.id.my_edit);
					et.setText(imageLink);
					
					// Similarly, we can do this for other items we want to show.

				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				Button b = (Button) findViewById(R.id.my_button);
				b.setClickable(true);
			}
		}

	}
}