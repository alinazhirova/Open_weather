package com.zhirova.alina.model.interaction;


import android.util.Pair;

import com.zhirova.alina.domain.City;
import java.util.List;
import io.reactivex.Observable;


public interface CitiesModel {
    Observable<List<City>> getCities(Pair<Double, Double> userLocation);
}
