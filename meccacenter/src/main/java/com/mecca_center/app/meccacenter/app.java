package com.mecca_center.app.meccacenter;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by The_Dev on 3/1/2015.
 */
@EApplication
public class app extends Application {


    @Pref
    AppPrefs_ pref;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        if (pref.playAzan().getOr(null) == null) {
            Set<String> azan = new HashSet<>();
            List<String> azanList = new ArrayList<>();
            azanList.add("Fajr");
            azanList.add("Dhuhr");
            azanList.add("Asr");
            azanList.add("Maghrib");
            azanList.add("Isha");
            azan.addAll(azanList);
            pref.edit().playAzan().put(azan).apply();
        }
    }
}
