package com.threabba.android2.http;

import com.threabba.android2.model.Address;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NaverAPI {
    @GET("/v1/map/reversegeocode")
    Observable<Address> getAddress(
            @QueryMap(encoded = true) Map<String, String> options
    );
}
