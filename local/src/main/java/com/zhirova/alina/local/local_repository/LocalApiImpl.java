package com.zhirova.alina.local.local_repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.local.database.DatabaseApi;
import com.zhirova.alina.local.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        Log.d("BASKA", "refreshCities");
        DatabaseApi.deleteAllItems(database);
        for (City curCity:cities) {
            Gson gson = new Gson();
            String weatherForecastString = gson.toJson(curCity.getWeatherForecast());

            Log.d("BASKA", "weatherForecastString = " + weatherForecastString);

            //Log.d("BASKA", "weatherForecastString = " + weatherForecastString);
            DatabaseApi.addItem(curCity.getName(), curCity.getLatitude(), curCity.getLongitude(),
                    curCity.getCurTemperature(), weatherForecastString, database);
        }
    }


}
