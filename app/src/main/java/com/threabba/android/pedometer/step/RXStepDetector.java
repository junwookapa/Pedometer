package com.threabba.android.pedometer.step;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RXStepDetector extends StepDetector implements ObservableOnSubscribe<Integer> {

    private Set<ObservableEmitter<Integer>> mEmitters= new HashSet<>();

    @Override
    public void subscribe(final ObservableEmitter<Integer> emitter) {

        mEmitters.add(emitter);
        setStepListener(step -> {
            for(ObservableEmitter<Integer> o : mEmitters ){
                o.onNext(step);
            }
        });
        emitter.setCancellable(() -> {
            emitter.onComplete();
            mEmitters.remove(emitter);
            if(mEmitters.isEmpty()){
                stop();
            }
        });
        start();
    }

    public static Observable<Integer> createObservable(){
        return Observable.create(new RXStepDetector());
    }

}
