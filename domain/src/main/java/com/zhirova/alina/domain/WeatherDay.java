package com.zhirova.alina.domain;


import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class WeatherDay {

    public class Main {
        float temp;
        float pressure;

        Main(float temp, float pressure) {
            this.temp = temp;
            this.pressure = pressure;
        }

        @Override
        protected Main clone() throws CloneNotSupportedException {
            return new Main(this.temp, this.pressure);
        }
    }


    public class WeatherDescription {
        String icon;
    }


    public class Wind {
        float speed;

        Wind(float speed) {
            this.speed = speed;
        }

        @Override
        protected Wind clone() throws CloneNotSupportedException {
            return new Wind(this.speed);
        }
    }


    @SerializedName("main")
    private Main mainInfo;

    @SerializedName("weather")
    private List<WeatherDescription> desctiption = new ArrayList<>();

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timeStamp;


    public WeatherDay(Main mainInfo, List<WeatherDescription> desctiption, Wind wind,
                      String city, long timeStamp) {
        try {
            this.mainInfo = mainInfo.clone();
            this.wind = wind;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.desctiption.clear();
        if (desctiption != null) {
            this.desctiption.addAll(desctiption);
        }
        this.city = city;
        this.timeStamp = timeStamp;
    }


    public String getTempWithDegree() {
        return (mainInfo.temp + "\u00B0");
    }


    public String getPressure() {
        return (mainInfo.pressure + " hpa");
    }


    public String getWind() {
        return (wind.speed + " m/s");
    }


    public String getCity() {
        return city;
    }


    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + desctiption.get(0).icon + ".png";
    }


    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM", Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }


    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("HH:00", Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }


    @Override
    protected WeatherDay clone() throws CloneNotSupportedException {
        return new WeatherDay(this.mainInfo, this.desctiption, this.wind,
                this.city, this.timeStamp);
    }


}
