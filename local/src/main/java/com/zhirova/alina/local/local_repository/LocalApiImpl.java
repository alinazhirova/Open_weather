package com.zhirova.alina.local.local_repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.local.database.DatabaseApi;
import com.zhirova.alina.local.database.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class LocalApiImpl implements LocalApi {

    private SQLiteDatabase database;


    public LocalApiImpl(Context context) {
        database = new DatabaseHelper(context).getWritableDatabase();
    }


    @Override
    public List<City> getCities() {
        return DatabaseApi.getAllItems(database);
    }


    @Override
    public List<Pair<Double, Double>> getLocations() {
        return DatabaseApi.getLocations(database);
    }


    @Override
    public City getSelectedCity(String name) {
        return DatabaseApi.getSelectedItem(database, name);
    }


    @Override
    public void deleteSelectedCity(String name) {
        DatabaseApi.deleteSelectedItem(database, name);
    }


    @Override
    public void refreshCities(List<City> cities) {
        DatabaseApi.deleteAllItems(database);
        for (City curCity:cities) {
            JSONObject json = new JSONObject();
            try {
                json.put("weatherDayList", curCity.getWeatherForecast());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String weatherForecastString = json.toString();
            DatabaseApi.addItem(curCity.getName(), curCity.getLatitude(), curCity.getLongitude(),
                    curCity.getCurTemperature(), weatherForecastString, database);
        }
    }


}
