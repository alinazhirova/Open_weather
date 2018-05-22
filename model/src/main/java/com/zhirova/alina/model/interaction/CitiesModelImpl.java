package com.zhirova.alina.model.interaction;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.local.local_repository.LocalApi;
import com.zhirova.alina.local.local_repository.LocalApiImpl;
import com.zhirova.alina.remote.remote_repository.RemoteApi;
import com.zhirova.alina.remote.remote_repository.RemoteApiImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CitiesModelImpl implements CitiesModel {

    private static final String TAG = CitiesModelImpl.class.getSimpleName();
    private final String PREFS_NAME = "COORDINATES";
    private final String BUNDLE_LOCATION_LAT = "CUR_POSITION_LATITUDE";
    private final String BUNDLE_LOCATION_LONG = "CUR_POSITION_LONG";

    private Context context;
    private LocalApi localApi;
    private RemoteApi remoteApi;


    public CitiesModelImpl(Context context) {
        this.context = context;
        this.localApi = new LocalApiImpl(context);
        this.remoteApi = new RemoteApiImpl(context);
    }


    @Override
    public Observable<List<City>> getCities(Pair<Double, Double> userLocation) {
        return Observable.<List<City>>create(emitter -> {
            try {
                List<Pair<Double, Double>> locations = localApi.getLocations();
                if (locations.size() == 0) {
                    SharedPreferences sPref = context.getSharedPreferences(PREFS_NAME,
                            Context.MODE_PRIVATE);
                    Double latitude = Double.valueOf(sPref.getString(BUNDLE_LOCATION_LAT, ""));
                    Double longitude = Double.valueOf(sPref.getString(BUNDLE_LOCATION_LONG, ""));
                    Pair<Double, Double> coord = new Pair<>(latitude, longitude);
                    locations.add(coord);
                }
                if (userLocation != null) {
                    locations.add(userLocation);
                }
                List<City> cities = localApi.getCities();
                emitter.onNext(new ArrayList<>(cities));

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Log.e(TAG, "ERROR", e);
                }

                cities = remoteApi.loadCities(locations);
                if (cities.size() != 0) {
                    localApi.refreshCities(cities);
                }
                emitter.onNext(new ArrayList<>(cities));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }


}
