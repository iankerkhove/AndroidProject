package com.example.ian.werkstuk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    String request;
    private String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private String META = "?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US";
    TextView titel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        titel = (TextView) findViewById(R.id.title);

        Intent i = getIntent();
        titel.setText(i.getStringExtra("naam"));
        if(i.getStringExtra("id")!=null){
            request = BASE_URL + i.getStringExtra("id") + META;
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(request);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //Get of Post
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

        protected void onPostExecute(String response) {
            String titel = "";

            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            else {
                JSONObject object = null;
                JSONArray result = new JSONArray();
                /*try {
                    object = new JSONObject(response);
                    result = object.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<result.length();i++){
                    try {
                        HashMap<String,String> tempList = new HashMap<String, String>();
                        JSONObject temp = (JSONObject) result.get(i);

                        if(rdbMovie.isChecked()){
                            tempList.put("id", temp.getString("id"));
                            tempList.put("naam", temp.getString("original_title"));
                        }else{
                            tempList.put("id", temp.getString("id"));
                            tempList.put("naam", temp.getString("name"));
                        }
                        lijst.add(tempList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }

            }
        }
}
