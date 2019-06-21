package com.example.cinema.vo;

public class VipStrategyVO {
    private int id;
    private double price;//会员卡价格
    private int viptype;//会员卡类型
    private double basis;//基础充值金额
    private double addition;//基础赠送额度
    private double cell;//升级上限


    public void setCell(double cell) {
        this.cell = cell;
    }

    public double getCell() {
        return cell;
    }



    public VipStrategyVO(){

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

    public void setViptype(int viptype) {
        this.viptype = viptype;
    }

    public int getViptype() {
        return viptype;
    }


}
