package com.home.achyu.hw05;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddCity extends AppCompatActivity {
    EditText input;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_name);
        findViewById(R.id.savecity_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place place = new Place();
                input = (EditText) findViewById(R.id.city_value);
                Log.d("demo",input.getText().toString());
                place.setCity(input.getText().toString());
                input = (EditText) findViewById(R.id.state_value);
                place.setState(input.getText().toString());
                if(place.getCity()!=null && place.getState()!=null) {
                    intent = new Intent();
                    intent.putExtra(MainActivity.KEY, place);
                    new ValidateAddress().execute("http://maps.googleapis.com/maps/api/geocode/json?address=" + place.toURL() + "&sensor=true&components=country:US", place.toString());
                }
                else
                    Toast.makeText(AddCity.this,"All Fields are Required",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ValidateAddress extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();
            try {

                URL url = new URL(params[0]);
                Log.d("demo",url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode==HttpURLConnection.HTTP_OK){
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    builder.setLength(0);
                    String line = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }
                    return WeatherUtil.WeatherValidateParser.parseAddress(builder.toString(),params[1]);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                setResult(RESULT_OK, intent);
                finish();
            }
            else
                Toast.makeText(AddCity.this,"Enter a Valid Location",Toast.LENGTH_LONG).show();
        }
    }
}
