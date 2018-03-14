package com.threabba.android2.step;

import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Cancellable;

public class RXStepDetector extends StepDetector implements ObservableOnSubscribe<Integer> {

    private Set<ObservableEmitter<Integer>> mEmitters= new HashSet<>();
    @Override
    public void subscribe(final ObservableEmitter<Integer> emitter) {

        mEmitters.add(emitter);
        setStepListener(new OnStepListener() {
            @Override
            public void onStep(int step) {
                for(ObservableEmitter<Integer> o: mEmitters){
                    o.onNext(step);
                }
            }
        });

        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                emitter.onComplete();
                mEmitters.remove(emitter);

                if(mEmitters.isEmpty()){
                    stop();
                }
            }
        });
        start();
        Log.e("사이즈", "사이즈 : "+mEmitters.size());
    }

    public static Observable<Integer> createObservable(){

        return Observable.create(new RXStepDetector());
    }

}
