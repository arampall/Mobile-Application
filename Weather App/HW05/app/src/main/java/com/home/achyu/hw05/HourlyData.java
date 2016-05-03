package com.home.achyu.hw05;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HourlyData extends AppCompatActivity implements GetWeatherDetails.IWeather{
    TextView text;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_name);
        place = (Place) getIntent().getExtras().getSerializable(MainActivity.KEY);
        text = (TextView) findViewById(R.id.location_value);
        text.setText(place.toString());
        new GetWeatherDetails(this).execute("http://api.wunderground.com/api/"+MainActivity.key+"/hourly/q/",place.getCity(),place.getState());
    }


    @Override
    public void getHourlyList(Place place_details) {
        if(place_details!=null) {
            place = place_details;
            ListView listView = (ListView) findViewById(R.id.weather_listView);
            WeatherAdapter adapter = new WeatherAdapter(this, R.layout.layout_list, place_details.getWeather_hourly());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HourlyData.this, Details.class);
                    intent.putExtra(MainActivity.KEY, place);
                    intent.putExtra(MainActivity.Key_One, position);
                    startActivity(intent);
                }
            });
        }
        else
            Toast.makeText(HourlyData.this,"No Connection",Toast.LENGTH_LONG).show();
    }
}
