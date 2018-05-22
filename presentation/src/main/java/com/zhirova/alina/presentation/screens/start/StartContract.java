package com.zhirova.alina.presentation.screens.start;

import android.content.Context;
import android.util.Pair;

import com.zhirova.alina.domain.City;

import java.util.List;


public class StartContract {

    public interface View {
        void updateCitiesList(List<City> actualCities);
        void showLoader();
        void hideLoader();
        void showInfoAboutLackOfWeather();
        void showInternetError();
        void showError();
        void addCityUpdate(Pair<Double, Double> userLocation);
    }


    public interface Presenter {
        void subscribe(View view, Context context);
        void unsubsribe(View view);
        void refreshCities(Pair<Double, Double> userLocation);
        List<City> removeCity(String cityName);
    }


}
