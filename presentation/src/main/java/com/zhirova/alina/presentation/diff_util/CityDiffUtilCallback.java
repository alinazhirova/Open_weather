package com.zhirova.alina.presentation.diff_util;


import android.support.v7.util.DiffUtil;
import com.zhirova.alina.domain.City;
import java.util.List;


public class CityDiffUtilCallback extends DiffUtil.Callback {

    private final List<City> oldList;
    private final List<City> newList;


    public CityDiffUtilCallback(List<City> oldList, List<City> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }


    @Override
    public int getOldListSize() {
        return oldList.size();
    }


    @Override
    public int getNewListSize() {
        return newList.size();
    }


    @Override
    public boolean areItemsTheSame(int oldCityPosition, int newCityPosition) {
        City oldCity = oldList.get(oldCityPosition);
        City newCity = newList.get(newCityPosition);

        return oldCity.getName().equals(newCity.getName());
    }


    @Override
    public boolean areContentsTheSame(int oldCityPosition, int newCityPosition) {
        City oldCity = oldList.get(oldCityPosition);
        City newCity = newList.get(newCityPosition);

        return (oldCity.getLatitude() == newCity.getLatitude() &&
                oldCity.getLongitude() == newCity.getLongitude());
    }


}
