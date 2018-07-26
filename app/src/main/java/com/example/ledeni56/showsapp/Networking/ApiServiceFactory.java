package com.example.ledeni56.showsapp.Networking;

import com.example.ledeni56.showsapp.Interfaces.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class ApiServiceFactory {
    private static final String BASE_URL = "https://api.infinum.academy";
    private static ApiService apiService;

    public static ApiService get(){
        if (apiService==null){
            apiService=initApiService();
        }
        return apiService;
    }

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
}
