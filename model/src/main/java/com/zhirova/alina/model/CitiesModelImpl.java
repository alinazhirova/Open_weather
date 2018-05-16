package com.zhirova.alina.model;


import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.remote.RemoteApi;
import com.zhirova.alina.remote.RemoteApiImpl;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CitiesModelImpl implements CitiesModel {

    private RemoteApi remoteApi;


    public CitiesModelImpl() {
        this.remoteApi = new RemoteApiImpl();
    }


    @Override
    public Observable<List<City>> getCities() {
        return Observable.<List<City>>create(emitter -> {
            try {
                Pair<Double, Double> coord = new Pair<>(53.22522522522522, 50.20128527695212);
                List<Pair<Double, Double>> locations = new ArrayList<>();
                locations.add(coord);
                List<City> cities = remoteApi.loadCities(locations);

                emitter.onNext(new ArrayList<>(cities));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }


}
