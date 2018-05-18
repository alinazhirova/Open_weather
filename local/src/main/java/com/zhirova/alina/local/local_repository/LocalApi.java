package com.zhirova.alina.local.local_repository;


import android.util.Pair;
import com.zhirova.alina.domain.City;
import java.util.List;


public interface LocalApi {
    List<City> getCities();
    List<Pair<Double, Double>> getLocations();
    City getSelectedCity(String name);
    void deleteSelectedCity(String name);
    void refreshCities(List<City> cities);
}
