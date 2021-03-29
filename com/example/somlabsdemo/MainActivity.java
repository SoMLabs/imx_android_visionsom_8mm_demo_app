package com.example.somlabsdemo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.io.FileOutputStream;

public class MainActivity extends Activity {

    private static final String VIDEO_SAMPLE = "video_example";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setSecurityManager(null);

        final ToggleButton led1Switch = findViewById(R.id.led1Switch);
        led1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("SOMLABS", "Is checked " + isChecked);
                try{
                    FileOutputStream fos = new FileOutputStream("/sys/devices/platform/leds/leds/LED-IO-04/brightness");
                    byte brightness = isChecked ? (byte)'1' : (byte)'0';
                    fos.write(brightness);
                    fos.close();
                } catch ( Exception e) {
                    Log.w("SOMLABS", "Failed writing to LED1 brightness file");
                }
            }
        });

        final ToggleButton led2Switch = findViewById(R.id.led2Switch);
        led2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("SOMLABS", "Is checked " + isChecked);
                try{
                    FileOutputStream fos = new FileOutputStream("/sys/devices/platform/leds/leds/LED-IO-05/brightness");
                    byte brightness = isChecked ? (byte)'1' : (byte)'0';
                    fos.write(brightness);
                    fos.close();
                } catch ( Exception e) {
                    Log.w("SOMLABS", "Failed writing to LED2 brightness file");
                }
            }
        });

        final TextView textView = findViewById(R.id.textView);

        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.valueOf(progress) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        videoView = findViewById(R.id.videoView);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
        videoView.start();
    }

    private void initializePlayer() {
        Uri videoUri = Uri.parse("android.resource://com.example.somlabsdemo/"+R.raw.video_example);
        videoView.setVideoURI(videoUri);
    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }
}
