
package com.example.wallpaper_weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * sys
 * sys.type Internal parameter
 * sys.id Internal parameter
 * sys.message Internal parameter
 * sys.country Country code (GB, JP etc.)
 * sys.sunrise Sunrise time, unix, UTC
 * sys.sunset Sunset time, unix, UTC
 */
@Data
public class Sys {

    @SerializedName("country")
    @Expose
    private String countryCode;

    @SerializedName("sunrise")
    @Expose
    private long sunriseTimeInMillis;

    @SerializedName("sunset")
    @Expose
    private long sunsetTimeInMillis;

}
