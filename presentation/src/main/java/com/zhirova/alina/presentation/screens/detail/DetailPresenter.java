package com.zhirova.alina.presentation.screens.detail;


import android.content.Context;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.local.local_repository.LocalApi;
import com.zhirova.alina.local.local_repository.LocalApiImpl;


public class DetailPresenter implements  DetailContract.Presenter {

    private DetailContract.View view;


    @Override
    public void subscribe(DetailContract.View view, String cityName, Context context) {
        this.view = view;
        updateScreen(cityName, context);
    }


    @Override
    public void unsubsribe(DetailContract.View view) {
        this.view = null;
    }


    @Override
    public void updateScreen(String cityName, Context context) {
        LocalApi localApi = new LocalApiImpl(context);
        City curCity = localApi.getSelectedCity(cityName);
        view.updateForecast(curCity);
    }


}
