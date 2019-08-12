package com.example.wallpaper_weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * more info Weather condition codes
 *
 * weather (more info Weather condition codes)
 * weather.id Weather condition id
 * weather.main Group of weather parameters (Rain, Snow, Extreme etc.)
 * weather.description Weather condition within the group
 * weather.icon Weather icon id
 */
@Data
public class WeatherConditionCode {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("main")
    @Expose
    private String weatherParameterGroup;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("icon")
    @Expose
    private String icon;

}
