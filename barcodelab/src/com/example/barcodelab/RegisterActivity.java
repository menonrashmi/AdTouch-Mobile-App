package com.example.barcodelab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
//import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set View to register.xml
		setContentView(R.layout.register);

		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

		// Listening to Login Screen link
		loginScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
	}

	public void onClick_RegisterButton(View v) {

		try {
			
			CreateUserAsyncTask asyncTask = new CreateUserAsyncTask();
			asyncTask.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	private JSONObject CreateUser(String firstName, String lastName,
			String userID, String password) {

		try {
			
			// convert parameters into JSON object
			JSONObject userInfoJson = new JSONObject();

			String passwordMD5 = getMD5("password");

			userInfoJson.put("udfId", "userID");
			userInfoJson.put("firstName", "firstName");
			userInfoJson.put("lastName", "lastName");
			userInfoJson.put("password", passwordMD5);
			userInfoJson.put("phone", "4083389393");
			userInfoJson.put("email", "s@g.com");

			// passes the results to a string builder/entity
			StringEntity se = new StringEntity(userInfoJson.toString());

			// instantiates httpclient to make request
			DefaultHttpClient httpclient = new DefaultHttpClient();

			// url with the post data
			String url = "http://adtouch.cloudfoundry.com/rest/user";
			HttpPost httpost = new HttpPost(url);

			// sets the post request as the resulting string
			httpost.setEntity(se);
			// sets a request header so the page receving the request
			// will know what to do with it
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");

			// Allow Network activity on the UI Thread
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			// Handles what is returned from the page
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = httpclient.execute(httpost, localContext);
			
			if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_CREATED) 
			{
				JSONObject ret = getJSONFromHttpResponse(response);
				return ret;
			} 
			else 
			{
				// Some problem
				String errorMessage = "";
				int statusCode =response.getStatusLine().getStatusCode() ;
				if (statusCode == org.apache.http.HttpStatus.SC_BAD_REQUEST) // 400
				{
					errorMessage = "400-BadRequest : Input Data is Invalid";
				}
				else if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_CONFLICT) // 409
				{
					errorMessage = "409-Conflict : Email already exists";
				}
				else // Unknown problem
				{
					errorMessage = String.valueOf(statusCode) + "-Unknown Error";
				}
				showError(errorMessage, "Create User Failed");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			showError(e.getMessage(), "Create User Failed");
			return null;
		}

	}
	*/

	public class CreateUserAsyncTask extends AsyncTask <Void, Void, HttpResponse> {
	
		private String _firstName = "";
		private String _lastName = "";
		private String _email = "";
		private String _password = "";
		
		private JSONObject _userInfoJson = null;
		
		@Override
		protected void onPreExecute() {
			
			Button btnRegister = (Button)findViewById(R.id.btnRegister);
			btnRegister.setClickable(false);
			
			// Get Data from UI
			EditText txt;
			
			txt = (EditText) findViewById(R.id.reg_firstname);
			_firstName = txt.getText().toString();
			
			txt = (EditText) findViewById(R.id.reg_lastname);
			_lastName = txt.getText().toString();
			
			txt = (EditText) findViewById(R.id.reg_email);
			_email = txt.getText().toString();
			
			txt = (EditText) findViewById(R.id.reg_password);
			_password = txt.getText().toString();
			
			_userInfoJson = CreateUserJSon();
			
			super.onPreExecute();
		}
		
		@Override
		protected HttpResponse doInBackground(Void... params) {
			//
			try {
				
				// url with the post data
				String url = "http://adtouch.cloudfoundry.com/rest/user";

				// sets the post request 
				HttpPost httpost = new HttpPost(url);
				httpost.setEntity(new StringEntity(_userInfoJson.toString()));
				
				// sets a request header so the page receving the request will know what to do with it
				httpost.setHeader("Accept", "application/json");
				httpost.setHeader("Content-type", "application/json");

				// instantiates httpclient to make request
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
			
				// Execute HTTP request
				HttpResponse response = httpclient.execute(httpost, localContext);
				
				return response;

			} catch (Exception e) {
				Log.e("CreateUserAsyncTask", "doInBackground Failed. " + e.toString());
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
					showMessageDialog("NULL HttpResponse", "Create User Failed");
					return;
				}
				
				if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_CREATED) // 201
				{
					// Succeeded
					JSONObject userInfo = getJSONFromHttpResponse(response);
					showMessageDialog("email = " + userInfo.getString("email") + 
							", id = " + userInfo.getString("id"), "User Created");
				} 
				else 
				{
					// Some problem
					String errorMessage = getStringFromHttpResponse(response);
					showMessageDialog(errorMessage, "Create User Failed");
				}
			} catch (Exception e) {
				Log.e("CreateUserAsyncTask", "onPostExecute Failed. " + e.toString());
			}
			finally
			{
				Button btnRegister = (Button) findViewById(R.id.btnRegister);
				btnRegister.setClickable(true);
			}
		}
		
		private JSONObject CreateUserJSon()
		{
			try {
				
				/* Format
				{ 
				  "udfId": "yyfearth", 
				  "email": "yyf@sjsu.edu", 
				  "firstName": "Wilson", 
				  "lastName": "Young", 
				  "phone": "4081234567", 
				  "password": "6a204bd89f3c8348afd5c77c717a097a", 
				  "account": { 
				    "organization": "SJSU" 
				  }, 
				  "address": { 
				    "city": "San Jose", 
				    "countryCode": "U.S.", 
				    "postalCode": "95192", 
				    "stateCode": "CA", 
				    "streetAddress1": "One Washington Square" 
				  } 
				}
				*/
				
				JSONObject jsonUser = new JSONObject();

				jsonUser.put("udfId", _firstName + "_" + _lastName); // Don't know what is udfID, maybe user ID. But email is the user ID too.
				jsonUser.put("email", _email);
				jsonUser.put("firstName", _firstName);
				jsonUser.put("lastName", _lastName);
				jsonUser.put("phone", "1234567890");
				String passMD5 = getMD5(_password);
				jsonUser.put("password", passMD5);
				
				// account
				JSONObject jsonAccount = new JSONObject();
				jsonAccount.put("organization", "SJSU");
				jsonUser.put("account", jsonAccount);
				
				// address
				JSONObject jsonAddress = new JSONObject();
				jsonAddress.put("city", "San Jose");
				jsonAddress.put("countryCode", "U.S.");
				jsonAddress.put("postalCode", "95192");
				jsonAddress.put("stateCode", "CA");
				jsonAddress.put("streetAddress1", "One Washington Square");
				jsonUser.put("address", jsonAddress);
				
				return jsonUser;
				
			} catch (Exception e) {
				Log.e("CreateUserAsyncTask", "CreateUserJSon Failed. " + e.toString());
				return null;
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