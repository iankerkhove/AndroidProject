package com.example.ian.werkstuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
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
import android.widget.ListView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String request="http://api.themoviedb.org/3/discover/movie?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
    DB database = null;
    ImageView discoverImage1;
    ListView listViewM=null;
    ListView listViewT= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        database = DB.getDb(this);
        listViewM = findViewById(R.id.movieView);
        listViewT = findViewById(R.id.tvView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });
        new MainActivity.RetrieveFeedTask().execute();

        /*//lijst voor opgeslagen films
        List<movie> films = database.MovieDAO().getAll();
        listView.setAdapter(new ArrayAdapter<movie>(this,R.layout.list_view,R.id.movieName,films));*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //lijst voor opgeslagen films
        final List<movie> films = database.MovieDAO().getTop4();
        final List<tvshow> tvshows = database.TvDAO().getTop4();
        listViewM.setAdapter(new ArrayAdapter<movie>(this,R.layout.list_view,R.id.movieName,films));
        listViewT.setAdapter(new ArrayAdapter<tvshow>(this,R.layout.list_view,R.id.movieName,tvshows));


        listViewM.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = films.get(position).getId();

                Intent i = new Intent(MainActivity.this, DetailLocalActivity.class);
                i.putExtra("movieId", key);
                i.putExtra("sort","movie");
                startActivity(i);
            }
        });
        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = tvshows.get(position).getId();

                Intent i = new Intent(MainActivity.this, DetailLocalActivity.class);
                i.putExtra("tvId", key);
                i.putExtra("sort","tv");
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
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                /*Intent in = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(in);*/
                break;
            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(MainActivity.this,FavoriteActivity.class);
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
