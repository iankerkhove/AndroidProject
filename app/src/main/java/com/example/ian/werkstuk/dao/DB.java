package com.example.ian.werkstuk.dao;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ian.werkstuk.model.movie;

/**
 * Created by ian on 13/12/2017.
 */
@Database(entities = {movie.class},version=1)
public abstract class DB extends RoomDatabase {
    public abstract movieDAO MovieDAO();
    public static DB getDb(Context context){
        return Room.databaseBuilder(context.getApplicationContext(),DB.class,"movieDb").build();
    }

}
