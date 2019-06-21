package com.example.cinema.vo;

public class VipStrategyForm {
    private int id;
    private double price;//会员卡价格
    private int viptype;//会员卡类型
    private double basis;//基础充值金额
    private double addition;//基础赠送额度
    private double cell;//升级上限

    public double getCell() {
        return cell;
    }


    public int getType() {
        return viptype;
    }

    public void setType(int viptype) {
        this.viptype = viptype;
    }

    public void setCell(double cell) {
        this.cell = cell;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBasis() {
        return basis;
    }

    public void setBasis(double basis) {
        this.basis = basis;
    }

    public double getAddition() {
        return addition;
    }

    public void setAddition(double addition) {
        this.addition = addition;
    }
}
