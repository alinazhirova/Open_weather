package com.zhirova.alina.domain;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WeatherDay {

    public class WeatherTemperature {
        Double temp;
        Double temp_min;
        Double temp_max;

        WeatherTemperature(Double temp, Double temp_min, Double temp_max) {
            this.temp = temp;
            this.temp_min = temp_min;
            this.temp_max = temp_max;
        }

        @Override
        protected WeatherTemperature clone() throws CloneNotSupportedException {
            return new WeatherTemperature(this.temp, this.temp_min, this.temp_max);
        }
    }


    public class WeatherDescription {
        String icon;
    }


    @SerializedName("main")
    private WeatherTemperature temperature;

    @SerializedName("weather")
    private List<WeatherDescription> desctiption = new ArrayList<>();

    @SerializedName("speed")
    private float wind;

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timeStamp;


    public WeatherDay(WeatherTemperature temperature, List<WeatherDescription> desctiption) {
        try {
            this.temperature = temperature.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.desctiption.clear();
        if (desctiption != null) {
            this.desctiption.addAll(desctiption);
        }
    }


    public Calendar getDate() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeStamp * 1000);
        return date;
    }


    public String getTemp() {
        return String.valueOf(temperature.temp);
    }


    public String getTempMin() {
        return String.valueOf(temperature.temp_min);
    }


    public String getTempMax() {
        return String.valueOf(temperature.temp_max);
    }


    public String getTempWithDegree() {
        return String.valueOf(temperature.temp.intValue()) + "\u00B0";
    }


    public float getWind() {
        return wind;
    }


    public String getCity() {
        return city;
    }


    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + desctiption.get(0).icon + ".png";
    }


    @Override
    protected WeatherDay clone() throws CloneNotSupportedException {
        return new WeatherDay(this.temperature, this.desctiption);
    }


}
