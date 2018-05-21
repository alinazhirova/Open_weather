package com.zhirova.alina.presentation.screens.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.domain.WeatherForecast;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.presentation.screens.detail.adapter.ForecastAdapter;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment implements DetailContract.View {

    private static final String BUNDLE_NAME = "CITY_NAME";
    private DetailContract.Presenter detailPresenter;
    private ForecastAdapter forecastAdapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailPresenter = new DetailPresenter();
        recyclerView = view.findViewById(R.id.recycler_view_forecast);
    }


    @Override
    public void onStart() {
        super.onStart();
        detailPresenter.subscribe(this, getShownCity(), getContext());
    }


    @Override
    public void onStop() {
        super.onStop();
        detailPresenter.unsubsribe(this);
    }


    @Override
    public void updateForecast(City curCity) {
        forecastAdapter = new ForecastAdapter(getContext());
        recyclerView.setAdapter(forecastAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Pair<String, List<WeatherDay>>> forecast = new ArrayList<>();
        List<WeatherDay> days = curCity.getWeatherForecast().getDays();
        for (int i = 0; i < days.size(); i++) {
            List<WeatherDay> curDayForecast = new ArrayList<>();
            for (int j = 0; j < days.size(); j++) {
                if (days.get(i).getDate().equals(days.get(j).getDate())) {
                    curDayForecast.add(days.get(i));
                }
            }
            Pair<String, List<WeatherDay>> curDay = new Pair<>(days.get(i).getDate(), curDayForecast);
            forecast.add(curDay);
        }
        forecastAdapter.setData(forecast);
    }


    public static DetailFragment create(String cityName){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, cityName);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    public String getShownCity() {
        Bundle bundle = getArguments();
        return bundle.getString(BUNDLE_NAME);
    }


    public void updateData(String cityName) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, cityName);
        setArguments(bundle);
        detailPresenter.updateScreen(cityName, getContext());
    }


}
