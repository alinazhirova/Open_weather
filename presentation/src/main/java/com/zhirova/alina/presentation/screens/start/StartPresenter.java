package com.zhirova.alina.presentation.screens.start;


import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.local.local_repository.LocalApi;
import com.zhirova.alina.local.local_repository.LocalApiImpl;
import com.zhirova.alina.model.interaction.CitiesModel;
import com.zhirova.alina.model.interaction.CitiesModelImpl;
import com.zhirova.alina.remote.exception.InternetException;
import com.zhirova.alina.remote.exception.NoForecastException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class StartPresenter implements StartContract.Presenter {

    private static final String TAG = StartPresenter.class.getSimpleName();
    public static CompositeDisposable disposables;
    public static Long delay = 1L;

    private StartContract.View view;
    private CitiesModel citiesModel;
    private Context context;


    @Override
    public void subscribe(StartContract.View view, Context context) {
        this.view = view;
        this.context = context;
        citiesModel = new CitiesModelImpl(context);
        disposables = new CompositeDisposable();
        updateScreen(null);
    }


    @Override
    public void unsubsribe(StartContract.View view) {
        this.view = null;
        disposables.dispose();
    }


    @Override
    public void refreshCities(Pair<Double, Double> userLocation) {
        updateScreen(userLocation);
    }


    @Override
    public List<City> removeCity(String cityName) {
        LocalApi localApi = new LocalApiImpl(context);
        localApi.deleteSelectedCity(cityName);
        return localApi.getCities();
    }


    private void updateScreen(Pair<Double, Double> userLocation) {
        if (view == null) return;
        Disposable curDisposable = citiesModel.getCities(userLocation)
                .repeatWhen(objectObservable -> objectObservable.delay(delay, TimeUnit.MINUTES))
                .subscribe(data -> {
                    Log.d("BASKA", "delay = " + delay);
                    if (data.size() == 0) {
                        view.showLoader();
                    } else {
                        view.updateCitiesList(data);
                        //print(data);
                    }
                }, throwable -> {
                    Log.d("BASKA", "throwable");
                    view.hideLoader();
                    if (throwable instanceof InternetException){
                        view.showInternetError();
                    } else if (throwable instanceof NoForecastException) {
                        view.showInfoAboutLackOfWeather();
                    } else {
                        view.showError();
                    }
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
