package ru.codegeek.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CitySelectActivity extends AppCompatActivity {

    ListView cities;
    City[] citiesArray;
    EditText tbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cities = (ListView) findViewById(R.id.cities);
        cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // start main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Gson gson = new Gson();
                intent.putExtra("city", gson.toJson(citiesArray[position]));
                startActivity(intent);
            }
        });

        tbSearch = (EditText) findViewById(R.id.text_search);
        tbSearch.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                Log.d("TAG", "TEXT: " + s);
                                                if (s.toString().length() > 1) {
                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    client.get("http://api.code-geek.ru:4000/cities?searchByName=" + s, new JsonHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                                            super.onSuccess(statusCode, headers, response);
                                                            Gson gson = new Gson();
                                                            citiesArray = gson.fromJson(response.toString(), City[].class);
                                                            // создаем адаптер
                                                            ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.simple_list_item_checked, citiesArray);

                                                            // присваиваем адаптер списку
                                                            cities.setAdapter(adapter);
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        }

        );


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
