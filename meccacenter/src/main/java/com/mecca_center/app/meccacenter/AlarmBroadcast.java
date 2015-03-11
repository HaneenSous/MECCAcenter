package com.mecca_center.app.meccacenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by The_Dev on 2/7/2015.
 */
@EReceiver
public class AlarmBroadcast extends BroadcastReceiver {

    @Bean
    PrayerTimes mPrayerTimes;



    @Override
    public void onReceive(Context context, Intent intent) {

            mPrayerTimes.setupAlarm(context, true);

        }

}
