package com.example.cinema.po;

public class Refund {
    /**
     * 策略id
     */
    private int id;
    /**
     * 退票截止时间
     */
    private int time;
    /**
     * 退票金额比例
     */
    private double persent;

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
