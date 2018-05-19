package com.zhirova.alina.presentation.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.zhirova.alina.data.geolocation.GeoCoordinates;
import com.zhirova.alina.data.geolocation.GeoCoordinatesImpl;
import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.model.CitiesModel;
import com.zhirova.alina.model.CitiesModelImpl;
import com.zhirova.alina.presentation.R;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private View mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.container);
        //showPermissionPreview();
        ////////////////////////////////////////////

        CitiesModel citiesModel = new CitiesModelImpl(this);
        CompositeDisposable disposables = new CompositeDisposable();
        Disposable curDisposable = citiesModel.getCities()
                .subscribe(data -> {
                    Log.d("BASKA", "data.size() = " + data.size());
                    print(data);
                }, throwable -> {
//                    if (throwable instanceof ServerException){
//                        view.showServerError();
//                    } else if (throwable instanceof InternetException) {
//                        view.showInternetError();
//                    } else {
//                        view.showError();
//                    }
                });
        disposables.add(curDisposable);
        //disposables.dispose();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mainLayout, R.string.location_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void showPermissionPreview() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(mainLayout, R.string.location_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_ACCESS_COARSE_LOCATION);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }


    void print(List<City> cities) {
        for (int i = 0; i < cities.size(); i++) {
            List<WeatherDay> weatherDayList = cities.get(i).getWeatherForecast().getDays();
            Log.d("BASKA", "==========================================");
            Log.d("BASKA", "==========================================");
            Log.d("BASKA", "==========================================");
            Log.d("BASKA", "City = " + cities.get(i).getName());
            Log.d("BASKA", "Latitude = " + cities.get(i).getLatitude());
            Log.d("BASKA", "Longitude = " + cities.get(i).getLongitude());
            for (int j = 0; j < weatherDayList.size(); j++) {
                WeatherDay weatherDayListDay = weatherDayList.get(j);
                Log.d("BASKA", "_______________________________________");
                Log.d("BASKA", "Temp = " + weatherDayListDay.getTemperature());
                Log.d("BASKA", "Pressure = " + weatherDayListDay.getPressure());
                Log.d("BASKA", "Description = " + weatherDayListDay.getDescription());
                Log.d("BASKA", "IconUrl = " + weatherDayListDay.getIconUrl());
                Log.d("BASKA", "Wind = " + weatherDayListDay.getWind());
                Log.d("BASKA", "Clouds = " + weatherDayListDay.getClouds());
                Log.d("BASKA", "Date = " + weatherDayListDay.getDate());
                Log.d("BASKA", "Time = " + weatherDayListDay.getTime());
            }
        }
    }


}
