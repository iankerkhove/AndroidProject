package com.example.ian.werkstuk.model;

/**
 * Created by ian on 13/12/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
}
