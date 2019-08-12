package com.example.wallpaper_weather.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final ServiceFactory sInstance = new ServiceFactory();

    private OpenWeatherService openWeatherService;


    private ServiceFactory() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://samples.openweathermap.org/")
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherService = retrofit.create(OpenWeatherService.class);
    }

    public static ServiceFactory getInstance() {
        return sInstance;
    }

    public OpenWeatherService getOpenWeatherService() {
        return openWeatherService;
    }

}
