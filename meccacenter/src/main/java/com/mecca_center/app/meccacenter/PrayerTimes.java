package com.mecca_center.app.meccacenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by The_Dev on 2/8/2015.
 */
@EBean
public class PrayerTimes {
    @RootContext
    Context context;

    @Pref
    AppPrefs_ prefs_;
    private ArrayList<String> prayerNames;
    private ArrayList<String> prayerTimes;

    private long RemainingTime = 0;
    private int NextPrayerIndex;
    private long NextPrayerTime;
    private PrayTime prayers;
    private boolean Nextday=false;


    public PrayerTimes() {


        prayers = new PrayTime();

        prayers.setTimeFormat(prayers.Time24);
        prayers.setCalcMethod(prayers.ISNA);
        prayers.setAsrJuristic(prayers.Shafii);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);



    }

    void getNextPrayer(Double Lat,Double Long) {

        double latitude = 41.76269;
        double longitude = -87.941357;

        if(Lat!=null && Long!=null){
            latitude=Lat;
            longitude=Long;
        }
        Calendar cal = new GregorianCalendar();


        if(BuildConfig.DEBUG){
            prayerTimes = prayers.getPrayerTimes(cal,
                    latitude, longitude, -6);
        }else{
            prayerTimes = prayers.getPrayerTimes(cal,
                    latitude, longitude, getGMT());
        }

        prayerNames = prayers.getTimeNames();


        Calendar mCalendar = new GregorianCalendar();


        if (isNewDay(prayerTimes.get(prayerTimes.size() - 1))) {
            Nextday=true;
            mCalendar.add(Calendar.DATE, 1);
        }
        if(mCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
                prayerTimes.set(2,"13:00");
        }

        for (int counter = 0; counter < prayerTimes.size(); counter++) {
            String[] TimeSpilter = prayerTimes.get(counter).split(":", 0);

            mCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(TimeSpilter[0]));
            mCalendar.set(Calendar.MINUTE, Integer.valueOf(TimeSpilter[1]));


            // skip sunset
            if (prayerNames.get(counter).equalsIgnoreCase("Sunset")){ continue;}

            if ((mCalendar.getTimeInMillis() > cal.getTimeInMillis())) {
                RemainingTime = (mCalendar.getTimeInMillis() - cal.getTimeInMillis());
                NextPrayerIndex = counter;
                NextPrayerTime = mCalendar.getTimeInMillis();

                return;
            }


        }

    }


    double getGMT() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();

        int offset = timeZone.getRawOffset();
        long hours = TimeUnit.MILLISECONDS.toHours(offset);
        double minutes = (double) TimeUnit.MILLISECONDS.toMinutes(offset - TimeUnit.HOURS.toMillis(hours)) / 60;
        double gmt = hours + minutes;
        return gmt;
    }


    public List<String> getPrayerTimes() {
        return prayerTimes;
    }

    public List<String> getPrayerNames() {
        return prayerNames;
    }

    public int NextPrayerIndex() {
        return NextPrayerIndex;
    }

    public long getRemainingTime() {
        return RemainingTime;
    }

    boolean isNewDay(String time) {
        String[] splitter = time.split(":");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(splitter[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(splitter[1]));
        if (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() <= 0) {
            return true;
        }
        return false;
    }

    public long getNextPrayerTime() {
        return NextPrayerTime;
    }

 public   void setupAlarm(Context context, boolean isBooted) {
            getNextPrayer(null,null);


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent IBroadcast = new Intent(context, AzanWackBroadcast_.class);

            PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, IBroadcast,PendingIntent.FLAG_UPDATE_CURRENT);

           if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getNextPrayerTime(), mPendingIntent);
           }else {
               alarmManager.set(AlarmManager.RTC_WAKEUP, getNextPrayerTime(), mPendingIntent);
           }

        prefs_.edit().Azanname().put(getPrayerNames().get(NextPrayerIndex)).apply();


 }


    public boolean isNextday() {
        return Nextday;
    }
}
