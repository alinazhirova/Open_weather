package com.zhirova.alina.presentation.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import com.zhirova.alina.data.geolocation.GeoCoordinates;
import com.zhirova.alina.data.geolocation.GeoCoordinatesImpl;
import com.zhirova.alina.domain.City;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.remote.RemoteApi;
import com.zhirova.alina.remote.RemoteApiImpl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private View mainLayout;
    private GeoCoordinates geoCoordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.container);
        //showPermissionPreview();


        Pair<Double, Double> coord = new Pair<>(53.22522522522522, 50.20128527695212);
        List<Pair<Double, Double>> locations = new ArrayList<>();
        locations.add(coord);

        RemoteApi remoteApi = new RemoteApiImpl();
        List<City> cities = remoteApi.loadCities(locations);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geoCoordinates = new GeoCoordinatesImpl(this);
                geoCoordinates.findCurLocation();
            } else {
                Snackbar.make(mainLayout, R.string.location_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void showPermissionPreview() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geoCoordinates = new GeoCoordinatesImpl(this);
            geoCoordinates.findCurLocation();
        } else {
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


}
