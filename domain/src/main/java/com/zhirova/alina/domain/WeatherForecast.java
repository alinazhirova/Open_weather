package com.zhirova.alina.domain;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class WeatherForecast {

    @SerializedName("list")
    private final List<WeatherDay> days = new ArrayList<>();


    public WeatherForecast(List<WeatherDay> days) {
        this.days.clear();
        if (days != null){
            this.days.addAll(days);
        }
    }


    public List<WeatherDay> getDays() {
        return days;
    }


    @Override
    protected WeatherForecast clone() throws CloneNotSupportedException {
        return new WeatherForecast(this.days);
    }


}
