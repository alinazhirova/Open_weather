package com.zhirova.alina.remote;


import android.util.Pair;

import com.zhirova.alina.domain.City;
import java.util.List;


public interface RemoteApi {
    List<City> loadCities(List<Pair<Double, Double>> locations);
}
