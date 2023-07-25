package com.example.myapplicationweather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ForecastWeatherDataModel> posts;

    public RecyclerViewAdapter(Activity activity, ArrayList<ForecastWeatherDataModel> posts) {
        this.activity = activity;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View listItem = layoutInflater.inflate(R.layout.forecast_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String iconUrl = "https://openweathermap.org/img/wn/"+ posts.get(position).getIcon() +"@2x.png";
        holder.timeTv.setText(String.valueOf(posts.get(position).getTimestamp()));

        double temperatureCelsius = posts.get(position).getTemperature() - 273.15;
        String temperatureText = String.format("%.1f°", temperatureCelsius);
        holder.tempTv.setText(temperatureText);

        Picasso.get()
                .load(iconUrl)
                .into(holder.forecastIcon);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<ForecastWeatherDataModel> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTv, tempTv;
        ImageView forecastIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.timestampTextView);
            tempTv = itemView.findViewById(R.id.temperatureTextView);
            forecastIcon = itemView.findViewById(R.id.weatherIconImageView);

        }
    }
}