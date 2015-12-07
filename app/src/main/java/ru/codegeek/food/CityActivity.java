package ru.codegeek.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class CityActivity extends AppCompatActivity {

    ListView selectedCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        selectedCityList = (ListView) findViewById(R.id.selected_city_list);

        String[] strings = {""};
        Gson gson = new Gson();
        City cityFromDb = gson.fromJson(getIntent().getStringExtra("city"), City.class);
        if(cityFromDb == null) {
            selectedCityList.setItemChecked(0, true);
            String[] temp = {"Выберите город"};
            strings = temp;
        }
        else {
            selectedCityList.setItemChecked(0, true);
            String[] temp = {cityFromDb.name};
            strings = temp;
        }

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_checked, strings);

        // присваиваем адаптер списку
        selectedCityList.setAdapter(adapter);


        selectedCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), CitySelectActivity.class);
                startActivity(intent);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
