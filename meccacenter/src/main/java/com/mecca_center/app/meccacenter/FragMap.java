package com.mecca_center.app.meccacenter;


import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.EFragment;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment
public class FragMap extends SupportMapFragment implements OnMapReadyCallback {


    final private static double LATITUDE = 41.76269;
    final private static double LONGITUDE = -87.941357;
    static final LatLng MECCA_CENTER = new LatLng(LATITUDE, LONGITUDE);

    @Override
    public void onResume() {
        super.onResume();
        getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location_mark))
                .position(MECCA_CENTER)
                .title("MECCA center");
        googleMap.addMarker(markerOptions);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MECCA_CENTER, 15));

    }
}
