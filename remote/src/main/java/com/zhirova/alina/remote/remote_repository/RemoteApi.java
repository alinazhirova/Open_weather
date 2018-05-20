package com.zhirova.alina.remote.remote_repository;


import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.remote.exception.InternetException;
import com.zhirova.alina.remote.exception.NoForecastException;

import java.util.List;


public interface RemoteApi {
    List<City> loadCities(List<Pair<Double, Double>> locations) throws NoForecastException,
            InternetException;
}
