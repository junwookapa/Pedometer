package com.threabba.android.util;

/**
 * Created by jun on 16. 12. 4.
 * ref : http://javacan.tistory.com/entry/maintainable-async-processing-code-based-on-AsyncTask
 */

public interface AsyncCallback<T> {
    void onResult(T result);
    void exceptionOccured(Exception e);
    void cancelled();
}
