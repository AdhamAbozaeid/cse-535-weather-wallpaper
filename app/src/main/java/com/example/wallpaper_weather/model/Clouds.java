package com.example.wallpaper_weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * clouds
 * clouds.all Cloudiness, %
 */
@Data
public class Clouds {

    @SerializedName("all")
    @Expose
    private double cloudinessPercentage;

}
