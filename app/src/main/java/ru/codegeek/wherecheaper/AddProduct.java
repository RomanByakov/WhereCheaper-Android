package ru.codegeek.wherecheaper;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddProduct extends Activity {

    String barcode;
    EditText name;
    EditText description;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        barcode = getIntent().getStringExtra("barcode");
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("barcode", barcode);
                params.put("name", name.getText());
                params.put("description", description.getText());

                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://api.code-geek.ru:4000/products", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(getApplicationContext(),"Товар добавлен",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(getApplicationContext(),"Товар добавлен",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        Toast.makeText(getApplicationContext(),"Товар добавлен",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
