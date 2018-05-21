package com.zhirova.alina.presentation.screens.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhirova.alina.domain.WeatherDay;
import com.zhirova.alina.presentation.R;

import java.util.ArrayList;
import java.util.List;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<Pair<String, List<WeatherDay>>> info = new ArrayList<>();


    public ForecastAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    public void setData(List<Pair<String, List<WeatherDay>>> info) {
        this.info.clear();
        if (info != null){
            this.info.addAll(info);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        ForecastViewHolder holder = new ForecastViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.date.setText(info.get(position).first);
        DayAdapter dayAdapter = new DayAdapter(context);
        holder.forecast.setAdapter(dayAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.forecast.setLayoutManager(layoutManager);
        dayAdapter.setData(info.get(position).second);
    }


    @Override
    public int getItemCount() {
        return info.size();
    }


    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView forecast;

        public ForecastViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.forecast_list_text_view);
            forecast = view.findViewById(R.id.recycler_view_day);
        }
    }


}
