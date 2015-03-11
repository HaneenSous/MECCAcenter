package com.mahmoud.customview.compassview;

import android.content.Context;
import android.content.pm.PackageManager;

public class CompassUtility {

    public static boolean isDeviceCompatible(Context context) {
        return context.getPackageManager() != null &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER) &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
    }

}
