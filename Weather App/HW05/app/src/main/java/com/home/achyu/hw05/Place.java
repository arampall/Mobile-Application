package com.home.achyu.hw05;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable{
    String city,state;
    ArrayList<Weather> weather_hourly;

    public Place(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public Place() {
        this.city=null;
        this.state=null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if(!city.isEmpty()) {
            String[] temp = city.split(" ");
            city = temp[temp.length - 1].substring(0, 1).toUpperCase() + temp[temp.length - 1].substring(1);
            for (int i = temp.length - 2; i >= 0; i--) {
                city = temp[i].substring(0, 1).toUpperCase() + temp[i].substring(1) + " " + city;
            }
            this.city = city;
        }
        else
            this.city=null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if(!state.isEmpty()) {
            state = state.toUpperCase();
            this.state = state;
        }
        else
            this.state=null;
    }

    public String getEncodedCity(){
        String[] temp = city.split(" ");
        String city_newformat = temp[temp.length-1];
        for(int i=temp.length-2;i>=0;i--){
            city_newformat = temp[i]+"_"+city_newformat;
        }
        return city_newformat;
    }

    public String toURL(){
        return getEncodedCity().replaceAll("_","%20")+","+state;
    }

    public ArrayList<Weather> getWeather_hourly() {
        return weather_hourly;
    }

    public void setWeather_hourly(ArrayList<Weather> weather_hourly) {
        this.weather_hourly = weather_hourly;
    }


    @Override
    public String toString() {
        return city+", "+state;
    }
}
