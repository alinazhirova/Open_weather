package com.zhirova.alina.presentation.screens.detail;


import android.content.Context;

import com.zhirova.alina.domain.City;


public class DetailContract {

    public interface View {
        void updateForecast(City city);
    }


    public interface Presenter {
        void subscribe(DetailContract.View view, String cityName, Context context);
        void unsubsribe(DetailContract.View view);
        void updateScreen(String cityName, Context context);
    }


}
