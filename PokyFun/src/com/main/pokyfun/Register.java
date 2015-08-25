package com.main.pokyfun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Register extends Activity {
	static InputStream is = null;
	String json = "";
	JSONObject jObj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Bundle b = getIntent().getExtras();
		postDate(b.getString("phoneId"), b.getString("deviceId"), b.getString("regId"), b.getString("email"));
	}

	@SuppressWarnings("unchecked")
	private void postDate(String phoneId, String deviceId, String regId, String email) {
		ArrayList<String> params = new ArrayList<String>();
		params.add(phoneId);
		params.add(deviceId);
		params.add(regId);
		params.add(email);
		try {
			String ret = new AsyncTask<ArrayList<String>, Void, String>() {

				@SuppressWarnings("deprecation")
				@Override
				protected String doInBackground(ArrayList<String>... params) {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(Config.REGISTER_URL);

					List nameValuePairs = new ArrayList();
					ArrayList<String> passed = params[0];
					if (passed.size() != 4)
						return "";
					nameValuePairs.add(new BasicNameValuePair("phoneID", passed
							.get(0)));
					nameValuePairs.add(new BasicNameValuePair("uID", passed
							.get(1)));
					nameValuePairs.add(new BasicNameValuePair("regID", passed
							.get(2)));
					nameValuePairs.add(new BasicNameValuePair("email", passed
							.get(3)));
					try {
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity httpEntity = response.getEntity();
						is = httpEntity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "iso-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						json = sb.toString();
				        try {
				            jObj = new JSONObject(json);
				            int success = jObj.getInt(Config.TAG_SUCCESS);
							if (success == 1) {
								Log.d("User Created!", json.toString());
								finish();
								return jObj.getString(Config.MSG_KEY);
							} else {
								Log.d("Login Failure!", jObj.getString(Config.MSG_KEY));
								return jObj.getString(Config.MSG_KEY);
							}
				        } catch (JSONException e) {
				        	Log.e("JSON Parser", json);
				        }
				        
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return "Error!";
				}
				
				@Override
				protected void onPostExecute(String msg) {
					showToast(msg);
				}
			}.execute(params).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}
