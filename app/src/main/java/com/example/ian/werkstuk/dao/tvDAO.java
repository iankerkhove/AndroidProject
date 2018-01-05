package com.example.ian.werkstuk.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ian.werkstuk.model.movie;
import com.example.ian.werkstuk.model.tvshow;

import java.util.List;

/**
 * Created by ian on 5/01/2018.
 */

@Dao
public interface tvDAO {
    @Query("SELECT * FROM tvshow WHERE title LIKE :titel LIMIT 1")
    tvshow findByName(String titel);

    @Query("SELECT * FROM tvshow WHERE id LIKE :id LIMIT 1")
    tvshow findById(int id);

    @Query("SELECT * FROM tvshow")
    List<tvshow> getAll();

    @Query("SELECT * FROM tvshow ORDER BY date DESC LIMIT 4 ")
    List<tvshow> getTop4();

    @Insert
    void insertAll(List<tvshow> t);

    @Insert
    void insert(tvshow t);

    @Update
    void update(tvshow t);

    @Delete
    void delete(tvshow t);
}
