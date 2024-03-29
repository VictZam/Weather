package com.weather.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.R;
import com.weather.data.db.Weather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    ArrayList<Weather> weather;

    public WeatherForecastAdapter(Context context, ArrayList<Weather> weather) {
        this.context = context;
        this.weather = weather;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_forecast,
                parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.with(context)
                .load(weather.get(position).getHourly().get(0).getWeatherIconUrl().get(0).getValue())
                .into(holder.imageViewWeather);


        if(context.getSharedPreferences("preferences", MODE_PRIVATE).getString("degrees", "c") == "f"){
            holder.txtTemperature.setText(String.format("%.2f °F",weather.get(position).getAvgtempF()));
        } else {
            holder.txtTemperature.setText(String.format("%.2f °C",weather.get(position).getAvgtempC()));
        }

        if(context.getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
            holder.txtDescription.setText(weather.get(position).getHourly().get(0).getWeatherDescSpanis().get(0).getValue());
        } else  {
            holder.txtDescription.setText(weather.get(position).getHourly().get(0).getWeatherDesc().get(0).getValue());
        }

        holder.txtDate.setText(weather.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return weather.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewWeatherCard) ImageView imageViewWeather;
        @BindView(R.id.txtTemperatureCard) TextView txtTemperature;
        @BindView(R.id.txtDescriptionCard) TextView txtDescription;
        @BindView(R.id.txtDate) TextView txtDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
