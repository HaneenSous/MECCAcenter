package com.mecca_center.app.meccacenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.androidannotations.annotations.EReceiver;

/**
 * Created by The_Dev on 2/23/2015.
 */
@EReceiver
public class AzanWackBroadcast extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context.getPackageName(),AzanService_.class.getName());

        startWakefulService(context,intent.setComponent(componentName));
        setResultCode(Activity.RESULT_OK);
    }
}
