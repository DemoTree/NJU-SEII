package com.example.cinema.po;

public class MovieBoxOffice {
    private Integer movieId;
    private Double boxOffice;
    private String movieName;
    public MovieBoxOffice(Integer movieId,Double boxOffice,String movieName){
        this.movieId=movieId;
        this.boxOffice=boxOffice;
        this.movieName=movieName;

    }
    public MovieBoxOffice(){

    }
    public Integer getMovieId() {
        return movieId;
    }
    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
    public Double getBoxOffice() {
        return boxOffice;
    }
    public void setBoxOffice(Double boxOffice) {
        this.boxOffice = boxOffice;
    }
    public void appendBoxoffice(Double money){
        boxOffice=boxOffice+money;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
