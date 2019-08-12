package com.example.wallpaper_weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wallpaper_weather.model.WeatherDetails;
import com.example.wallpaper_weather.network.ServiceFactory;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wallpaper_weather.Constants.API_KEY;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String NOTIFICATION_CHANEL_ID_WEATHER_RETRIEVAL_SUCCESS
            = "weather-retrieval-success";
    private static final int NOTIFICATION_ID_WEATHER_RETRIEVAL_SUCCESS = 100;

    private ImageView ivWallpaper;

    private TextView tvLocation;
    private TextView tvState;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivWallpaper = findViewById(R.id.iv_wallpaper);
        tvLocation = findViewById(R.id.tv_location);
        tvState = findViewById(R.id.tv_state);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvCondition = findViewById(R.id.tv_condition);
        ImageView ivSettings = findViewById(R.id.iv_settings);
        ivSettings.setOnClickListener(v -> preferencesActivity.launch(MainActivity.this));

        fetchLocationWithPermissionCheck();
    }

    private void fetchLocationWithPermissionCheck() {
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.fetchLocationWithPermissionCheck(MainActivity.this);
    }

    private void setWallpaperWithPermissionCheck() {
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.setWallpaperWithPermissionCheck(MainActivity.this);
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    void fetchLocation() {
        Log.i(TAG, "fetchLocation");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 360000, 200000, this);
    }

    // Annotate a method which explains why the permission/s is/are needed.
    // It passes in a `PermissionRequest` object which can continue or abort the current permission
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForLocation(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    // Annotate a method which is invoked if the user doesn't grant the permissions
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    void showDeniedForLocation() {
        Snackbar.make(ivWallpaper, R.string.permission_location_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.button_allow,
                        v -> fetchLocationWithPermissionCheck()
                )
                .show();
    }

    // Annotates a method which is invoked if the user
    // chose to have the device "never ask again" about a permission
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    void showNeverAskForLocation() {
        Snackbar.make(ivWallpaper, R.string.permission_location_neverask, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    @NeedsPermission(Manifest.permission.SET_WALLPAPER)
    void setWallpaper() {
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.wallpaper) // Replace this with URL
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap wallpaperBitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        ivWallpaper.setImageBitmap(wallpaperBitmap);
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                        try {
                            wallpaperManager.setBitmap(wallpaperBitmap);
                            Toast.makeText(MainActivity.this,
                                    R.string.wallpaper_change_successful, Toast.LENGTH_SHORT)
                                    .show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Snackbar.make(ivWallpaper,
                                    R.string.wallpaper_change_successful, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, v -> fetchLocationWithPermissionCheck())
                                    .show();
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    @OnShowRationale(Manifest.permission.SET_WALLPAPER)
    void showRationaleForSetWallpaper(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_set_wallpaper_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.SET_WALLPAPER)
    void showDeniedForSetWallpaper() {
        Snackbar.make(ivWallpaper, R.string.permission_set_wallpaper_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.button_allow,
                        v -> fetchLocationWithPermissionCheck()
                )
                .show();
    }

    @OnNeverAskAgain(Manifest.permission.SET_WALLPAPER)
    void showNeverAskForSetWallpaper() {
        Snackbar.make(ivWallpaper, R.string.permission_set_wallpaper_neverask, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged provider: " + location.getProvider()
                + " coordinates: [" + location.getLatitude() + "," + location.getLongitude() + "]");
//        final String state = getStateName(location.getLatitude(), location.getLongitude());
        final String state = "";
        final Call<WeatherDetails> weatherDetailsByCoordinatesCall = ServiceFactory.getInstance()
                .getOpenWeatherService()
                .getWeatherDetailsByCoordinates(API_KEY, String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()));
        weatherDetailsByCoordinatesCall.enqueue(new Callback<WeatherDetails>() {
            @Override
            public void onResponse(Call<WeatherDetails> call, Response<WeatherDetails> response) {
                WeatherDetails weatherDetails = response.body();
                if (weatherDetails != null) {
                    String location = weatherDetails.getCityName() + ", "
                            + weatherDetails.getSys().getCountryCode();
                    tvLocation.setText(location);
                    if (state.isEmpty()) {
                        tvState.setText(R.string.unknown_state);
                    } else {
                        tvState.setText(state);
                    }
                    tvLatitude.setText(String.valueOf(weatherDetails.getCoordinates().getLatitude()));
                    tvLongitude.setText(String.valueOf(weatherDetails.getCoordinates().getLongitude()));
                    String temperatureInFahrenheit = String
                            .valueOf(convertKelvinToFahrenheit(weatherDetails.getMain().getTemperature()));
                    tvTemperature.setText(temperatureInFahrenheit);
                    tvHumidity.setText(String.format("%s%%", weatherDetails.getMain().getHumidity()));
                    if (weatherDetails.getWeatherConditionCodeList() != null
                            && !weatherDetails.getWeatherConditionCodeList().isEmpty())
                        tvCondition.setText(weatherDetails.getWeatherConditionCodeList().get(0)
                                .getWeatherParameterGroup());

                    showWeatherRetrieveSuccessfulNotification();

                    // NOTE: delegate the permission handling to generated method
                    MainActivityPermissionsDispatcher.setWallpaperWithPermissionCheck(MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<WeatherDetails> call, Throwable t) {
                t.printStackTrace();
                Snackbar.make(ivWallpaper,
                        R.string.get_weather_info_unsuccessful, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, v -> fetchLocationWithPermissionCheck())
                        .show();
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status == LocationProvider.AVAILABLE) {
            onProviderEnabled(provider);
        } else {
            onProviderDisabled(provider);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled provider: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled provider: " + provider);
    }

    private void showWeatherRetrieveSuccessfulNotification() {
        Log.i(TAG, "showWeatherRetrieveSuccessfulNotification start");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANEL_ID_WEATHER_RETRIEVAL_SUCCESS)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.weather_retrieved))
                .setContentText("Wohoo! Weather is here")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();

        Log.i(TAG, "showWeatherRetrieveSuccessfulNotification notifying..");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID_WEATHER_RETRIEVAL_SUCCESS, builder.build());

        Log.i(TAG, "showWeatherRetrieveSuccessfulNotification notification sent!");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "createNotificationChannel");

            CharSequence name = "weather-retrieval";
            String description = "Informs whether the weather retrieval was successful or not!!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel
                    = new NotificationChannel(NOTIFICATION_CHANEL_ID_WEATHER_RETRIEVAL_SUCCESS,
                    name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private double convertKelvinToFahrenheit(double temperatureInKelvin) {
        return Math.round(((temperatureInKelvin - 273.15) * 9 / 5) + 32);
    }

    private String getStateName(double latitude, double longitude) {
        String adminArea = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Log.i(TAG, "locality: " + addresses.get(0).getLocality());
                adminArea = addresses.get(0).getAdminArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adminArea;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }
}
