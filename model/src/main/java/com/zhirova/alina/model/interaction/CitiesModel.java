package com.zhirova.alina.model.interaction;


import com.zhirova.alina.domain.City;
import java.util.List;
import io.reactivex.Observable;


public interface CitiesModel {
    Observable<List<City>> getCities();
}
