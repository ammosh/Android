package com.main.pokyfun;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendPoke extends Activity {
	TextView nameS;
	String numbers[];
	String message = "";
	Button sendIt;
	EditText msgContect;
	JSONObject jObj = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_poke);
		Bundle b = getIntent().getExtras();
		String name = b.getString("name");
		numbers = b.getStringArray("numbers");
		nameS = (TextView) findViewById(R.id.user_name);
		sendIt = (Button) findViewById(R.id.msg_send);
		msgContect = (EditText) findViewById(R.id.msg_content);
		nameS.setText(name);	
		sendIt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				message = msgContect.getText().toString();
				sendData(numbers, message);
			}
		});
	}

	private void sendData(String numbers[], String message) {
		ArrayList<String> params = new ArrayList<String>();
		params.add(message);
		for (int i = 0; i < numbers.length; i++) {
			params.add(numbers[i].replaceAll("\\D+",""));
		}
		try {
			String ret = new AsyncTask<ArrayList<String>, Void, String>() {
				@SuppressWarnings("deprecation")
				@Override
				protected String doInBackground(ArrayList<String>... params) {
					List nameValuePairs = new ArrayList();
					ArrayList<String> passed = params[0];
					if (passed.size() < 2)
						return "";
					nameValuePairs.add(new BasicNameValuePair("message", passed
							.get(0)));
					for (int i = 1; i < passed.size(); i++) {
						nameValuePairs.add(new BasicNameValuePair("key[]", passed
								.get(i)));
					}
					RequestHandler request = new RequestHandler();
					jObj = request.send(Config.SEND_URL, "POST",
							nameValuePairs);
					Log.d("obj", jObj.toString());
					try {
						return "success:"+jObj.getString("success");
					} catch (JSONException e) {
						Log.e("Error: [Send Poke]", e.getMessage());
						return "Error: " + e.getMessage();
					}
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
