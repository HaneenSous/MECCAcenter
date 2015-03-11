package com.mecca_center.app.meccacenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.mecca_center.app.utils.AzanListPreference;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;


import java.util.List;
import java.util.Set;


@EActivity
public class SettingsActivity extends PreferenceActivity {

    @Pref
    static AppPrefs_ appPrefs;
    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      if(isSimplePreferences(this))  this.getPreferenceManager().setSharedPreferencesName("AppPrefs");
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.preference_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return NotificationPreferenceFragment.class.getName().equals(fragmentName) ;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        addPreferencesFromResource(R.xml.pref_notification);

        bindPreferenceSummaryToValue(findPreference("Azantone"));
        bindPreferenceSummaryToValue(findPreference("playAzan"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }


    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference.getKey().equals("Azantone")) {
                int index = ((ListPreference) preference).findIndexOfValue(stringValue);
                    preference.setSummary(((ListPreference) preference).getEntries()[index]);

            } else if(preference.getKey().equals("playAzan")){
                String Fajr="",Sunrise="",Dhuhr="",Asr="",Maghrib="",Isha="";
                Set<String> values= (Set<String>) value;
                for(String azan:values){

                    if(azan.equals("Fajr")){
                        Fajr="Fajr";
                    }else if(azan.equals("Sunrise")){
                        Sunrise=",Sunrise";
                    }else if(azan.equals("Dhuhr")){
                        Dhuhr=",Dhuhr";
                    }else if(azan.equals("Asr")){
                        Asr=",Asr";
                    }else if(azan.equals("Maghrib")){
                        Maghrib=",Maghrib";
                    }else if(azan.equals("Isha")){
                        Isha=",Isha";
                    }
                    String  summary=Fajr+Sunrise+Dhuhr+Asr+Maghrib+Isha;
                    preference.setSummary(summary);
                }
            }else{

                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
if(preference.getKey().equals("Azantone")){

    int index=appPrefs.Azantone().get().indexOf(".");
            if(index>0) {
                preference.setSummary(appPrefs.Azantone().get().subSequence(0, index));
            }

} else if(preference.getKey().equals("playAzan")){
    sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,appPrefs.playAzan().get());
}





    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.getPreferenceManager().setSharedPreferencesName("AppPrefs");
            addPreferencesFromResource(R.xml.pref_notification);
            bindPreferenceSummaryToValue(findPreference("Azantone"));
        }
    }


}
