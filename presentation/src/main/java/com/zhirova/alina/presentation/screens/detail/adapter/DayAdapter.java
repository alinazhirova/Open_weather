package com.zhirova.alina.presentation.screens.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.presentation.R;

import java.util.ArrayList;
import java.util.List;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private final LayoutInflater inflater;
    List<WeatherDay> day = new ArrayList<>();


    public DayAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<WeatherDay> day) {
        this.day.clear();
        if (day != null) {
            this.day.addAll(day);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.day_list_item, parent, false);
        DayViewHolder holder = new DayViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.time.setText(day.get(position).getTime());
        Picasso.get().load(day.get(position).getIconUrl()).into(holder.icon);
        holder.temp.setText(day.get(position).getTemperature());
        holder.desc.setText(day.get(position).getDescription());
        holder.wind.setText(day.get(position).getWind());
        holder.clouds.setText(day.get(position).getClouds());
        holder.pressure.setText(day.get(position).getPressure());
    }


    @Override
    public int getItemCount() {
        return day.size();
    }


    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView icon;
        TextView temp;
        TextView desc;
        TextView wind;
        TextView clouds;
        TextView pressure;

        public DayViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.day_list_time);
            icon = view.findViewById(R.id.detail_image_view);
            temp = view.findViewById(R.id.day_list_temp);
            desc = view.findViewById(R.id.day_list_desc);
            wind = view.findViewById(R.id.day_list_wind);
            clouds = view.findViewById(R.id.day_list_clouds);
            pressure = view.findViewById(R.id.day_list_pressure);
        }
    }


}