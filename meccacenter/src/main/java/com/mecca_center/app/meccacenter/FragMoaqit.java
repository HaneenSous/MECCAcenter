package com.mecca_center.app.meccacenter;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.mecca_center.app.utils.EndpointData;
import com.mecca_center.app.utils.ServiceUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.frag_moaqit)
public class FragMoaqit extends Fragment implements PlayUtil.PlayUtilCallBacks, ServiceUtils.EndpointCallback, AdapterView.OnItemSelectedListener {

    @Pref
    AppPrefs_ pref;

    @ViewById
    TextView tvFajr;
    @ViewById
    TextView tvFajrTime;
    @ViewById
    TextView tvSunrise;
    @ViewById
    TextView tvSunriseTime;
    @ViewById
    TextView tvDhuhr;
    @ViewById
    TextView tvDhuhrTime;
    @ViewById
    TextView tvAsr;
    @ViewById
    TextView tvAsrTime;
    @ViewById
    TextView tvMaghrib;
    @ViewById
    TextView tvMaghribTime;
    @ViewById
    TextView tvIsha;
    @ViewById
    TextView tvIshaTime;

    @ViewById
    Spinner SpLocation;

    @ViewById
    TextView tvDateHiijri;

    @ViewById
    TextView tvDateJ;
    @ViewById
    TextView tvNextFajr;

    @ViewById
    TextView tvNextFajrTime;

    @ViewById
    TableRow NextDayRow;

    @Bean
    PrayerTimes mPrayerTimes;

    @Bean
    PlayUtil playUtil;

    @Bean
    ServiceUtils serviceUtils;

Location location;

    @AfterViews
    void initTimes() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Locations,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpLocation.setAdapter(adapter);
        SpLocation.setOnItemSelectedListener(this);
        serviceUtils.setEndpointCallback(this);
        if (!pref.FirstTimeAlarm().get()) {
            mPrayerTimes.setupAlarm(getActivity(), false);
            pref.edit().FirstTimeAlarm().put(true).apply();
        }
        playUtil.setCallBacks(this);
        if (!pref.registered().get()) {
            if (playUtil.CheckPlayService()) {
                playUtil.RegisterDevice();
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        tvDateHiijri.setText(getHiijri());

        DateTimeFormatter dateTimeFormatter1 = DateTimeFormat.forPattern(" hh:mm aa ");
        tvDateJ.setText(DateTimeFormat.forPattern("E d MMMM yyyy ").print(LocalDate.now()));
      if(location!=null) {
          mPrayerTimes.getNextPrayer(location.getLatitude(),location.getLongitude());
      }else{
          mPrayerTimes.getNextPrayer(null,null);
      }
        tvFajrTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(0))));
        tvSunriseTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(1))));
        tvDhuhrTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(2))));
        tvAsrTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(3))));
        tvMaghribTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(5))));
        tvIshaTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(6))));
       if(!mPrayerTimes.isNextday()) {
           switch (mPrayerTimes.NextPrayerIndex()) {
               case 0:
                   tvFajr.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvFajrTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
               case 1:
                   tvSunrise.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvSunriseTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
               case 2:
                   tvDhuhr.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvDhuhrTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
               case 3:
                   tvAsr.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvAsrTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
               case 5:
                   tvMaghrib.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvMaghribTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
               case 6:
                   tvIsha.setTextColor(getResources().getColor(R.color.NextPrayer));
                   tvIshaTime.setTextColor(getResources().getColor(R.color.NextPrayer));

                   break;
           }
       }else{
        NextDayRow.setVisibility(View.VISIBLE);
           tvNextFajrTime.setText(dateTimeFormatter1.print(new LocalTime(mPrayerTimes.getPrayerTimes().get(0))));

        }

    }

    @Click
    public void BTNDonate(View v) {
        DonateActivity_.intent(getActivity()).start();
    }

    @Override
    public void getRegisterId(String RegisteredId) {
        serviceUtils.registerDevice(RegisteredId.trim());

    }

    @Override
    public void onResult(int request, int result, EndpointData data) {
        if (result == ServiceUtils.SUCCESS) {
            if (request == ServiceUtils.REGISTER) {
                pref.edit().registered().put(true).apply();

            }
        }

    }


    String getHiijri() {
        String[] hmonths = {"Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Akhir", "Jamadi al-Awwal", "Jamadi al-Akhir", "Rajab", "Shabaan", "Ramadhan", "Shawwal", "Zilqad", "Zilhajj"};
        DateTime IslamicDate = DateTime.now().withChronology(IslamicChronology.getInstance(DateTimeZone.getDefault()));
        DateTimeFormatter format = DateTimeFormat.forPattern("dd -- yyyy").withChronology(IslamicChronology.getInstance(DateTimeZone.getDefault()));
        int MonthIndex = Integer.parseInt(DateTimeFormat.forPattern("MMM").withChronology(IslamicChronology.getInstance(DateTimeZone.getDefault())).print(IslamicDate)) - 1;
        String month = hmonths[MonthIndex];
        return format.print(IslamicDate).replaceAll("--", month);

    }

    Location getUserLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) location = new Location("");
        return location;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter().getItem(position).equals("Masjd")){
            location=null;
            onResume();
        }else{
            location=getUserLocation();
            onResume();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
