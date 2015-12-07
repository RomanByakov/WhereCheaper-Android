package ru.codegeek.food;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int RC_BARCODE_CAPTURE = 9001;
    Barcode barcode;
    ListView listViewShops;
    BoxAdapter boxAdapter;
    DBHelper dbHelper;
    City cityFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

        ///////
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Log.d("TAG", "--- Clear mytable: ---");
//        // удаляем все записи
//        int clearCount = db.delete("Cities", null, null);
//        Log.d("TAG", "deleted rows count = " + clearCount);
//        dbHelper.close();

        //////


        Gson gson = new Gson();
        cityFromList = gson.fromJson(getIntent().getStringExtra("city"),City.class);
        if(cityFromList != null)
        {
            // создаем объект для данных
            ContentValues cv = new ContentValues();
            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(SelectCity() != null) {
                // обновляем значение если есть

            Log.d("TAG", "--- Update mytabe: ---");
            // подготовим значения для обновления
            cv.put("id", cityFromList.id);
            cv.put("name", cityFromList.name);
            // обновляем по id
            int updCount = db.update("Cities", cv, "id = ?",
                    new String[] { SelectCity().id + "" });
            Log.d("TAG", "updated rows count = " + updCount);
//                dbHelper.close();
            } else {

                ///////////

                // вставка нового
                Log.d("TAG", "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                cv.put("id", cityFromList.id);
                cv.put("name", cityFromList.name);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Cities", null, cv);
                Log.d("TAG", "row inserted, ID = " + rowID);
//                dbHelper.close();
            }
        }
        // проверяем выбран ли город
        City cityFromDb = SelectCity();
        if(cityFromDb == null)
        {
            Intent intent = new Intent(getApplicationContext(), CityActivity.class);
            startActivity(intent);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Food&Price");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        //ab.setHomeAsUpIndicator(R.mipmap.ic_launcher); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


    }

    private City SelectCity() {
        Log.d("TAG", "--- Rows in mytable: ---");
        City cityDB = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("Cities", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("TAG",
                        "ID = " + c.getInt(idColIndex) + ", name = "
                                + c.getString(nameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false -
                // выходим из цикла
                cityDB = new City();
                cityDB.name = c.getString(nameColIndex);
                cityDB.id = c.getInt(idColIndex);


            } while (c.moveToNext());
        } else
            Log.d("TAG", "0 rows");
        c.close();
//        dbHelper.close();
        return cityDB;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "МАГАЗИНЫ");
        adapter.addFragment(new TwoFragment(), "СКАНЕР");
        adapter.addFragment(new ThreeFragment(), "ЦЕНЫ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_settings:
                Log.d("TAG", "Настройки");
                Intent intent = new Intent(getApplicationContext(), CityActivity.class);
                Gson gson = new Gson();
                intent.putExtra("city",gson.toJson(SelectCity()));
                startActivity(intent);
                return true;

            case R.id.action_find:
                Log.d("TAG", "Поиск");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            // return barcode
            barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
            // request async
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://api.code-geek.ru:4000/products/" + barcode.displayValue, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    Gson gson = new Gson();
                    Products product = gson.fromJson(response.toString(), Products.class);
                    viewPager.setCurrentItem(2);
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbut);
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), AddPrice.class);
                            intent.putExtra("city", new Gson().toJson(SelectCity()));
                            intent.putExtra("barcode", barcode.displayValue);
                            startActivity(intent);
                        }
                    });
                    LinearLayout conteiner = (LinearLayout) findViewById(R.id.conteiner);
                    conteiner.setVisibility(View.VISIBLE);
                    TextView title = (TextView) findViewById(R.id.title);
                    title.setVisibility(View.GONE);
                    TextView name = (TextView) findViewById(R.id.name);
                    name.setText(product.name);
                    TextView description = (TextView) findViewById(R.id.description);
                    description.setText(product.description);

                    listViewShops = (ListView) findViewById(R.id.shops);


                    AsyncHttpClient client = new AsyncHttpClient();
                    int id = SelectCity().id;
                    client.get("http://api.code-geek.ru:4000/prices?barcode="+barcode.rawValue +"&city_id=" + id, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            //progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            Gson gson = new Gson();
                            Price[] prices = gson.fromJson(response.toString(), Price[].class);

                            boxAdapter = new BoxAdapter(getApplicationContext(), prices);

                            // создаем адаптер
                            //ArrayAdapter<Price> adapter = new ArrayAdapter<Price>(getApplicationContext() ,R.layout.simple_item, prices);

                            // присваиваем адаптер списку
                            listViewShops.setAdapter(boxAdapter);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            //progressBar.setVisibility(View.GONE);
                        }
                    });

//                    barcodeTextView.setText(product.barcode);
//                    name.setText(product.name);
//                    description.setText(product.description);


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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TAG", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table Cities ("
                    + "id integer primary key,"
                    + "name text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}