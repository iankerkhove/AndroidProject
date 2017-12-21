package com.example.ian.werkstuk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String REQUEST="https://api.themoviedb.org/3/discover/movie?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(REQUEST);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
    }
    protected void onPostExecute(String response) {

        if (response == null) {
            response = "THERE WAS AN ERROR";
        } else {
            JSONObject object = null;
            JSONArray result = new JSONArray();
            try {
                object = new JSONObject(response);
                result = object.getJSONArray("results");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0; i<result.length();i++){
                try {
                    JSONObject temp = (JSONObject) result.get(i);
                    imageView = findViewById(R.id.imageView1);
                    Picasso.with(this).load("https://image.tmdb.org/t/p/w300/eKi8dIrr8voobbaGzDpe8w0PVbC.jpg").into(imageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
