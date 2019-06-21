package com.example.cinema.vo;

public class VipStrategyVO {
    private int id;
    private double price;
    private double basis;
    private double addition;
    private int viptype;
    private double cell;

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
