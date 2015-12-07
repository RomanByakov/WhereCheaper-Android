package ru.codegeek.food;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class OneFragment extends Fragment{

    ListView listView;
    Context mContext;
    ProgressBar progressBar;

    public OneFragment() {
        // Required empty public constructor

    }

//    public OneFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_one,
                container, false);

        mContext = this.getActivity().getApplicationContext();

        listView = (ListView) view.findViewById(R.id.shops);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.code-geek.ru:4000/shops", new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Shop[] shops = gson.fromJson(response.toString(), Shop[].class);

                // создаем адаптер
                ArrayAdapter<Shop> adapter = new ArrayAdapter<Shop>(mContext,R.layout.simple_item, shops);

                // присваиваем адаптер списку
                listView.setAdapter(adapter);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }
        });
        return view;
    }

}
