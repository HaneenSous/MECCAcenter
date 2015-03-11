package com.mecca_center.app.meccacenter;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.IBinder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;


import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by The_Dev on 2/7/2015.
 */
@EIntentService
public class AzanService extends IntentService {


    WindowManager windowManager;
    @Pref
    AppPrefs_ appPrefs;

    View ParentView;
    TextView tvAzanName;
    ImageButton btnAzanDone;
    MediaPlayer mediaPlayer;
    @Bean
    PrayerTimes prayerTimes;

    public AzanService(){

        super("");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        ParentView = LayoutInflater.from(this).inflate(R.layout.azan_view, null);
        tvAzanName = (TextView) ParentView.findViewById(R.id.tvAzanName);
        btnAzanDone = (ImageButton) ParentView.findViewById(R.id.btnAzanDone);

           tvAzanName.setText(appPrefs.Azanname().get());
        btnAzanDone.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_check).actionBarSize().color(Color.WHITE));
        btnAzanDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null){
                    mediaPlayer.stop();
                }

                windowManager.removeView(ParentView);
                stopSelf();
            }
        });


        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
        params.flags=WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        windowManager.addView(ParentView, params);


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prayerTimes.setupAlarm(this,false);
          for(String azan:appPrefs.playAzan().get()){
            if(appPrefs.Azanname().get().equals(azan)){
                mediaPlayer = new MediaPlayer();
                try {
                    AssetFileDescriptor file = getAssets().openFd(appPrefs.Azantone().get());
                    mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(),file.getLength());

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }




                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        windowManager.removeView(ParentView);
                        stopSelf();
                    }
                });

            }
        }
        AzanWackBroadcast_.completeWakefulIntent(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
