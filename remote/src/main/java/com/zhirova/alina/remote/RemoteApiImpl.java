package com.zhirova.alina.remote;


import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.domain.WeatherForecast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RemoteApiImpl implements RemoteApi {

    private static final String TAG = RemoteApiImpl.class.getSimpleName();
    private final WeatherApi.WeatherService weatherService;


    public RemoteApiImpl() {
        Retrofit retrofit = WeatherApi.getClient();
        weatherService = retrofit.create(WeatherApi.WeatherService.class);
    }


    @Override
    public List<City> loadCities(List<Pair<Double, Double>> locations) {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            double curLatitude = locations.get(i).first;
            double curLongitude = locations.get(i).second;

            Call<WeatherDay> callToday = weatherService.getToday(curLatitude, curLongitude,
                    WeatherApi.UNITS, WeatherApi.KEY);
            Call<WeatherForecast> callForecast = weatherService.getForecast(curLatitude, curLongitude,
                    WeatherApi.UNITS, WeatherApi.KEY);
            try {
                Response<WeatherDay> responseDay = callToday.execute();
                WeatherDay weatherDay = responseDay.body();
                Response<WeatherForecast> responseForecast = callForecast.execute();
                WeatherForecast weatherForecast = responseForecast.body();

                City curCity = new City(weatherForecast.getCity(), curLatitude, curLongitude,
                        weatherDay.getTemperature());
                curCity.setWeatherForecast(weatherForecast);
                cities.add(curCity);
            } catch (IOException e) {
                Log.e(TAG, "ERROR!", e);
            }
        }
        return cities;
    }


}
