package com.example.cinema.po;

import com.example.cinema.vo.TicketVO;
import com.example.cinema.vo.TicketWithScheduleVO;

import java.sql.Timestamp;

/**
 * Created by liying on 2019/4/16.
 */
public class Ticket {

    /**
     * 电影票id
     */
    private int id;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 排片id
     */
    private int scheduleId;
    /**
     * 列号
     */
    private int columnIndex;
    /**
     * 排号
     */
    private int rowIndex;
    /**
     * 实际价钱
     */
    private double price;
    /**
     * 是否用会员卡
     */
    private boolean ifUseVIP;
    /**
     * 是否出票
     */
    private boolean isOut;
    /**
     * 订单状态：
     * 0：未完成 1：已完成 2:已失效
     */
    private int state;

    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Ticket() {
    }


    public TicketVO getVO() {
        TicketVO vo = new TicketVO();
        vo.setRowIndex(this.getRowIndex());
        vo.setColumnIndex(this.getColumnIndex());
        vo.setScheduleId(this.getScheduleId());
        vo.setId(this.getId());
        vo.setUserId(this.getUserId());
        vo.setPrice(this.getPrice());
        vo.setIfUseVIP(this.getIfUseVIP());
        vo.setIsOut(this.getIsOut());
        String stateString;
        switch (state) {
            case 0:
                stateString = "未完成";
                break;
            case 1:
                stateString = "已完成";
                break;
            case 2:
                stateString = "已失效";
                break;
            case 3:
                stateString = "已退票";
                break;
            default:
                stateString = "未完成";
        }
        vo.setState(stateString);
        vo.setTime(time);
        return vo;

    }
    public TicketWithScheduleVO getWithScheduleVO() {
        TicketWithScheduleVO vo = new TicketWithScheduleVO();
        vo.setRowIndex(this.getRowIndex());
        vo.setColumnIndex(this.getColumnIndex());
        vo.setId(this.getId());
        vo.setUserId(this.getUserId());
        vo.setPrice(this.getPrice());
        vo.setIfUseVIP(this.getIfUseVIP());
        vo.setIsOut(this.getIsOut());
        String stateString;
        switch (state) {
            case 0:
                stateString = "未完成";
                break;
            case 1:
                stateString = "已完成";
                break;
            case 2:
                stateString = "已失效";
                break;
            case 3:
                stateString = "已退票";
                break;
            default:
                stateString = "未完成";
        }
        vo.setState(stateString);
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

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price=price;
    }

    public boolean getIfUseVIP(){
        return ifUseVIP;
    }

    public void setIfUseVIP(boolean VIP){
        this.ifUseVIP=VIP;
    }

    public boolean getIsOut(){
        return isOut;
    }

    public void setIsOut(boolean isOut){
        this.isOut=isOut;
    }
}
