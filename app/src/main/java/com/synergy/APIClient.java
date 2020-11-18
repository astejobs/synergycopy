package com.synergy;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient {

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("http://192.168.2.16:8081/api/")
                //  .baseUrl("http://192.168.1.114:8081/api/")
                // .baseUrl("http://192.168.1.116:8081/api/")
                //.baseUrl("http://ifarms.com.sg:8086/lsme/api/")
                //http://192.168.1.117:8082http://ifarms.com.sg:8086/lsme
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient()
                                .serializeNulls().create()))
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static UserService getUserServices() {
        UserService userService = getRetrofit().create(UserService.class);
        return userService;
    }

}