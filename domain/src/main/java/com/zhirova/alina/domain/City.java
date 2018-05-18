package com.zhirova.alina.domain;


public class City {

    private final String name;
    private final double latitude;
    private final double longitude;
    private final String curTemperature;
    private WeatherForecast weatherForecast;


    public City(String name, double latitude, double longitude, String curTemperature) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.curTemperature = curTemperature;
    }


    public String getName() {
        return name;
    }


    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public String getCurTemperature() {
        return curTemperature;
    }


    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }


    public void setWeatherForecast(WeatherForecast weatherForecast) {
        try {
            this.weatherForecast = weatherForecast.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


}
