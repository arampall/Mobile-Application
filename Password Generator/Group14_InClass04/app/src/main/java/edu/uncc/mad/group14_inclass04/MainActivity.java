// Assignment In Class 04
// MainActivity.java
// Brad LaMotte
// ACHYUTA RAM
// Jesus Garcia

package edu.uncc.mad.group14_inclass04;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String password;
    Handler handler;
    final static String MSG_KEY = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.length_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.length_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        findViewById(R.id.button_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormSettings form = getFormSettings();
                if (form.valid()) {
                    new CreatePasswordAsync().execute(form);
                } else {
                    Toast.makeText(MainActivity.this, form.getErrors(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormSettings settings = getFormSettings();
                if (settings.valid()) {
                    Thread thread = new Thread(new CreatePasswordThread(settings));
                    thread.start();
                } else {
                    Toast.makeText(MainActivity.this, settings.getErrors(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case CreatePasswordThread.START:
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMax(100);
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Generating Password");
                        progressDialog.show();
                        break;
                    case CreatePasswordThread.FINISH:
                        TextView display = (TextView) findViewById(R.id.result);
                        display.setText(password);
                        progressDialog.dismiss();
                        break;
                }

                return false;
            }
        });
    }

    class CreatePasswordThread implements Runnable{
        FormSettings settings;
        static final int START = 0x00;
        static final int FINISH = 0x01;

        public CreatePasswordThread(FormSettings settings) {
            this.settings = settings;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = START;
            handler.sendMessage(msg);

            Log.d("proj", "settings: " + settings.toString());

            password = Util.getPassword(settings.getPwLength(), settings.getNumbers(), settings.getUpperCase(), settings.getLowerCase(), settings.getSpecial());
            msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString(MSG_KEY, "Password created");
            msg.what = FINISH;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    class CreatePasswordAsync extends AsyncTask<FormSettings, Integer, String>{
        @Override
        protected String doInBackground(FormSettings... params) {
            FormSettings form = params[0];
            Log.d("proj", "form: " + form.toString());
            Util util = new Util();
            password = util.getPassword(form.getPwLength(), form.getNumbers(), form.getUpperCase(), form.getLowerCase(), form.getSpecial());
            return password;
        }

        @Override
        protected void onPreExecute() {
            Log.d("proj", "starting async");
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Generating Password");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("proj", "password: " + password);
            TextView display = (TextView) findViewById(R.id.result);
            display.setText(password);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }

    private FormSettings getFormSettings(){
        Spinner length = (Spinner) findViewById(R.id.length_spinner);
        CheckBox numbers = (CheckBox) findViewById(R.id.numbers_label);
        CheckBox upper = (CheckBox) findViewById(R.id.uppercase_label);
        CheckBox lower = (CheckBox) findViewById(R.id.lowercase_label);
        CheckBox special = (CheckBox) findViewById(R.id.specialcase_label);
        return new FormSettings(length.getSelectedItemPosition(), numbers.isChecked(), upper.isChecked(), lower.isChecked(), special.isChecked());
    }
}
