package com.example.wallpaper_weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * main
 * main.temp Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * main.pressure Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa
 * main.humidity Humidity, %
 * main.temp_min Minimum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * main.temp_max Maximum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * main.sea_level Atmospheric pressure on the sea level, hPa
 * main.grnd_level Atmospheric pressure on the ground level, hPa
 */
@Data
public class Main {

    @SerializedName("temp")
    @Expose
    private double temperature;

    @SerializedName("pressure")
    @Expose
    private double pressure;

    @SerializedName("humidity")
    @Expose
    private double humidity;

    @SerializedName("temp_min")
    @Expose
    private double minTemperature;

    @SerializedName("temp_max")
    @Expose
    private double maxTemperature;

    @SerializedName("sea_level")
    @Expose
    private double atmosphericPressureOnSeaLevel;

    @SerializedName("grnd_level")
    @Expose
    private double atmosphericPressureOnGroundLevel;

}
