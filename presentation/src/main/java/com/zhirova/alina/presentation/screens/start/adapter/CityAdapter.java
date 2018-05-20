package com.zhirova.alina.presentation.screens.start.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.presentation.R;

import java.util.ArrayList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private final LayoutInflater inflater;
    private List<City> cities = new ArrayList<>();
    private ClickListener clickListener;
    private String selectedCity = null;


    public CityAdapter (Context context, String selectedCity) {
        this.inflater = LayoutInflater.from(context);
        this.selectedCity = selectedCity;
    }


    public void setData(List<City> cities){
        this.cities.clear();
        if (cities != null){
            this.cities.addAll(cities);
        }
        //notifyDataSetChanged();
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public void setSelectCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }


    public int positionById(String selectedCity){
        for(int i = 0; i < cities.size(); i++){
            if (cities.get(i).getName().equals(selectedCity)) {
                return i;
            }
        }
        return -1;
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.city_list_item, parent, false);
        CityViewHolder holder = new CityViewHolder(view);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                String cityName = (String)v.getTag();
                clickListener.onClick(cityName);
                selectedCity = cityName;
                notifyDataSetChanged();
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City curCity = cities.get(position);
        holder.itemView.setTag(curCity.getName());
        holder.cityName.setText(curCity.getName());
        holder.cityTemp.setText(curCity.getCurTemperature());

        if (curCity.getName().equals(selectedCity)) {
            holder.cityElement.setBackgroundResource(R.color.backColorPressed);
        } else {
            holder.cityElement.setBackgroundResource(R.color.backColorDefault);
        }
    }


    @Override
    public int getItemCount() {
        return cities.size();
    }


    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView cityTemp;
        FrameLayout cityElement;

        public CityViewHolder(View view) {
            super(view);
            cityName = view.findViewById(R.id.item_list_city_text_view);
            cityTemp = view.findViewById(R.id.item_list_temp_text_view);
            cityElement = view.findViewById(R.id.category_item);
        }
    }


    public interface ClickListener {
        void onClick(String cityName);
    }


}
