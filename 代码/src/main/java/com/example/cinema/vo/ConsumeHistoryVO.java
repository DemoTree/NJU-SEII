package com.example.cinema.vo;

import java.sql.Timestamp;

public class ConsumeHistoryVO {

    private int id;
    private int userId;
    private double amountOfMoney;//消费总额
    private Timestamp consumeTime;//消费时间
    private int consumeType;//消费类型（购买会员卡0，购买电影票1，退票2）
    private int consumeWay;//付款方式（会员卡0，银行卡1）
    private int consumeCardId;//付款卡号
    public ConsumeHistoryVO(){

    };

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
