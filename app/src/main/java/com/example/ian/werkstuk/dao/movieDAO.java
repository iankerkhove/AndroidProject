package com.example.ian.werkstuk.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ian.werkstuk.model.movie;

import java.util.List;

/**
 * Created by ian on 13/12/2017.
 */

@Dao
public interface movieDAO {

    @Query("SELECT * FROM movie WHERE title LIKE :titel LIMIT 1")
    movie findByName(String titel);

    @Query("SELECT * FROM movie WHERE id LIKE :id LIMIT 1")
    movie findById(int id);

    @Query("SELECT * FROM movie")
    List<movie> getAll();

    @Insert
    void insertAll(List<movie> m);

    @Insert
    void insert(movie m);

    @Update
    void update(movie m);

    @Delete
    void delete(movie m);
}