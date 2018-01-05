package com.example.ian.werkstuk.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.media.Image;

import java.util.Calendar;

/**
 * Created by ian on 27/12/2017.
 */

@Entity
public class tvshow {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "releasedate")
    private String releasedate;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "originalLanguage")
    private String oriLanguage;

    @ColumnInfo(name = "productionCompany")
    private String prodCompany;

    @ColumnInfo(name = "numberOfSeasons")
    private String numberOfSeasons;

    @ColumnInfo(name = "voteAverage")
    private String voteAverage;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "date")
    private String cal = Calendar.getInstance().toString();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriLanguage() {
        return oriLanguage;
    }

    public void setOriLanguage(String oriLanguage) {
        this.oriLanguage = oriLanguage;
    }

    public String getProdCompany() {
        return prodCompany;
    }

    public void setProdCompany(String prodCompany) {
        this.prodCompany = prodCompany;
    }

    public String getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(String numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }


    @Override
    public String toString() {
        return title;
    }
}

