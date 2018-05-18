package com.zhirova.alina.local.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherForecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DatabaseApi {

    private static final String TAG = DatabaseApi.class.getSimpleName();


    public static void createTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + CityContract.CityEntry.TABLE_NAME + " (" +
                CityContract.CityEntry.COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL," +
                CityContract.CityEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                CityContract.CityEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                CityContract.CityEntry.COLUMN_TEMPERATURE + " TEXT NOT NULL, " +
                CityContract.CityEntry.COLUMN_WEATHER_FORECAST + " TEXT NOT NULL);");
    }


    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + CityContract.CityEntry.TABLE_NAME);
    }


    public static void addItem(String name, double latitude, double longitude, String temperature,
                               String weatherForecast, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(CityContract.CityEntry.COLUMN_NAME, name);
        values.put(CityContract.CityEntry.COLUMN_LATITUDE, latitude);
        values.put(CityContract.CityEntry.COLUMN_LONGITUDE, longitude);
        values.put(CityContract.CityEntry.COLUMN_TEMPERATURE, temperature);
        values.put(CityContract.CityEntry.COLUMN_WEATHER_FORECAST, weatherForecast);
        database.insert(CityContract.CityEntry.TABLE_NAME, null, values);
    }


    public static void deleteAllItems(SQLiteDatabase database) {
        database.delete(CityContract.CityEntry.TABLE_NAME, null, null);
    }


    public static void deleteSelectedItem(SQLiteDatabase database, String name) {
        String whereClause = CityContract.CityEntry.COLUMN_NAME + " = ?";
        String[] whereArgs = {name};
        database.delete(CityContract.CityEntry.TABLE_NAME, whereClause, whereArgs);
    }


    public static List<City> getAllItems(SQLiteDatabase database) {
        Cursor cursor = database.query(CityContract.CityEntry.TABLE_NAME,
                CityContract.CityEntry.citiesAllColumns,
                null, null, null, null,
                CityContract.CityEntry.COLUMN_NAME + " ASC");
        return getResultFromCursor(cursor);
    }


    public static City getSelectedItem(SQLiteDatabase database, String name) {
        String selection = CityContract.CityEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = database.query(CityContract.CityEntry.TABLE_NAME,
                CityContract.CityEntry.citiesAllColumns,
                selection, selectionArgs, null, null, null);
        List<City> cities = getResultFromCursor(cursor);
        return cities.get(0);
    }


    public static List<Pair<Double, Double>> getLocations(SQLiteDatabase database) {
        List<Pair<Double, Double>> locations = new ArrayList<>();
        List<City> cities = getAllItems(database);
        for (int i = 0; i < cities.size(); i++) {
            City curCity = cities.get(i);
            Pair<Double, Double> coordinates = new Pair<>(curCity.getLatitude(),
                    curCity.getLongitude());
            locations.add(coordinates);
        }
        return locations;
    }


    private static List<City> getResultFromCursor(Cursor cursor) {
        List<City> result = new ArrayList<>();
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(CityContract.CityEntry.COLUMN_NAME);
            int latitudeIndex = cursor.getColumnIndex(CityContract.CityEntry.COLUMN_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(CityContract.CityEntry.COLUMN_LONGITUDE);
            int temperatureIndex = cursor.getColumnIndex(CityContract.CityEntry.COLUMN_TEMPERATURE);
            int weatherForecastIndex = cursor.getColumnIndex(CityContract.CityEntry.COLUMN_WEATHER_FORECAST);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                double latitude = cursor.getDouble(latitudeIndex);
                double longitude = cursor.getDouble(longitudeIndex);
                String temperature = cursor.getString(temperatureIndex);
                String weatherForecastString = cursor.getString(weatherForecastIndex);

                City curCity = new City(name, latitude, longitude, temperature);
                JSONObject json = null;
                WeatherForecast weatherForecastObject = null;

                try {
                    json = new JSONObject(weatherForecastString);
                    weatherForecastObject = (WeatherForecast) json.get("weatherDayList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                curCity.setWeatherForecast(weatherForecastObject);
                result.add(curCity);
            }
            try{
                cursor.close();
            } catch (Exception e){
                Log.e(TAG, "DATABASE CURSOR CLOSE ERROR", e);
            }
        }
        return result;
    }


}
