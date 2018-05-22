package com.zhirova.alina.presentation.screens.add;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zhirova.alina.domain.City;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.local.local_repository.LocalApi;
import com.zhirova.alina.local.local_repository.LocalApiImpl;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.presentation.screens.start.StartPresenter;
import com.zhirova.alina.remote.exception.InternetException;
import com.zhirova.alina.remote.exception.NoForecastException;
import com.zhirova.alina.remote.remote_repository.RemoteApi;
import com.zhirova.alina.remote.remote_repository.RemoteApiImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddFragment extends Fragment {

    private String[] cities = new String[]{"London", "Paris", "Moscow", "Berlin", "Minsk", "St.Petesburg",
            "Kiev", "Rome", "Stockholm", "Prague", "Tokio", "Copenhagen", "Madrid", "Lisbon", "Oslo"};

    private Double[][] coordinates = {{51.50, 0.12}, {48.86, 2.37}, {55.75, 37.50}, {52.44, 13.33},
            {54.00, 27.60}, {59.80, 30.30}, {50.40, 30.50}, {41.88, 12.48}, {59.33, 18.05},
            {50.08, 14.40}, {32.70, 139.70}, {55.66, 12.57}, {40.42, 03.70}, {38.71, -09.15}, {59.90, 10.75} };

    private ArrayList<String> listCities;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private EditText editText;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listview_add);
        editText = view.findViewById(R.id.search_edittext);
        initList();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    initList();
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    public void searchItem(String textToSearch) {
        for(String curCity: cities){
            if (!curCity.toLowerCase().contains(textToSearch.toLowerCase())){
                listCities.remove(curCity);
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void initList() {
        listCities = new ArrayList<>(Arrays.asList(cities));
        adapter = new ArrayAdapter<>(getActivity(), R.layout.add_list_item,
                R.id.add_city_textview, listCities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String cityName = cities[position];
            Double latitude = coordinates[position][0];
            Double longitude = coordinates[position][1];

            List<Pair<Double, Double>> locations = new ArrayList<>();
            Pair<Double, Double> coord = new Pair<>(latitude, longitude);
            locations.add(coord);



            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            StartPresenter startPresenter = new StartPresenter();
            startPresenter.refreshCities();
        });
    }


}
