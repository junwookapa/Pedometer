package com.threabba.android2.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.threabba.android2.App;

/**
 * 스텝 발생 함수 클래스
 * 출처 : https://github.com/bagilevi/android-pedometer
 */

public class StepDetector implements SensorEventListener {

    private final static String TAG = "StepDetector";
    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;

    private OnStepListener mStepListener;
    private int mStep;
    private boolean isRunning = false;
    public StepDetector() {
        onInitialize();
    }

    private void onInitialize(){
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    //public void onSensorChanged(int sensor, float[] values) {
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER && mStepListener != null) {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;

                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                mStepListener.onStep(mStep++);
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    public void start(){
        if(!isRunning){
            SensorManager appSensorManager = App.getSensorManager();
            Sensor accSensor = appSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            appSensorManager.registerListener(StepDetector.this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
            Log.e("스타트", "스타트");
            isRunning = true;
        }
    }

    public void stop(){
        if(!isRunning){
            Log.e("정지", "정지");
            isRunning = false;
            SensorManager appSensorManager = App.getSensorManager();
            appSensorManager.unregisterListener(StepDetector.this);
        }
    }

    public void setStepListener(OnStepListener listener){
        synchronized (this){
            this.mStepListener = listener;
        }
    }

    public interface OnStepListener{
        void onStep(int step);
    }
}
