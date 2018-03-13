package com.threabba.android2.step;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by ETRI LSAR Project Team on 2018-03-13.
 */

public class RXStepDetector extends StepDetector implements ObservableOnSubscribe<Integer> {

    @Override
    public void subscribe(final ObservableEmitter<Integer> emitter) {
        setStepListener(new OnStepListener() {
            @Override
            public void onStep(int step) {
                emitter.onNext(step);
            }
        });

        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                emitter.onComplete();
                stop();
            }
        });
        start();
    }

    public static Observable<Integer> createObservable(){

        return Observable.create(new RXStepDetector());
    }

}
