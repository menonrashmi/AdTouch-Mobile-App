package com.example.barcodelab;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
			}
		});
    }
    
    public void onClick_LoginButton(View v) {

		try {
			
			LoginUserAsyncTask asyncTask = new LoginUserAsyncTask();
			asyncTask.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public class LoginUserAsyncTask extends AsyncTask <Void, Void, HttpResponse> {
    	
		private String _email = "";
		private String _password = "";
				
		@Override
		protected void onPreExecute() {
			
			Button btnLogin = (Button)findViewById(R.id.btnLogin);
			btnLogin.setClickable(false);
			
			// Get Data from UI
			EditText txt;
			
			txt = (EditText) findViewById(R.id.login_email);
			_email = txt.getText().toString();
			
			txt = (EditText) findViewById(R.id.login_password);
			_password = txt.getText().toString();
									
			super.onPreExecute();
		}
		
		@Override
		protected HttpResponse doInBackground(Void... params) {
			//
			try {
				
				// url with the post data
				String url = "http://adtouch.cloudfoundry.com/rest/user/" + _email;

				// sets the get request 
				HttpGet request = new HttpGet(url);

				// Set Basic Authentication in Authorization Header
				String pwdMD5 = getMD5(_password);
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
						_email, pwdMD5);
				BasicScheme scheme = new BasicScheme();
				Header authorizationHeader = scheme.authenticate(credentials, request);
				request.addHeader(authorizationHeader);
		            
				// sets a request header so the page receving the request will know what to do with it
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");

				// instantiates httpclient to make request
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
			
				// Execute HTTP request
				HttpResponse response = httpclient.execute(request, localContext);
				
				return response;

			} catch (Exception e) {
				Log.e("LoginUserAsyncTask", "doInBackground Failed. " + e.toString());
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
			super.onPostExecute(response);
			//
			try {
				
				if( response == null)
				{
					showMessageDialog("NULL HttpResponse", "Login User Failed");
					return;
				}
				
				if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) // 200
				{
					// Succeeded
					JSONObject userInfo = getJSONFromHttpResponse(response);
					
					showMessageDialog("email = " + userInfo.getString("email") + 
							"\n id = " + userInfo.getString("id"), "User Logged In Successfully");
					
					MainActivity.SetLoggedInUser(userInfo.getString("email"));
					
					// Show Main Activity
					Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(myIntent);
				} 
				else 
				{
					// Some problem
					String errorMessage = getStringFromHttpResponse(response);
					showMessageDialog(errorMessage, "Login User Failed");
				}
			} catch (Exception e) {
				Log.e("LoginUserAsyncTask", "onPostExecute Failed. " + e.toString());
			}
			finally
			{
				Button btnLogin = (Button) findViewById(R.id.btnLogin);
				btnLogin.setClickable(true);
			}
		}

		private String getMD5(String s) {
		    try {
		        // Create MD5 Hash
		        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		        digest.update(s.getBytes());
		        byte messageDigest[] = digest.digest();
		        
		        // Create Hex String
		        StringBuffer hexString = new StringBuffer();
		        for (int i=0; i<messageDigest.length; i++)
		            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		        return hexString.toString();
		        
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    }
		    return "";
		}
		
		private String getStringFromHttpResponse(HttpResponse response) {

			InputStream is = null;
			String ret = "";

			try {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				ret = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			return ret;

		}
		
		private JSONObject getJSONFromHttpResponse(HttpResponse response) throws Exception {

			String json = getStringFromHttpResponse(response);
			
			JSONObject jObj = null;
		
			// try parse the string to a JSON object
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				String errorInfo = "Failed to convert string to JSon object. "+e.getMessage();
				Log.e("JSON Parser", "Error parsing data " + e.toString());
				throw new Exception(errorInfo);
			}

			// return JSON String
			return jObj;

		}
	}

	private void showMessageDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(title).setMessage(message).setPositiveButton("OK", null).show();
	}
}