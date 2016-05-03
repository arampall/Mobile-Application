package com.home.achyu.hw05;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    static final int RESULT_CODE = 100;
    static final String key = "56ad58108f3c4410";
    static final String Key_One = "details";
    static final String KEY = "city";
    ArrayList<Place> cities = new ArrayList<>();
    TextView text;
    ListView list;
    ArrayAdapter adapter;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_name);
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.ic_launcher);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(Gravity.RIGHT);
        actionBar.setCustomView(image,layoutParams);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCity.class);
                startActivityForResult(intent, RESULT_CODE);
            }
        });
        relativeLayout = (RelativeLayout) findViewById(R.id.rel);
        list = new ListView(MainActivity.this);

        adapter = new ArrayAdapter<Place>(this,android.R.layout.simple_list_item_1,cities);
        list.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        list.setBackgroundColor(Color.parseColor("#FFF5F2F2"));
        list.setVisibility(View.GONE);
        relativeLayout.addView(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,HourlyData.class);
                intent.putExtra(KEY,cities.get(position));
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(cities.get(position));
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK){
            text = (TextView) findViewById(R.id.default_label);
            text.setVisibility(View.GONE);
            adapter.add(data.getExtras().getSerializable(KEY));
            list.setVisibility(View.VISIBLE);

        }
    }
}

