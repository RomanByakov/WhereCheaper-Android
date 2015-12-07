package ru.codegeek.food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddPrice extends Activity {

    String barcode;
    City city_id;
    EditText price;
    Button save;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);
        barcode = getIntent().getStringExtra("barcode");
        city_id = new Gson().fromJson(getIntent().getStringExtra("city"), City.class);

        price = (EditText) findViewById(R.id.price);

        AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://api.code-geek.ru:4000/shops", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);

                        Gson gson = new Gson();
                        Shop[] shops = gson.fromJson(response.toString(), Shop[].class);

                        spinner = (Spinner) findViewById(R.id.shops_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<Shop> adapter = new ArrayAdapter<Shop>(getApplicationContext(), R.layout.simple_item, shops);
// Specify the layout to use when the list of choices appears
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                        spinner.setAdapter(adapter);

                    }
                });

        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("barcode", barcode);
                params.put("city_id", city_id.id);
                params.put("price", price.getText());
                params.put("shop_id", ((Shop)spinner.getSelectedItem()).id);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://api.code-geek.ru:4000/prices", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(getApplicationContext(),"Цена добавлена",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }

}
