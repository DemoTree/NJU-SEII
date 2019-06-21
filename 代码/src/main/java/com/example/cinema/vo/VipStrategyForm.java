package com.example.cinema.vo;

public class VipStrategyForm {
    private int id;
    private double price;
    private int viptype;
    private double basis;
    private double addition;
    private double cell;

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
