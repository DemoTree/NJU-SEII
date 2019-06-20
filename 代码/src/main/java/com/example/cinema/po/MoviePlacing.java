package com.example.cinema.po;

import java.text.DecimalFormat;

public class MoviePlacing {
    private Integer movieId;
    private String movidName;
    private Double placingrate;
    private Integer numofaudience;
    private Integer numofschedule;
    public MoviePlacing(Integer movieId,String movidName){
        this.movieId=movieId;
        this.movidName=movidName;
        this.placingrate=0.0;
        this.numofaudience=0;
        this.numofschedule=0;

    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Double getPlacingrate() {
        return placingrate;
    }

    public Integer getNumofaudience() {
        return numofaudience;
    }

    public Integer getNumofschedule() {
        return numofschedule;
    }

    public String getMovidName() {
        return movidName;
    }

    public void setMovidName(String movidName) {
        this.movidName = movidName;
    }

    public void setNumofaudience(Integer numofaudience) {
        this.numofaudience = numofaudience;
    }

    public void setNumofschedule(Integer numofschedule) {
        this.numofschedule = numofschedule;
    }

    public void setPlacingrate(Double placingrate) {
        this.placingrate = placingrate;
    }

    public void addnumofschedule(){
        numofschedule++;
    }

    public void addnumofaudience(Integer num){
        numofaudience+=num;

    }


}
