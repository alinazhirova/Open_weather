package com.zhirova.alina.data.geolocation;


import android.util.Pair;

import java.util.List;


public interface GeoCoordinates {
    List<Pair<Double, Double>> findCurLocation();
}
