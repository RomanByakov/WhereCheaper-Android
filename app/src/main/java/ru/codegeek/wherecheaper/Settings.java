package ru.codegeek.wherecheaper;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Settings extends Activity {

    ListView lvMain;
    Button btnSave;
    Button btnEdit;
    TextView lbCity;
    ProgressBar progresBar;
    int selectedIndex = -1;

    City[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnEdit = (Button) findViewById(R.id.edit);
        btnSave = (Button) findViewById(R.id.save);
        lbCity = (TextView) findViewById(R.id.city);
        progresBar = (ProgressBar) findViewById(R.id.progress);
        // находим список
        lvMain = (ListView) findViewById(R.id.list_item);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvMain.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                lbCity.setVisibility(View.GONE);
                lbCity.setText(cities[selectedIndex].name);
            }
        });

        if (selectedIndex != -1) {
            lvMain.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            lbCity.setVisibility(View.GONE);
            lbCity.setText("");
        } else {

            progresBar.setVisibility(View.VISIBLE);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://api.code-geek.ru:4000/cities", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    progresBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    cities = gson.fromJson(response.toString(), City[].class);

                    // создаем адаптер
                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(),
                            android.R.layout.simple_list_item_single_choice, cities);

                    // присваиваем адаптер списку
                    lvMain.setAdapter(adapter);

                    lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                    lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                              long id) {
                                                          Log.d("", "");
                                                          lvMain.setItemChecked(position, true);
                                                          lvMain.setVisibility(View.GONE);
                                                          btnSave.setVisibility(View.VISIBLE);
                                                          btnEdit.setVisibility(View.VISIBLE);
                                                          lbCity.setVisibility(View.VISIBLE);
                                                          lbCity.setText(cities[position].name);
                                                          selectedIndex = position;
                                                      }
                                                  }

                    );
                }
            });

        }
    }

}
