package com.mecca_center.app.compass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mecca_center.app.meccacenter.BuildConfig;
import com.squareup.picasso.Picasso;


public class CompassView extends ImageView implements CompassSensorsFragment.AzimuthCallBack{


    private static final int FAST_ANIMATION_DURATION = 200;
    private static final int DEGREES_360 = 360;
    private static final float CENTER = 0.5f;

    private Context context;
    private Location userLocation;
    private Location objectLocation;
    private Bitmap directionBitmap;

    private int drawableResource;
    private float lastRotation;
    float azimuth;

    @SuppressWarnings("unused")
    public CompassView(Context context) {
        super(context);
        init(context);
    }

    @SuppressWarnings("unused")
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        if (CompassUtility.isDeviceCompatible(context)) {

        } else setVisibility(GONE);
    }

    public void initializeCompass(Location userLocation, Location objectLocation, int drawableResource) {
        if (CompassUtility.isDeviceCompatible(context)) {
            this.userLocation = userLocation;
            this.objectLocation = objectLocation;
            this.drawableResource = drawableResource;
            startRotation();
        }
    }

    private void startRotation() {
        GeomagneticField geomagneticField = new GeomagneticField(
                (float) userLocation.getLatitude(), (float) userLocation.getLongitude(),
                (float) userLocation.getAltitude(), System.currentTimeMillis());


        azimuth -= geomagneticField.getDeclination();

        float bearTo = userLocation.bearingTo(objectLocation);
        if (bearTo < 0) bearTo = bearTo + DEGREES_360;

        float rotation = bearTo - azimuth;
        if (rotation < 0) rotation = rotation + DEGREES_360;

        rotateImageView(this, drawableResource, rotation);

        if (BuildConfig.DEBUG) Log.d(CompassConstants.LOG_TAG, String.valueOf(rotation));
    }

    @SuppressWarnings("ConstantConditions")
    private void rotateImageView(ImageView compassView, int drawable, float currentRotate) {
        if (directionBitmap == null) {

            directionBitmap = BitmapFactory.decodeResource(getResources(), drawable);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            fadeInAnimation.setAnimationListener(new CustomAnimationListener());
            compassView.startAnimation(fadeInAnimation);
           /* compassView.setImageDrawable(new BitmapDrawable(getResources(), directionBitmap));
            compassView.setScaleType(ScaleType.FIT_XY);*/
            Picasso.with(getContext()).load(drawable).resize(1000,1000).into(compassView);

        } else {
            currentRotate = currentRotate % DEGREES_360;
            int animationDuration = FAST_ANIMATION_DURATION;

            RotateAnimation rotateAnimation = new RotateAnimation(lastRotation, currentRotate,
                    Animation.RELATIVE_TO_SELF, CENTER, Animation.RELATIVE_TO_SELF, CENTER);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(animationDuration);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setAnimationListener(new CustomAnimationListener());

            lastRotation = currentRotate;

            compassView.startAnimation(rotateAnimation);
        }
    }

    private class CustomAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            startRotation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    @Override
    public void getAzimuth(float azimuth) {
        this.azimuth=azimuth;
    }



}
