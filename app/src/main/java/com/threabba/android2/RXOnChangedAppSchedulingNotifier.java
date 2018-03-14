package com.threabba.android2;

import android.util.Log;

import com.threabba.android2.step.RXStepDetector;
import com.threabba.android2.step.StepDetector;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;


public class RXOnChangedAppSchedulingNotifier implements ObservableOnSubscribe<Boolean>{

    private Set<ObservableEmitter<Boolean>> mEmitters = new HashSet<>();
    private Observable<Boolean> mObservable;
    private boolean mAppScheduling;
    @Override
    public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
        Log.e("섭스 크라이브", "섭스 크라이브 : ");
        if(!mEmitters.contains(emitter)){
            Log.e("섭스 크라이브", "없는디 : ");
            mEmitters.add(emitter);
            emitter.onNext(mAppScheduling);
        }

        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                emitter.onComplete();
                mEmitters.remove(emitter);

            }
        });
    }
    public void notify(boolean isForeground){
        mAppScheduling = isForeground;
        for(ObservableEmitter<Boolean> o: mEmitters){
            o.onNext(isForeground);
        }
    }

    public io.reactivex.Observable<Boolean> getObservable(){
        if(mObservable == null){
            mObservable = Observable.create(this);
        }
        return mObservable;
    }
}