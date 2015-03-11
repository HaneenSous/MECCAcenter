package com.mecca_center.app.meccacenter;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.mecca_center.app.compass.CompassSensorsFragment;
import com.mecca_center.app.compass.CompassView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;



/**
 * Created by The_Dev on 2/11/2015.
 */


@EFragment(R.layout.frag_compass)
public class CompassFragment extends CompassSensorsFragment {

    final private static double LATITUDE=21.4225103;
    final private static double LONGITUDE=39.8261464;

    @ViewById(R.id.compassView)
    CompassView mCompassView;


    @AfterViews
    public void runCompass() {

        Location mLocation = new Location("");
        mLocation.setLatitude(LATITUDE);
        mLocation.setLongitude(LONGITUDE);

        if(getUserLocation()!=null) {
            setAzimuthCallBack(mCompassView);
            mCompassView.initializeCompass(getUserLocation(), mLocation, R.drawable.sgada11);
        }
    }


    Location getUserLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) location = new Location("");
        return location;
    }



}
