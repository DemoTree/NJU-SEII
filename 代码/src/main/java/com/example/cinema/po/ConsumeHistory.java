package com.example.cinema.po;

import com.example.cinema.vo.ConsumeHistoryVO;

import java.sql.Timestamp;

public class ConsumeHistory {
    private int id;
    private int userId;
    private double amountOfMoney;
    private Timestamp consumeTime;
    private int consumeType;
    private int consumeWay;
    private int consumeCardId;
    public ConsumeHistory(){

    };
    public ConsumeHistoryVO getVO(){
        ConsumeHistoryVO vo=new ConsumeHistoryVO();
        vo.setAmountOfMoney(this.getAmountOfMoney());
        vo.setConsumeCardId(this.getConsumeCardId());
        vo.setConsumeTime(this.getConsumeTime());
        vo.setConsumeType(this.getConsumeType());
        vo.setConsumeWay(this.getConsumeWay());
        vo.setId(this.getId());
        vo.setUserId(this.getUserId());
        return vo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public Timestamp getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Timestamp consumeTime) {
        this.consumeTime = consumeTime;
    }

    public int getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(int consumeType) {
        this.consumeType = consumeType;
    }

    public int getConsumeWay() {
        return consumeWay;
    }

    public void setConsumeWay(int consumeWay) {
        this.consumeWay = consumeWay;
    }

    public int getConsumeCardId() {
        return consumeCardId;
    }

    public void setConsumeCardId(int consumeCardId) {
        this.consumeCardId = consumeCardId;
    }
}
