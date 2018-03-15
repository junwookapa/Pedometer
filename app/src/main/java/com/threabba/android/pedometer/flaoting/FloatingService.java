package com.threabba.android.pedometer.flaoting;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.threabba.android.pedometer.App;

public class FloatingService extends Service {
    FloatingView view;
    IBinder mBinder;
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class FloatingServiceBinder extends Binder {
        public FloatingService getService() {
            return FloatingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        view = new FloatingView(this);
        App.getRXOnChagedAppSchedulingNotifier().subscribe(
                (Boolean isForeGRound) ->{
                    if(isForeGRound){
                        view.destroyView();
                    }else{
                        view.createView(FloatingService.this);
                    }}, error->{});
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        view.destroyView();
        return super.stopService(name);
    }
}
