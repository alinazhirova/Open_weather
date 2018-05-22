package com.zhirova.alina.presentation.screens.add;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.presentation.screens.start.StartFragment;
import java.util.ArrayList;
import java.util.Arrays;


public class AddFragment extends Fragment {

    private String[] cities = new String[]{"London", "Socorro", "Moscow", "Berlin", "Minsk", "Novosibirsk",
            "Odessa", "New_York", "Stockholm", "Karlovy Vary", "Tokio", "Kaliningrad", "Beijing",
            "Reykjavik", "Oslo"};

    private Double[][] coordinates = {{51.5073509, -0.1277583}, {38.7222524, -9.139336599999979},
            {55.755826, 37.617299900000035}, {52.52000659999999, 13.404953999999975},
            {53.9045398, 27.561524400000053}, {55.00835259999999, 82.93573270000002},
            {46.482526, 30.723309500000028}, {40.7127753, -74.0059728},
            {59.3293235, 18.0685808}, {50.2318521, 12.871961599999963},
            {35.6894875, 139.69170639999993}, {54.7104264, 20.452214400000003},
            {54.7104264, 20.452214400000003}, {64.1265206, -21.8174392}, {59.9138688, 10.7522454}};

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
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();

            Double latitude = coordinates[position][0];
            Double longitude = coordinates[position][1];
            Pair<Double, Double> coord = new Pair<>(latitude, longitude);
            StartFragment startFragment = (StartFragment) fragmentManager.findFragmentById(R.id.start);
            startFragment.addCityUpdate(coord);
        });
    }


}
