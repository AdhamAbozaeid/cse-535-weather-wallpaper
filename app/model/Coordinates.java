package com.example.wallpaper_weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * coordinates of the place
 *
 * coord.lon City geo location, longitude
 * coord.lat City geo location, latitude
 */
@Data
public class Coordinates {

    @SerializedName("lon")
    @Expose
    private double longitude;

    @SerializedName("lat")
    @Expose
    private double latitude;

}
