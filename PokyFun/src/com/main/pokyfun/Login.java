package com.main.pokyfun;

import java.io.IOException;
import java.util.UUID;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	Button login;
	EditText phoneNumber;
	EditText emailAdd;
	GoogleCloudMessaging gcm;
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login = (Button) findViewById(R.id.a_login);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					loginActivity();
			}
		});
	}

	public void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public void loginActivity() {
		String deviceID = getDeviceID();
		phoneNumber = (EditText) findViewById(R.id.a_number);
		emailAdd = (EditText) findViewById(R.id.user_email);
		String phoneId = phoneNumber.getText().toString();
		String email = emailAdd.getText().toString();
		getRegId(phoneId, deviceID, email);
	}

	public String getDeviceID() {
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;

	}

	public void getRegId(final String phoneId, final String deviceId, final String email) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(Config.PROJECT_ID);
					msg = "Device registered [phone number:" + phoneId
							+ "], registration ID=" + regid;
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					prefs.edit().putString("phoneId", phoneId).commit();
					prefs.edit().putString("deviceId", deviceId).commit();
					prefs.edit().putString("regId", regid).commit();
					prefs.edit().putString("email", email).commit();
					
					loadRegister(phoneId, deviceId, regid, email);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				showToast(msg);
			}
		}.execute(null, null, null);
	}
	public void loadRegister(String phoneId, String deviceId, String regId, String email) {
		Bundle b = new Bundle();
		b.putString("phoneId", phoneId);
		b.putString("deviceId", deviceId);
		b.putString("regId", regId);
		b.putString("email", email);
		Intent intent = new Intent(Login.this, Register.class);
		intent.putExtras(b);
		startActivity(intent);
		Login.this.finish();
	}
}
