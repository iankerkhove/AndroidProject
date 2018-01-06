package com.example.ian.werkstuk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.werkstuk.dao.DB;
import com.example.ian.werkstuk.model.movie;
import com.example.ian.werkstuk.model.tvshow;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String request = "http://api.themoviedb.org/3/discover/movie?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
    private DB database = null;
    private ImageView discoverImage1;
    private ListView listViewM = null;
    private ListView listViewT = null;
    private ListView discoverView = null;
    private ArrayList<HashMap<String, String>> lijst = new ArrayList<HashMap<String, String>>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        database = DB.getDb(this);
        listViewM = findViewById(R.id.movieView);
        listViewT = findViewById(R.id.tvView);
        discoverView = findViewById(R.id.discoverView);
        sharedPreferences = getSharedPreferences("key_clr", Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });
        new MainActivity.RetrieveFeedTask().execute();

        discoverView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = lijst.get(position).get("id");
                String titel = lijst.get(position).get("naam");
                String soort = lijst.get(position).get("sort");

                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("naam", titel);
                i.putExtra("id",key);
                i.putExtra("sort", soort);
                startActivity(i);
            }
        });

        //set color of action bar
        int r=sharedPreferences.getInt("a_r",0);
        int g=sharedPreferences.getInt("a_g",0);
        int b=sharedPreferences.getInt("a_b",0);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.rgb(r,g,b)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //set color of action bar
        int r=sharedPreferences.getInt("a_r",0);
        int g=sharedPreferences.getInt("a_g",0);
        int b=sharedPreferences.getInt("a_b",0);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.rgb(r,g,b)));

        //lijst voor opgeslagen films
        final List<movie> films = database.MovieDAO().getTop4();
        final List<tvshow> tvshows = database.TvDAO().getTop4();
        listViewM.setAdapter(new ArrayAdapter<movie>(this, R.layout.list_view, R.id.movieName, films));
        listViewT.setAdapter(new ArrayAdapter<tvshow>(this, R.layout.list_view, R.id.movieName, tvshows));

        if(films.isEmpty()){
            TextView t = findViewById(R.id.labelFavoriteM);
            t.setText("");
        }
        if(films.isEmpty() && tvshows.isEmpty()){
            TextView t = findViewById(R.id.labelFavoriteM);
            t.setText("If you add favorites you will find them here");
        }
        listViewM.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = films.get(position).getId();

                Intent i = new Intent(MainActivity.this, DetailLocalActivity.class);
                i.putExtra("movieId", key);
                i.putExtra("sort", "movie");
                startActivity(i);
            }
        });
        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = tvshows.get(position).getId();

                Intent i = new Intent(MainActivity.this, DetailLocalActivity.class);
                i.putExtra("tvId", key);
                i.putExtra("sort", "tv");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                Intent is = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(is);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(in);
                break;
            case R.id.action_favorite:

                Intent i = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(i);
                return true;
            default:
                break;
        }

        return true;
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
                discoverView.setAdapter(adapter);

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
