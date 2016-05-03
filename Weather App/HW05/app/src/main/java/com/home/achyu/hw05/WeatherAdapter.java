package com.home.achyu.hw05;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter{
    List<Weather> mData;
    Context mcontext;
    int mResource;

    public WeatherAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mData = objects;
        this.mcontext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        Weather weather = mData.get(position);
        TextView time_text = (TextView) convertView.findViewById(R.id.time_value_list);
        time_text.setText(weather.getTime());
        TextView condition_text = (TextView) convertView.findViewById(R.id.condition_value_list);
        condition_text.setText(weather.getClimateType());
        TextView temperature_text = (TextView) convertView.findViewById(R.id.temperature_value_list);
        temperature_text.setText(weather.getTemperature()+"°F");
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView_list);
        Picasso.with(mcontext).load(weather.getIconUrl()).into(image);
        return convertView;
    }
}
