package com.example.ian.werkstuk;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    private ListView responseView;
    private EditText txtSearch;
    private String BASEMOVIE_URL = "https://api.themoviedb.org/3/search/movie?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&query=";
    private String BASETV_URL = "https://api.themoviedb.org/3/search/tv?api_key=1da7f7f08b98f2fb0be745269a36728b&language=en-US&query=";
    private String request;
    private RadioButton rdbMovie;
    private RadioButton rdbTVShow;
    private  ArrayList<HashMap<String,String>> lijst = new ArrayList<HashMap<String, String>>();

    //https://forum.unity.com/threads/reliable-way-to-detect-tablet-on-android.127184
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
        if(isTablet()){
            setContentView(R.layout.fragment_test);
        }
        else{
            setContentView(R.layout.activity_search);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        }



        responseView = (ListView) findViewById(R.id.responseView);
         rdbMovie = (RadioButton) findViewById(R.id.rdbMovie);
         rdbTVShow = (RadioButton) findViewById(R.id.rdbTVShow);

        responseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = lijst.get(position).get("id");
                String titel = lijst.get(position).get("naam");
                String soort = lijst.get(position).get("sort");

                Intent i = new Intent(SearchActivity.this, DetailActivity.class);
                i.putExtra("naam", titel);
                i.putExtra("id",key);
                i.putExtra("sort", soort);
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
                break;
            default:
                break;
        }

        return true;
    }
    public void btnSearch(View v){
        String search = "";
        txtSearch = (EditText) findViewById(R.id.searchField);

        if(rdbMovie.isChecked()){
            search  = txtSearch.getText().toString().replace(" ","%20");
                request = BASEMOVIE_URL + search;
                new SearchActivity.RetrieveFeedTask().execute();

        }else if(rdbTVShow.isChecked()){
            search = txtSearch.getText().toString().replace(" ","%20");
             request = BASETV_URL + search;
            new SearchActivity.RetrieveFeedTask().execute();

        }
        else {
            Toast toast = Toast.makeText(this,"@string/toast_invalidrdb",Toast.LENGTH_LONG);
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
            else{
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
                for(int i=0; i<result.length();i++){
                    try {
                        HashMap<String,String> tempList = new HashMap<String, String>();
                        JSONObject temp = (JSONObject) result.get(i);

                        if(rdbMovie.isChecked()){
                            tempList.put("id", temp.getString("id"));
                            tempList.put("naam", temp.getString("original_title"));
                            tempList.put("sort","movie");
                        }else{
                            tempList.put("id", temp.getString("id"));
                            tempList.put("naam", temp.getString("name"));
                            tempList.put("sort","tv");
                        }
                        lijst.add(tempList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                ListAdapter adapter = new SimpleAdapter(
                        SearchActivity.this,lijst,R.layout.list_view,new String[]{"naam"}, new int[]{R.id.movieName});
                responseView.setAdapter(adapter);

            }
        }
    }
}
//listview: https://www.androidhive.info/2011/10/android-listview-tutorial/