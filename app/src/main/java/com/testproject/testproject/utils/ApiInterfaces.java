package com.testproject.testproject.utils;

import com.testproject.testproject.pojo.Facts;

import retrofit2.http.GET;
import rx.Observable;

public interface ApiInterfaces {

    @GET("")
    Observable<Facts> callFactsService();
}
