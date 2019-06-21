package com.example.cinema.vo;

public class RefundForm {
    private int time;

    private double persent;//折扣

    public void setTime(int minute){
        this.time=minute;
    }
    public int getTime(){
        return time;
    }
    public void setPersent(double persent){
        this.persent=persent;
    }
    public double getPersent(){
        return persent;
    }
}
