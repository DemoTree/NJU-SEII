package com.example.cinema.vo;

import com.example.cinema.po.PopularMovie;

public class PopularMovieVO {
    private Integer movieId;
    private String movieName;
    private Double boxoffice;


    public PopularMovieVO(PopularMovie popularMovie){
        this.movieId=popularMovie.getMovieId();
        this.boxoffice=popularMovie.getBoxoffice();
        this.movieName=popularMovie.getMovieName();
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
