package com.main.pokyfun;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NotifyManager extends Activity{
	Button play;
	Button stop;
	TextView msg;
	MediaPlayer player;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_manager);
		msg = (TextView) findViewById(R.id.received_msg);
		Bundle b = getIntent().getExtras();
		msg.setText(b.getString("message"));
		play = (Button) findViewById(R.id.playit);
		stop = (Button) findViewById(R.id.stop_btn);
		player=  new MediaPlayer();
		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String message = msg.getText().toString();
				try {
					if (player.isPlaying())
						player.stop();
					player = new MediaPlayer();
				    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				    player.setDataSource(Config.PREFIX_URL + message + ".mp3");
				    player.prepare();
				    player.start();    
				} catch (Exception e) {
					Log.e("Error [mp3]", e.getMessage());
				}
			}
		});
		
		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (player.isPlaying())
						player.stop();  
				} catch (Exception e) {
					Log.e("Error [mp3]", e.getMessage());
				}
			}
		});
	}
}
