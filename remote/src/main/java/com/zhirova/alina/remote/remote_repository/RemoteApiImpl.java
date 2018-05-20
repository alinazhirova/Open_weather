package com.zhirova.alina.remote.remote_repository;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.domain.WeatherForecast;
import com.zhirova.alina.remote.exception.InternetException;
import com.zhirova.alina.remote.exception.NoForecastException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RemoteApiImpl implements RemoteApi {

    private static final String TAG = RemoteApiImpl.class.getSimpleName();
    private final WeatherApi.WeatherService weatherService;
    private final Context context;


    public RemoteApiImpl(Context context) {
        this.context = context;
        Retrofit retrofit = WeatherApi.getClient();
        weatherService = retrofit.create(WeatherApi.WeatherService.class);
    }


    @Override
    public List<City> loadCities(List<Pair<Double, Double>> locations) throws NoForecastException,
            InternetException {
        List<City> cities = new ArrayList<>();
        if (isOnline()) {
            for (int i = 0; i < locations.size(); i++) {
                double curLatitude = locations.get(i).first;
                double curLongitude = locations.get(i).second;

                Call<WeatherDay> callToday = weatherService.getToday(curLatitude, curLongitude,
                        WeatherApi.UNITS, WeatherApi.KEY);
                Call<WeatherForecast> callForecast = weatherService.getForecast(curLatitude, curLongitude,
                        WeatherApi.UNITS, WeatherApi.KEY);

                if (callToday != null && callForecast != null) {
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
                } else {
                    throw new NoForecastException("There is no weather forecast!");
                }
            }
        } else {
            throw new InternetException("There is impossible to connect to network!");
        }
        return cities;
    }


    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
