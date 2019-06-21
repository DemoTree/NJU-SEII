package com.example.cinema.vo;

import com.example.cinema.po.Refund;

public class RefundVO {
    private int id;
    private int time;
    private double persent;//折扣

    public RefundVO(Refund refund){
        this.id=refund.getId();
        this.time=refund.getTime();
        this.persent=refund.getPersent();
    }

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
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
}
