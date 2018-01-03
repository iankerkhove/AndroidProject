package com.example.ian.werkstuk;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.ian.werkstuk.dao.DB;
import com.example.ian.werkstuk.model.movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbTest {
    DB db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, DB.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


    @Test
    public void testDatabase() throws Exception {
        movie film = new movie();
        film.setId(1);
        film.setTitle("toffeFilm");
        film.setGenre("action");
        film.setSpoLanguage("en");
        film.setReleasedate("10-12-2019");

        db.MovieDAO().insert(film);

        movie film2 = db.MovieDAO().findById(1);

        assertEquals(film.getId(), film2.getId());
        assertEquals(film.getTitle(), film2.getTitle());
        assertEquals(film.getGenre(), film2.getGenre());
        assertEquals(film.getSpoLanguage(), film2.getSpoLanguage());
        assertEquals(film.getReleasedate(), film2.getReleasedate());


        assertEquals(db.MovieDAO().getAll().size(),1);

        db.MovieDAO().delete(film);

        assertEquals(db.MovieDAO().getAll().size(),0);
    }
}
