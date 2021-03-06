package com.example.ian.werkstuk;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ian.werkstuk.dao.DB;
import com.example.ian.werkstuk.model.movie;
import com.example.ian.werkstuk.model.tvshow;

public class DetailLocalActivity extends AppCompatActivity {
    private DB database = null;
    private int id;
    private TextView titel;
    private TextView tagline;
    private TextView release;
    private TextView orilanguage;
    private TextView spolanguage;
    private TextView spolanguageLabel;
    private TextView genre;
    private TextView statu;
    private TextView productionCountryLabel;
    private TextView productionCountry;
    private TextView votesAverage;
    private TextView overview;
    private ImageView image;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_local);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


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
        titel = (TextView) findViewById(R.id.title);

        database = DB.getDb(this);
        sharedPreferences = getSharedPreferences("key_clr", Context.MODE_PRIVATE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        Intent i = getIntent();
        if (i.getStringExtra("sort").equals("movie")) {
            id = i.getIntExtra("movieId", 0);
            fillMovie();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movie m = database.MovieDAO().findById(id);
                    database.MovieDAO().delete(m);

                    Snackbar.make(view, "Movie successfully deleted", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else if (i.getStringExtra("sort").equals("tv")) {
            id = i.getIntExtra("tvId", 0);
            fillTv();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvshow t = database.TvDAO().findById(id);
                    database.TvDAO().delete(t);

                    Snackbar.make(view, "Tv show successfully deleted", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        //set color of action bar
        CollapsingToolbarLayout t = findViewById(R.id.toolbar_layout);
        int r=sharedPreferences.getInt("a_r",0);
        int g=sharedPreferences.getInt("a_g",0);
        int b=sharedPreferences.getInt("a_b",0);
        t.setBackground(
                new ColorDrawable(Color.rgb(r,g,b)));
    }

    public void fillTv() {
        tvshow t = database.TvDAO().findById(id);
        titel.setText(t.getTitle());
        release.setText(t.getReleasedate());
        orilanguage.setText(t.getOriLanguage());
        spolanguage.setText(t.getNumberOfSeasons());
        spolanguageLabel.setText("Number of seasons: ");
        genre.setText(t.getGenre());
        statu.setText(t.getStatus());
        productionCountryLabel.setText("Production Company's");
        productionCountry.setText(t.getProdCompany());
        votesAverage.setText(t.getVoteAverage());
        overview.setText(t.getOverview());
    }

    public void fillMovie() {
        movie m = database.MovieDAO().findById(id);
        titel.setText(m.getTitle());
        tagline.setText(m.getTagline());
        release.setText(m.getReleasedate());
        orilanguage.setText(m.getOriLanguage());
        spolanguage.setText(m.getSpoLanguage());
        genre.setText(m.getGenre());
        statu.setText(m.getStatus());
        productionCountry.setText(m.getProdCountry());
        votesAverage.setText(m.getVoteAverage());
        overview.setText(m.getOverview());
    }
}
