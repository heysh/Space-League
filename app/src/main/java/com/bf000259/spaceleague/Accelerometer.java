package com.bf000259.spaceleague;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Class responsible for handling the accelerometer.
 * @author Harshil Surendralal bf000259
 */
public class Accelerometer implements SensorEventListener {
    protected Sensor sensor;
    protected float tilt;

    /**
     * Create an object of type Accelerometer that will measure the tilt of the device.
     * @param context The context in which the sensor will be applied.
     */
    public Accelerometer(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Update the tilt value every time it changes.
     * @param event The event from which the tilt will be recorded.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        tilt = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
