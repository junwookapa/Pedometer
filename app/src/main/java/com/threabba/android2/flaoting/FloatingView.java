package com.threabba.android2.flaoting;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.threabba.android.pedometer.db.Record;
import com.threabba.android.pedometer.fragments.StepMiniView;
import com.threabba.android2.App;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ETRI LSAR Project Team on 2018-03-14.
 */

public class FloatingView extends View implements View.OnTouchListener{

    private WindowManager mWindowManager;
    private StepMiniView mMiniOverlayView;
    private View topLeftView;
    private boolean mIsActiveOveray;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;

    public FloatingView(Context context) {
        super(context);
    }

    public void createView(Context context){
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mMiniOverlayView = new StepMiniView(context);
        mMiniOverlayView.setOnTouchListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        mWindowManager.addView(mMiniOverlayView, params);

        topLeftView = new View(context);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        mWindowManager.addView(topLeftView, topLeftParams);
        mIsActiveOveray =true;

        mMiniOverlayView.setDist("0KM");
        mMiniOverlayView.setStep("0");

    }

    public void destroyView(){
        if (mMiniOverlayView != null) {
            mWindowManager.removeView(mMiniOverlayView);
            mWindowManager.removeView(topLeftView);
            mMiniOverlayView = null;
            topLeftView = null;
        }
        mIsActiveOveray =false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
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

            mWindowManager.updateViewLayout(mMiniOverlayView, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }
}
