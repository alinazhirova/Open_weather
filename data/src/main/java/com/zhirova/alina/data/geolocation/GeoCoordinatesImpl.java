package com.zhirova.alina.data.geolocation;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class GeoCoordinatesImpl implements GeoCoordinates {

    private static final String TAG = GeoCoordinatesImpl.class.getSimpleName();
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private static List<Pair<Double, Double>> coordinates;


    public GeoCoordinatesImpl(Context context) {
        this.context = context;
    }


    public static  List<Pair<Double, Double>> getCoordinates() {
        return coordinates;
    }


    @Override
    public void findCurLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Pair<Double, Double> position = new Pair<>(location.getLatitude(),
                                location.getLongitude());
                        coordinates.add(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERROR", "Error trying to get last GPS location");
                    }
                });
    }


}
