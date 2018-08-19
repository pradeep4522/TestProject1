package com.testproject.testproject.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.testproject.testproject.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {
    private static Helper instance = null;
    Context mContext;

    private Helper(Context context) {
        this.mContext = context;
    }

    public static Helper getdataInstance(Context context) {

        if (instance == null) {
            instance = new Helper(context);
        }

        return instance;
    }

    /*  Method for checking network connection of device */

    public boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    /* method that will return interface for caaling webservice*/

    public ApiInterfaces getrestadapter() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "Application/JSON")
                                        .build();
                                return chain.proceed(request);
                            }
                        }).build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        return retrofit.create(ApiInterfaces.class);
    }

    /* Method to show alert message*/

    public void showMessageAlertDialog(String msg, Activity activity) {
        final AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setMessage(msg);
        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final Dialog d = ab.create();
        d.show();
    }


}
