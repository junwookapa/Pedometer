package com.threabba.android2.flaoting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.threabba.android2.App;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FloatingService extends Service {
    FloatingView view;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        view = new FloatingView(this);

        App.getRXOnChagedAppSchedulingNotifier().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean isForeGround) {
                Log.e("스타트 서비스", "현재그라운드 상태 : "+isForeGround);
                if(isForeGround){
                    view.destroyView();
                }else{
                    view.createView(FloatingService.this);

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                view.destroyView();
            }
        });



        return START_STICKY;
    }

}
