package com.example.wallpaper_weather.network;

import lombok.Singular;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final ServiceFactory sInstance = new ServiceFactory();

    private OpenWeatherService openWeatherService;

    private ServiceFactory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
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
