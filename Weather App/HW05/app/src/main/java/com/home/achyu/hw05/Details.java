package com.home.achyu.hw05;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Details extends AppCompatActivity {
    int index;
    ArrayList<Weather> weatherList;
    TextView text;
    ImageView image;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_name);
        index = getIntent().getExtras().getInt(MainActivity.Key_One);
        place = (Place) getIntent().getExtras().getSerializable(MainActivity.KEY);
        weatherList = place.getWeather_hourly();
        Log.d("demo", String.valueOf(weatherList.size()));
        setLayout();
        ImageView image_forward = (ImageView) findViewById(R.id.next);
        image_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index == weatherList.size())
                    index = 0;
                setLayout();
            }
        });

        ImageView image_back = (ImageView) findViewById(R.id.previous);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    index--;
                    if (index < 0)
                        index = weatherList.size() - 1;
                    setLayout();
            }
        });


    }

    public void setLayout(){
        Weather weather = weatherList.get(index);
        image = (ImageView) findViewById(R.id.image_details);
        Picasso.with(this).load(weather.getIconUrl()).into(image);
        text = (TextView) findViewById(R.id.location_details_value);
        text.setText(place.toString()+" ("+weather.getTime()+")");
        text = (TextView) findViewById(R.id.temperature_value_details);
        text.setText(weather.getTemperature()+"°F");
        text = (TextView) findViewById(R.id.condition_value_details);
        text.setText(weather.getClimateType());
        text = (TextView) findViewById(R.id.feels_value);
        text.setText(weather.getFeelsLike()+" Fahrenheit");
        text = (TextView) findViewById(R.id.humidity_value);
        text.setText(weather.getHumidity()+"%");
        text = (TextView) findViewById(R.id.dew_value);
        text.setText(weather.getDewpoint()+" Fahrenheit");
        text = (TextView) findViewById(R.id.pressure_value);
        text.setText(weather.getPressure()+" hPa");
        text = (TextView) findViewById(R.id.clouds_value);
        text.setText(weather.getClouds());
        text = (TextView) findViewById(R.id.winds_value);
        text.setText(weather.getWindSpeed()+" mph, "+weather.getWindDirection());
        text = (TextView) findViewById(R.id.maxtemp_value);
        text.setText(weather.getMaximumTemp()+" Fahrenheit");
        text = (TextView) findViewById(R.id.mintemp_value);
        text.setText(weather.getMinimumTemp()+" Fahrenheit");
    }
}
