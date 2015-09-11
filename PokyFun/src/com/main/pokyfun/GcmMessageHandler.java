package com.main.pokyfun;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GcmMessageHandler extends IntentService {
	private NotificationManager mNotificationManager;
	String mes;
	int mNotificationId = 1;
	public static final int NOTIFICATION_ID = 1;
	private Handler handler;

	public GcmMessageHandler() {
		super("GcmMessageHandler");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		mes = extras.getString(Config.MSG_KEY);
		Log.i("GCM",
				"Received : (" + messageType + ")  "
						+ extras.getString("message"));
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG)
						.show();
				Notify("New Message", mes);
			}
		});
	}

	private void Notify(String notificationTitle, String notificationMessage) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.startup)
				.setContentTitle(notificationTitle)
				.setContentText(notificationMessage);
		mBuilder.setAutoCancel(true);
		Intent intent = new Intent(this, NotifyManager.class);
		Bundle b = new Bundle();
		b.putString("message", notificationMessage);
		intent.putExtras(b);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    this,
		    0,
		    intent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		mBuilder.setContentIntent(resultPendingIntent);
		int mNotificationId = 001;
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		mNotificationId++;	
	}

}