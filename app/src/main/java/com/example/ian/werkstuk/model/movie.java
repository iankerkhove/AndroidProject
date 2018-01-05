package com.example.ian.werkstuk.model;

/**
 * Created by ian on 13/12/2017.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.media.Image;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
public class movie implements Serializable {
    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "tagline")
    private String tagline;

    @ColumnInfo(name = "releasedate")
    private String releasedate;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "originalLanguage")
    private String oriLanguage;

    @ColumnInfo(name = "spokenLanguage")
    private String spoLanguage;

    @ColumnInfo(name = "productionCountry")
    private String prodCountry;

    @ColumnInfo(name = "voteAverage")
    private String voteAverage;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "date")
    private String cal = Calendar.getInstance().toString();

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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

    public String getSpoLanguage() {
        return spoLanguage;
    }

    public void setSpoLanguage(String spoLanguage) {
        this.spoLanguage = spoLanguage;
    }

    public String getProdCountry() {
        return prodCountry;
    }

    public void setProdCountry(String prodCountry) {
        this.prodCountry = prodCountry;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
