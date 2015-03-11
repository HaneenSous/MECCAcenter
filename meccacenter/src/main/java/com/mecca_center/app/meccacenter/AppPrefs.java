package com.mecca_center.app.meccacenter;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.DefaultStringSet;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by The_Dev on 2/8/2015.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface AppPrefs {

    @DefaultInt(R.raw.adhan_makkah)
  public  int AzanNumber();

    @DefaultInt(-1)
    public int NextPrayerIndex();

    @DefaultBoolean(false)
    public  boolean FirstTimeAlarm();


    @DefaultBoolean(false)
    public boolean registered();

    @DefaultString(value = "adhan_makkah.mp3" )
    public String Azantone();

    @DefaultString("")
    public String Azanname();


    @DefaultStringSet()
    public Set<String> playAzan();

}