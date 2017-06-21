package com.iceoton.canomcakeadmin.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "http://canomcake-iton.rhcloud.com/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(String apiBaseUrl,Class<S> serviceClass) {
        Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(apiBaseUrl)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
