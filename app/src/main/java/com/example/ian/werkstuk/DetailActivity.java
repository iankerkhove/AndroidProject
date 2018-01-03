package com.example.ian.werkstuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.werkstuk.dao.DB;
import com.example.ian.werkstuk.model.movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    String request;
    private String BASEM_URL = "http://api.themoviedb.org/3/movie/";
    private String METAM = "?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US";
    private String BASET_URL = "https://api.themoviedb.org/3/tv/";
    private String METAT = "?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US";
    TextView titel;
    TextView tagline;
    TextView release;
    TextView orilanguage;
    TextView spolanguage;
    TextView spolanguageLabel;
    TextView genre;
    TextView statu;
    TextView productionCountryLabel;
    TextView productionCountry;
    TextView votesAverage;
    TextView overview;
    ImageView image;
    private Bitmap bmp;
    String sort="";
    DB database = null;
    String title="";
    String id="";

    public boolean isTablet() {
        try {
            // Compute screen size
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 7;
        } catch(Throwable t) {
            Log.wtf("", "Failed to compute screen size", t);
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database  = DB.getDb(this);
        if(isTablet()){
            setContentView(R.layout.fragment_test);
        }
        else{
            setContentView(R.layout.activity_detail);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie m = new movie();
                m.setTagline(tagline.getText().toString());
                m.setStatus(statu.getText().toString());
                m.setTitle(title);
                m.setSpoLanguage(spolanguage.getText().toString());
                m.setReleasedate(release.getText().toString());
                m.setStatus(statu.getText().toString());
                m.setProdCountry(productionCountry.getText().toString());
                m.setOverview(overview.getText().toString());
                m.setOriLanguage(orilanguage.getText().toString());
                m.setGenre(genre.getText().toString());
                m.setId(Integer.parseInt(id));
                m.setVoteAverage(votesAverage.getText().toString());
                database.MovieDAO().insert(m);
                Snackbar.make(view, "Movie succesvol toegevoegd", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        titel = (TextView) findViewById(R.id.title);

        Intent i = getIntent();
        titel.setText(i.getStringExtra("naam"));

        //check if movie or tv show
        if(i.getStringExtra("id")!=null ){
            if(i.getStringExtra("sort").equals("movie")) {
                sort="movie";
                request = BASEM_URL + i.getStringExtra("id") + METAM;
                new DetailActivity.RetrieveFeedTask().execute();
            }
            else if(i.getStringExtra("sort").equals("tv")) {
                sort="tv";
                request = BASET_URL + i.getStringExtra("id") + METAT;
                new DetailActivity.RetrieveFeedTask().execute();
            }
        }
        else{
            Toast toast = Toast.makeText(this,"@string/toast_invalid",Toast.LENGTH_LONG);
            toast.show();
        }
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
                break;
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
            String titel = "";

            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            else {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    tagline = (TextView) findViewById(R.id.tagline);
                    release = (TextView) findViewById(R.id.releaseDate);
                    orilanguage = (TextView) findViewById(R.id.originalLanguage);
                    spolanguageLabel = (TextView) findViewById(R.id.spokenLanguageLabel);
                    spolanguage = (TextView) findViewById(R.id.spokenLanguage);
                    genre = (TextView) findViewById(R.id.genres);
                    statu = (TextView) findViewById(R.id.status);
                    productionCountryLabel = (TextView) findViewById(R.id.prodCountryLabel);
                    productionCountry = (TextView) findViewById(R.id.prodCountry);
                    votesAverage = (TextView) findViewById(R.id.voteAverage);
                    overview = (TextView) findViewById(R.id.overview);
                    image = (ImageView) findViewById(R.id.headImage);
                    final String imagePath = object.get("poster_path").toString();

                    if (sort.equals("tv")){
                        Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w300/" + imagePath).into(image);
                        release.setText(object.get("first_air_date").toString());
                        orilanguage.setText(object.get("original_language").toString());

                        String genres = "";
                        for (int i = 0; i < object.getJSONArray("genres").length(); i++) {
                            JSONArray a = new JSONArray();
                            a = object.getJSONArray("genres");
                            String value = a.getJSONObject(i).get("name").toString();
                            if (a.length() > 1) {
                                genres += value + "\n";
                            } else {
                                genres += value;
                            }
                        }
                        genre.setText(genres);

                        statu.setText(object.get("status").toString());
                        String production="";
                        for (int i = 0; i < object.getJSONArray("production_companies").length(); i++) {
                            JSONArray a = new JSONArray();
                            a = object.getJSONArray("production_companies");
                            String value = a.getJSONObject(i).get("name").toString();
                            if (a.length() > 1) {
                                production += value + ", ";
                            } else {
                                production += value;
                            }
                        }
                        spolanguageLabel.setText("Number of Seasons: ");
                        spolanguage.setText(object.get("number_of_seasons").toString());
                        productionCountryLabel.setText("Production Companies: ");
                        productionCountry.setText(production);
                        votesAverage.setText(object.get("vote_average").toString());
                        overview.setText(object.get("overview").toString());
                    }

                    if(sort.equals("movie")) {
                        Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w300/" + imagePath).into(image);
                        release.setText(object.get("release_date").toString());
                        tagline.setText(object.get("tagline").toString());
                        orilanguage.setText(object.get("original_language").toString());
                        String language = "";
                        for (int i = 0; i < object.getJSONArray("spoken_languages").length(); i++) {
                            JSONArray a = new JSONArray();
                            a = object.getJSONArray("spoken_languages");
                            String value = a.getJSONObject(i).get("iso_639_1").toString();
                            if (a.length() > 1) {
                                language += value + ", ";
                            } else {
                                language += value;
                            }
                        }
                        String genres = "";
                        spolanguage.setText(language);
                        for (int i = 0; i < object.getJSONArray("genres").length(); i++) {
                            JSONArray a = new JSONArray();
                            a = object.getJSONArray("genres");
                            String value = a.getJSONObject(i).get("name").toString();
                            if (a.length() > 1) {
                                genres += value + "\n";
                            } else {
                                genres += value;
                            }
                        }
                        genre.setText(genres);
                        statu.setText(object.get("status").toString());
                        String production = "";
                        for (int i = 0; i < object.getJSONArray("production_countries").length(); i++) {
                            JSONArray a = new JSONArray();
                            a = object.getJSONArray("production_countries");
                            String value = a.getJSONObject(i).get("iso_3166_1").toString();
                            if (a.length() > 1) {
                                production += value + ", ";
                            } else {
                                production += value;
                            }
                        }
                        productionCountry.setText(production);
                        votesAverage.setText(object.get("vote_average").toString());
                        overview.setText(object.get("overview").toString());
                        title = object.get("title").toString();
                        id = object.get("id").toString();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }

            }
        }
}
//gridview: //https://developer.android.com/guide/topics/ui/layout/gridview.html