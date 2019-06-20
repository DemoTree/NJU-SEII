package com.example.cinema.po;

public class PopularMovie {
    private Integer movieId;
    private String movieName;
    private Double boxoffice;
    public PopularMovie(){

    }

    public Double getBoxoffice() {
        return boxoffice;
    }
    public Integer getMovieId(){
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setBoxoffice(Double boxoffice) {
        this.boxoffice = boxoffice;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
