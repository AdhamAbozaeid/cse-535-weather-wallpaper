package com.example.wallpaper_weather.network;

import com.example.wallpaper_weather.model.WeatherDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("data/2.5/weather")
    Call<WeatherDetails> getWeatherDetailsByCoordinates(@Query("appid") String id,
                                                        @Query("lat") String latitude,
                                                        @Query("lon") String longitude);

}
