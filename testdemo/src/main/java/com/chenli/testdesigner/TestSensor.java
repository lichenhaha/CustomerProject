package com.chenli.testdesigner;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/1/23.
 */

public class TestSensor extends AppCompatActivity{

    private SensorManager manager;
    private Sensor sensor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            LogUtils.e("x = " + x + " , y = " + y + " , z = " + z);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }
}
