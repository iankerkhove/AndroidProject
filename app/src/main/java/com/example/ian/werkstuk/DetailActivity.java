package com.example.ian.werkstuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    String request;
    private String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private String META = "?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US";
    TextView titel;
    TextView tagline;
    TextView release;
    TextView orilanguage;
    TextView spolanguage;
    TextView genre;
    TextView statu;
    TextView productionCountry;
    TextView votesAverage;
    TextView overview;
    ImageView image;
    private Bitmap bmp;
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
            new DetailActivity.RetrieveFeedTask().execute();
        }
        else{
            Toast toast = Toast.makeText(this,"Invalid DATA",Toast.LENGTH_LONG);
            toast.show();
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
                try {
                    object = new JSONObject(response);
                    tagline = (TextView) findViewById(R.id.tagline);
                    release = (TextView) findViewById(R.id.releaseDate);
                    orilanguage = (TextView) findViewById(R.id.originalLanguage);
                    spolanguage = (TextView) findViewById(R.id.spokenLanguage);
                    genre = (TextView) findViewById(R.id.genres);
                    statu = (TextView) findViewById(R.id.status);
                    productionCountry = (TextView) findViewById(R.id.prodCountry);
                    votesAverage = (TextView) findViewById(R.id.voteAverage);
                    overview = (TextView) findViewById(R.id.overview);
                    image = (ImageView) findViewById(R.id.headImage);
                    final String imagePath = object.get("poster_path").toString();


                    //https://stackoverflow.com/questions/24535924/how-to-get-image-from-url-website-in-imageview-in-android
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                InputStream in = new URL("https://image.tmdb.org/t/p/w300/"+ imagePath).openStream();
                                bmp = BitmapFactory.decodeStream(in);
                            } catch (Exception e) {
                                // log error
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            if (bmp != null)
                                image.setImageBitmap(bmp);
                        }

                    }.execute();
                    //image.setScaleType(ImageView.ScaleType.FIT_XY);

                    release.setText(object.get("release_date").toString());
                    tagline.setText(object.get("tagline").toString());
                    orilanguage.setText(object.get("original_language").toString());
                    String language ="";
                    for(int i=0;i< object.getJSONArray("spoken_languages").length();i++){
                        JSONArray a = new JSONArray();
                        a = object.getJSONArray("spoken_languages");
                        String value = a.getJSONObject(i).get("iso_639_1").toString();
                        if(a.length()>1){
                            language += value +", ";
                        }
                       else{
                            language += value;
                        }
                    }
                    String genres ="";
                    spolanguage.setText(language);
                    for(int i=0;i< object.getJSONArray("genres").length();i++){
                        JSONArray a = new JSONArray();
                        a = object.getJSONArray("genres");
                        String value = a.getJSONObject(i).get("name").toString();
                        if(a.length()>1){
                            genres += value +", ";
                        }
                        else{
                            genres += value;
                        }
                    }
                    genre.setText(genres);
                    statu.setText(object.get("status").toString());
                    String production="";
                    for(int i=0;i< object.getJSONArray("production_countries").length();i++){
                        JSONArray a = new JSONArray();
                        a = object.getJSONArray("production_countries");
                        String value = a.getJSONObject(i).get("iso_3166_1").toString();
                        if(a.length()>1){
                            production += value +", ";
                        }
                        else{
                            production += value;
                        }
                    }
                    productionCountry.setText(production);
                    votesAverage.setText(object.get("vote_average").toString());
                    overview.setText(object.get("overview").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
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
//gridview: //https://developer.android.com/guide/topics/ui/layout/gridview.html