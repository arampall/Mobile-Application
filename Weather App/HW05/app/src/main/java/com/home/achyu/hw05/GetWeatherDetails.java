package com.home.achyu.hw05;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetWeatherDetails extends AsyncTask<String, Void, Place> {
    ProgressDialog progressDialog;
    IWeather Activity;

    public GetWeatherDetails(IWeather activity) {
        Activity = activity;
    }

    @Override
    protected Place doInBackground(String... params) {
        Place place = new Place(params[1],params[2]);

        String url_builder = params[0]+params[2]+"/"+place.getEncodedCity()+".xml";
        try {
            URL url = new URL(url_builder);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                Log.d("demo",url_builder);
                place.setWeather_hourly(WeatherUtil.WeatherPullParser.parseWeatherDetails(in));
                return place;
            }
            else {
                Log.d("demo", "Not found  " + statusCode);
                Log.d("demo", url_builder);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog((Context) Activity);
        progressDialog.setMessage("Loading Hourly Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Place place_details) {
        super.onPostExecute(place_details);
        progressDialog.dismiss();
        Activity.getHourlyList(place_details);
    }

    interface IWeather{
        void getHourlyList(Place place_weather);
    }
}
