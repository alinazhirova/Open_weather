package com.zhirova.alina.local.database;

import android.provider.BaseColumns;

import java.sql.Blob;


public final class CityContract {

    private CityContract() {}


    public static abstract class CityEntry implements BaseColumns {
        public static final String TABLE_NAME = "Cities";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_WEATHER_FORECAST = "weather_forecast";

        public static final String[] citiesAllColumns = {
                COLUMN_NAME,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_TEMPERATURE,
                COLUMN_WEATHER_FORECAST
        };
    }


}