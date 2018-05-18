package com.zhirova.alina.domain;


import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    public class Weather {
        String description;
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


    public class Clouds {
        int all;

        Clouds(int all) {
            this.all = all;
        }

        @Override
        protected Clouds clone() throws CloneNotSupportedException {
            return new Clouds(this.all);
        }
    }


    @SerializedName("main")
    private Main mainInfo;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("weather")
    private List<Weather> weather = new ArrayList<>();

    @SerializedName("dt_txt")
    private String timeStamp;


    public WeatherDay(Main mainInfo, Wind wind, Clouds clouds, List<Weather> weather,
                      String timeStamp) {
        try {
            this.mainInfo = mainInfo.clone();
            this.wind = wind.clone();
            this.clouds = clouds.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.weather.clear();
        if (weather != null) {
            this.weather.addAll(weather);
        }
        this.timeStamp = timeStamp;
    }


    public String getTemperature() {
        return (mainInfo.temp + "\u00B0");
    }


    public String getPressure() {
        return (mainInfo.pressure + " hpa");
    }


    public String getWind() {
        return (wind.speed + " m/s");
    }


    public String getClouds() {
        return (clouds.all + " %");
    }


    public String getDescription() {
        return weather.get(0).description;
    }


    public String getIconUrl() {
        return "http://openweathermap.org/img/w/" + weather.get(0).icon + ".png";
    }


    public String getDate() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = dateFormat.parse(timeStamp);
            dateFormat = new SimpleDateFormat("E, dd MMMM", Locale.ENGLISH);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    public String getTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = dateFormat.parse(timeStamp);
            dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    @Override
    protected WeatherDay clone() throws CloneNotSupportedException {
        return new WeatherDay(this.mainInfo, this.wind, this.clouds, this.weather, this.timeStamp);
    }


}
