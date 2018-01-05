package com.example.wear;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends WearableActivity {

    String request = "http://api.themoviedb.org/3/discover/movie?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
    ListView responseView = null;
    private ArrayList<HashMap<String, String>> lijst = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = findViewById(R.id.responseView);

        new MainActivity.RetrieveFeedTask().execute();
        responseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "See it in theater", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(request);
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

        protected void onPostExecute(String response) {
            Bitmap bmp;
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
                //maak lijst leeg van vorige zoekactie
                lijst.clear();
                //vul lijst op
                for (int i = 0; i < 4; i++) {
                    try {
                        HashMap<String, String> tempList = new HashMap<String, String>();
                        JSONObject temp = (JSONObject) result.get(i);

                        tempList.put("id", temp.getString("id"));
                        tempList.put("naam", temp.getString("title"));
                        tempList.put("sort", "movie");

                        lijst.add(tempList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this,lijst,R.layout.list_view,new String[]{"naam"}, new int[]{R.id.movieName});
                responseView.setAdapter(adapter);

               /* discoverImage1 = findViewById(R.id.discover1);
                for (int i = 1; i <= 4;i++) {
                    try {
                        JSONObject temp= (JSONObject) result.get(i);
                        String poster = temp.get("poster_path").toString();
                        Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w300/"+ poster).into(discoverImage1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        }
    }

}
