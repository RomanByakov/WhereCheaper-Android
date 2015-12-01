package ru.codegeek.wherecheaper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Barcode barcode;
    private static final String HOST  = "http://api.code-geek.ru:4000";
    private static final int RC_BARCODE_CAPTURE = 9001;
    TextView barcodeTextView;
    TextView description;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barcodeTextView = (TextView)findViewById(R.id.barcode);
        name = (TextView)findViewById(R.id.name);
        description = (TextView)findViewById(R.id.description);

        barcodeTextView.setText("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // camera start
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                // end camera start
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // start settings
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);
            // end settings
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            // return barcode
            barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
            // request async
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(HOST + "/products/" + barcode.displayValue, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                        Gson gson = new Gson();
                        Products product = gson.fromJson(response.toString(), Products.class);
                        barcodeTextView.setText(product.barcode);
                        name.setText(product.name);
                        description.setText(product.description);


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    //Toast.makeText(getApplicationContext(), "Товар не найден", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Товар не найден")
                            .setMessage("Хотите добавить товар?")
                            .setCancelable(false)
                            .setPositiveButton("Добавить",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            // start add product
                                            Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                                            intent.putExtra("barcode", barcode.displayValue);
                                            startActivity(intent);
                                            // end add product
                                        }
                                    })
                    .setNegativeButton("Попробовать снова",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // camera start
                                    Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                                    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                                    // end camera start
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
            }
        }
}
