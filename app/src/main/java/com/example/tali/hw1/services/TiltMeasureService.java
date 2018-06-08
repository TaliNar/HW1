package com.example.tali.hw1.services;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tali.hw1.ui.GameActivity;


public class TiltMeasureService extends IntentService {

    final static int THRESHOLD = 2;
    public static final String MY_NOTIFICATION = "com.example.tali.hw1.MY_NOTIFICATION";

    // Binder given to clients
    private final IBinder mBinder = new TiltMeasureBinder();
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private static int counter = 0;

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;

    // initial azimuth, pitch and roll
    private float initialAzimuth;
    private float initialPitch;
    private float initialRoll;

    public TiltMeasureService(){
        super("TiltMeasureService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        mags = event.values.clone();
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        accels = event.values.clone();
                        break;
                }

                if (mags != null && accels != null) {
                    gravity = new float[9];
                    magnetic = new float[9];
                    SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                    float[] outGravity = new float[9];
                    SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
                    SensorManager.getOrientation(outGravity, values);

                    if(counter == 0){
                        initialAzimuth = values[0] * 57.2957795f;
                        initialPitch =values[1] * 57.2957795f;
                        initialRoll = values[2] * 57.2957795f;
                    }else {

                        azimuth = values[0] * 57.2957795f;
                        pitch = values[1] * 57.2957795f;
                        roll = values[2] * 57.2957795f;
                    }
                    mags = null;
                    accels = null;
                    counter++;
                }

                checkTilt();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void checkTilt() {
        if(Math.abs(azimuth - initialAzimuth) > THRESHOLD || 
                Math.abs(pitch - initialPitch) > THRESHOLD || Math.abs(roll - initialRoll) > THRESHOLD){
            // send Broadcast to update the UI
            Intent updateUiIntent = new Intent(getApplicationContext(), GameActivity.MyBroadcastReceiver.class);
            sendBroadcast(updateUiIntent);
        }
    }

    public class TiltMeasureBinder extends Binder {
        public TiltMeasureService getService() {
            // Return this instance of TiltMeasureService so clients can call public methods
            return TiltMeasureService.this;
        }
    }


}
