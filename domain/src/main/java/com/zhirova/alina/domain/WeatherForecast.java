package com.zhirova.alina.domain;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class WeatherForecast {

    public class CityForecast {
        String name;

        CityForecast(String name) {
            this.name = name;
        }

        @Override
        protected CityForecast clone() throws CloneNotSupportedException {
            return new CityForecast(this.name);
        }
    }


    @SerializedName("city")
    private CityForecast city;

    @SerializedName("list")
    private final List<WeatherDay> days = new ArrayList<>();


    public WeatherForecast(List<WeatherDay> days, CityForecast city) {
        this.days.clear();
        if (days != null){
            this.days.addAll(days);
        }
        try {
            this.city = city.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    public String getCity() {
        return city.name;
    }


    public List<WeatherDay> getDays() {
        return days;
    }


    @Override
    protected WeatherForecast clone() throws CloneNotSupportedException {
        return new WeatherForecast(this.days, this.city);
    }


}
