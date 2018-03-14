package com.threabba.android.pedometer.components;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.threabba.android2.App;
import com.threabba.android.pedometer.fragments.StepMiniView;
import com.threabba.android2.step.StepDetector;
import com.threabba.android.pedometer.db.Record;

import java.text.DecimalFormat;


/**
 * Created by junwoo on 2016-12-02.
 */

public class StepService extends Service implements View.OnTouchListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    final IBinder mBinder = new StepServiceBinder();

    // overay view value
    private boolean mIsActiveOveray;
    private View topLeftView;

    private StepMiniView mMiniOverlayView;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("0.##");
    private App mApp;
    // overay view thread


    /**
     * 1. 생명주기 관리
     * **/
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class StepServiceBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();
        mIsActiveOveray =false;
        mApp = (App)getApplication();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 2. 오버레이 뷰 관련
     * 참고 및 출처: https://gist.github.com/bjoernQ
     * **/

    // 오버레이 뷰 생성
    public void initOverayView(){
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mMiniOverlayView = new StepMiniView(this);
        mMiniOverlayView.setOnTouchListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        wm.addView(mMiniOverlayView, params);

        topLeftView = new View(this);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);
        mIsActiveOveray =true;
        //Record record = mApp.getRecord();
        //mMiniOverlayView.setDist(mDecimalFormat.format(record.getDistance())+"KM");
       // mMiniOverlayView.setStep(record.getStep_count()+"");
    }
    // 오버레이 뷰 제거
    public void finishOverayView(){
        if (mMiniOverlayView != null) {
            wm.removeView(mMiniOverlayView);
            wm.removeView(topLeftView);
            mMiniOverlayView = null;
            topLeftView = null;
        }
        //mApp.updateRecord();
        mIsActiveOveray =false;
    }

    // 터치 이벤트
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            mMiniOverlayView.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY=" + topLeftLocationOnScreen[1]);
            System.out.println("originalY=" + originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mMiniOverlayView.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(mMiniOverlayView, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }
    public boolean isOverrayActive(){
        return this.mIsActiveOveray;
    }
    /**
     * 3. 스텝 관련
     * **/



    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER /*|
            Sensor.TYPE_MAGNETIC_FIELD |
            Sensor.TYPE_ORIENTATION*/);
        mSensorManager.registerListener(mStepDetector,
                mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }


}
