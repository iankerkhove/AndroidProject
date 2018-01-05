package com.example.ian.werkstuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ian.werkstuk.dao.DB;
import com.example.ian.werkstuk.model.movie;
import com.example.ian.werkstuk.model.tvshow;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    DB database = null;
    ListView listViewM=null;
    ListView listViewT= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        database = DB.getDb(this);
        listViewM = findViewById(R.id.movieView);
        listViewT = findViewById(R.id.tvView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //lijst voor opgeslagen films
        final List<movie> films = database.MovieDAO().getAll();
        final List<tvshow> tvshows = database.TvDAO().getAll();
        listViewM.setAdapter(new ArrayAdapter<movie>(this,R.layout.list_view,R.id.movieName,films));
        listViewT.setAdapter(new ArrayAdapter<tvshow>(this,R.layout.list_view,R.id.movieName,tvshows));


        listViewM.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = films.get(position).getId();

                Intent i = new Intent(FavoriteActivity.this, DetailLocalActivity.class);
                i.putExtra("movieId", key);
                i.putExtra("sort","movie");
                startActivity(i);
            }
        });
        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int key = tvshows.get(position).getId();

                Intent i = new Intent(FavoriteActivity.this, DetailLocalActivity.class);
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

                break;
            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //Intent i = new Intent(MainActivity.this,FavoriteActivity.class);
                //startActivity(i);
                Toast.makeText(this, "Already in favorites", Toast.LENGTH_SHORT)
                        .show();
                return true;
            default:
                break;
        }

        return true;
    }
}
