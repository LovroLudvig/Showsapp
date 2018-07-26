package com.example.ledeni56.showsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class NetworkingSupportMethods {
    private static ProgressDialog progressDialog;
    private static final String BASE_URL = "https://api.infinum.academy";

    public static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static ApiService initApiService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(ApiService.class);
    }

    public static void showError(Context context,String text) {
        new AlertDialog.Builder(context)
                .setTitle("Error!")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }
    public static void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void showProgress(Context context) {
        progressDialog = ProgressDialog.show(context, "Please wait", "Loading", true, false);
    }
}
