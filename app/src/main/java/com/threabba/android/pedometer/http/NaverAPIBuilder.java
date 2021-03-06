package com.threabba.android.pedometer.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.threabba.android.pedometer.App;
import com.threabba.android.pedometer.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverAPIBuilder {
    private static final String baseURL ="https://openapi.naver.com";
    private static Retrofit retrofit;

    private static Retrofit createRetrofit(){
        OkHttpClient.Builder okClient = new OkHttpClient.Builder();
        okClient.addInterceptor(chain -> {
            Request originalReq = chain.request();
            Request.Builder requestBuilder = originalReq.newBuilder()
                    .addHeader("X-Naver-Client-Id", App.getRes().getString(R.string.api_naver_client_id))
                    .addHeader("X-Naver-Client-Secret", App.getRes().getString(R.string.api_naver_client_secret));
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient.build())
                .build();

        return retrofit;
    }
    public static <T> T createAPI(Class<T> clazz){
        if(retrofit == null){
            retrofit = createRetrofit();
        }
        T t = retrofit.create(clazz);
        if(t != null){
            return t;
        }else{
            throw new NullPointerException("Not valid class");
        }
    }
}
