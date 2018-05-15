package com.zhirova.alina.domain;


public class City {

    private final String name;
    private final double latitude;
    private final double longitude;
    private WeatherDay weatherDay;
    private WeatherForecast weatherForecast;


    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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


    public WeatherDay getWeatherDay() {
        return weatherDay;
    }


    public void setWeatherDay(WeatherDay weatherDay) {
        try {
            this.weatherDay = weatherDay.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
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
