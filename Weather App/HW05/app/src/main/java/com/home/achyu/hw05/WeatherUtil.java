package com.home.achyu.hw05;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class WeatherUtil {
    static public class WeatherPullParser{

        static ArrayList<Weather> parseWeatherDetails(InputStream in){
            try {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(in,"UTF-8");
                ArrayList<Weather> weatherArrayList = new ArrayList<>();
                Weather weather = null;
                int event = parser.getEventType();
                while(event != XmlPullParser.END_DOCUMENT){
                    switch (event){
                        case XmlPullParser.START_TAG:
                             if(parser.getName().equals("forecast")){
                                 weather = new Weather();
                             }
                             else if(parser.getName().equals("civil")){
                                weather.setTime(parser.nextText());
                             }
                             else if(parser.getName().equals("temp")){
                                 parser.nextTag();
                                if(parser.getName().equals("english")){
                                    String temp = parser.nextText();
                                    weather.setTemperature(temp);
                                    weather.setLimits(Integer.parseInt(temp));
                                }
                             }
                             else if(parser.getName().equals("wx")){
                                 weather.setClimateType(parser.nextText());
                             }
                             else if(parser.getName().equals("icon_url")){
                                 weather.setIconUrl(parser.nextText());
                             }
                             else if(parser.getName().equals("dewpoint")){
                                parser.nextTag();
                                 if(parser.getName().equals("english")){
                                     weather.setDewpoint(parser.nextText());
                                 }
                             }
                             else if(parser.getName().equals("condition")){
                                weather.setClouds(parser.nextText());
                             }
                             else if(parser.getName().equals("wspd")){
                                 parser.nextTag();
                                 if (parser.getName().equals("english")){
                                     weather.setWindSpeed(parser.nextText());
                                 }
                             }
                             else if(parser.getName().equals("wdir")){
                                 parser.nextTag();
                                 if(parser.getName().equals("dir")){
                                     weather.setWindDirection(parser.nextText());
                                 }
                                 parser.nextTag();
                                 if(parser.getName().equals("degrees")){
                                     weather.setWindDirection(parser.nextText()+"°"+weather.getWindDirection());
                                 }
                             }
                             else if(parser.getName().equals("humidity")){
                                 weather.setHumidity(parser.nextText());
                             }
                             else if(parser.getName().equals("feelslike")){
                                 parser.nextTag();
                                 if(parser.getName().equals("english")){
                                     weather.setFeelsLike(parser.nextText());
                                 }
                             }
                             else if(parser.getName().equals("mslp")){
                                 parser.require(XmlPullParser.START_TAG, null, "mslp");
                                 while (parser.next()!=XmlPullParser.END_TAG){
                                     if(parser.getEventType()!=XmlPullParser.START_TAG){
                                         continue;
                                     }
                                 }
                                 parser.nextTag();
                                 if(parser.getName().equals("metric")){
                                     weather.setPressure(parser.nextText());
                                 }

                             }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("forecast")){
                                weatherArrayList.add(weather);
                                weather=null;
                            }
                            break;

                        default:
                            break;
                    }
                    event = parser.next();
                }
                return weatherArrayList;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static public class WeatherValidateParser{
        static Boolean parseAddress(String in, String address ){
            try {
                JSONObject root = new JSONObject(in);
                JSONArray address_value = root.getJSONArray("results");
                JSONObject address_object = address_value.getJSONObject(0);
                if(address_object.getString("formatted_address").equals(address+", USA")){
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
