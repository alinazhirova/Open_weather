package com.zhirova.alina.presentation.screens.start;


import android.content.Context;
import android.util.Log;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.model.CitiesModel;
import com.zhirova.alina.model.CitiesModelImpl;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class StartPresenter implements StartContract.Presenter {

    private static final String TAG = StartPresenter.class.getSimpleName();
    private StartContract.View view;
    private CitiesModel citiesModel;
    private CompositeDisposable disposables;


    @Override
    public void subscribe(StartContract.View view, Context context) {
        this.view = view;
        citiesModel = new CitiesModelImpl(context);
        disposables = new CompositeDisposable();
        updateScreen();
    }


    @Override
    public void unsubsribe(StartContract.View view) {
        this.view = null;
        disposables.dispose();
    }


    @Override
    public void refreshCities() {
        updateScreen();
    }


    private void updateScreen() {
        if (view == null) return;
        Disposable curDisposable = citiesModel.getCities()
                .subscribe(data -> {
                    Log.d("BASKA", "data.size() = " + data.size());
                    view.updateCitiesList(data);
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
