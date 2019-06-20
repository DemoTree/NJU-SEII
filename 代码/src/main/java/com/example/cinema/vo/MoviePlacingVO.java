package com.example.cinema.vo;

public class MoviePlacingVO {
    private Double palcingrate;
    private String movieName;
    public MoviePlacingVO(){}

    public void setMovieName(String movieName) {
        this.movieName = movieName;
        this.palcingrate=0.0;
    }

    public String getMovieName() {
        return movieName;
    }

    public Double getPalcingrate() {
        return palcingrate;
    }

    public void setPalcingrate(Double palcingrate) {
        this.palcingrate = palcingrate;
    }
}
