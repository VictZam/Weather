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
import com.weather.data.db.Hourly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class WeatherHourAdapter extends RecyclerView.Adapter<WeatherHourAdapter.MyViewHolder> {

    Context context;
    ArrayList<Hourly> hourlies;

    public WeatherHourAdapter(Context context, ArrayList<Hourly> hourlies) {
        this.context = context;
        this.hourlies = hourlies;
    }

    @NonNull
    @Override
    public WeatherHourAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_forecast,
                parent, false);

        return new WeatherHourAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHourAdapter.MyViewHolder holder, int position) {

        Picasso.with(context)
                .load(hourlies.get(position).getWeatherIconUrl().get(0).getValue())
                .into(holder.imageViewWeather);


        if(context.getSharedPreferences("preferences", MODE_PRIVATE).getString("degrees", "c") == "f"){
            holder.txtTemperature.setText(String.format("%.2f °F",hourlies.get(position).getTempF()));
        } else {
            holder.txtTemperature.setText(String.format("%.2f °C",hourlies.get(position).getTempC()));
        }

        if(context.getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
            holder.txtDescription.setText(hourlies.get(position).getWeatherDescSpanis().get(0).getValue());
        } else {
            holder.txtDescription.setText(hourlies.get(position).getWeatherDesc().get(0).getValue());
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String currentDateandTime = sdf.format(new Date());

            Date date = sdf.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, position + 1);

            holder.txtDate.setText(sdf.format(calendar.getTime()));

        } catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return hourlies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewWeatherCard)
        ImageView imageViewWeather;
        @BindView(R.id.txtTemperatureCard)
        TextView txtTemperature;
        @BindView(R.id.txtDescriptionCard) TextView txtDescription;
        @BindView(R.id.txtDate) TextView txtDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
