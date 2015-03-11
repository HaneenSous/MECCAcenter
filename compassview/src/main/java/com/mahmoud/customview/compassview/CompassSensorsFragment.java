package com.mahmoud.customview.compassview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Surface;
import android.view.View;


@SuppressWarnings("SuspiciousNameCombination")
public class CompassSensorsFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magneticFieldSensor;

    private float[] temporaryRotationMatrix = new float[9];
    private float[] rotationMatrix = new float[9];
    private float[] accelerometerData = new float[3];
    private float[] magneticData = new float[3];
    private float[] orientationData = new float[3];
    private float azimuth;
    private AzimuthCallBack azimuthCallBack;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    public float getAzimuth() {
        return azimuth;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magneticFieldSensor);
    }

    private void loadSensorData(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) accelerometerData = event.values;
        else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) magneticData = event.values;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        loadSensorData(event);
        SensorManager.getRotationMatrix(temporaryRotationMatrix, null, accelerometerData, magneticData);

        configureDeviceAngle();

        SensorManager.getOrientation(rotationMatrix, orientationData);
        azimuth = (float) Math.toDegrees(orientationData[0]);
        azimuthCallBack.getAzimuth(azimuth);
    }

    private void configureDeviceAngle() {
        switch (getActivity().getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0: // Portrait
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_Z,
                        SensorManager.AXIS_Y, rotationMatrix);
                break;
            case Surface.ROTATION_90: // Landscape
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_Y,
                        SensorManager.AXIS_MINUS_Z, rotationMatrix);
                break;
            case Surface.ROTATION_180: // Portrait
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_MINUS_Z,
                        SensorManager.AXIS_MINUS_Y, rotationMatrix);
                break;
            case Surface.ROTATION_270: // Landscape
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_MINUS_Y,
                        SensorManager.AXIS_Z, rotationMatrix);
                break;
        }
    }

  public  void setAzimuthCallBack(AzimuthCallBack azimuthCallBack){
        this.azimuthCallBack=azimuthCallBack;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    interface AzimuthCallBack{
        void getAzimuth(float azimuth);
    }


}
