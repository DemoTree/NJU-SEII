package com.example.cinema.po;

import com.example.cinema.vo.RechangeRecordVO;

import java.sql.Timestamp;

public class RechangeRecord {
    private int id;
    private int userId;
    private double amountOfMoney;
    private Timestamp rechangeTime;
    private int consumeCardId;
    private double balance;
    private double bonus;
    public RechangeRecord(){

    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public RechangeRecordVO getVO(){
        RechangeRecordVO vo=new RechangeRecordVO();
        vo.setAmountOfMoney(this.getAmountOfMoney());
        vo.setConsumeCardId(this.getConsumeCardId());
        vo.setId(this.getId());
        vo.setRechangeTime(this.getRechangeTime());
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

    public Timestamp getRechangeTime() {
        return rechangeTime;
    }

    public void setRechangeTime(Timestamp rechangeTime) {
        this.rechangeTime = rechangeTime;
    }

    public int getConsumeCardId() {
        return consumeCardId;
    }

    public void setConsumeCardId(int consumeCardId) {
        this.consumeCardId = consumeCardId;
    }
}
