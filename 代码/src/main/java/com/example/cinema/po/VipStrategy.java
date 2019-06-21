package com.example.cinema.po;

import com.example.cinema.vo.VipStrategyVO;

public class VipStrategy {
    private int id;
    private double price;
    private double basis;
    private double addition;
    private int viptype;
    private double cell;

    public double getCell() {
        return cell;
    }

    public void setCell(double cell) {
        this.cell = cell;
    }

    public void setViptype(int viptype) {
        this.viptype = viptype;
    }

    public int getViptype() {
        return viptype;
    }

    public VipStrategyVO getVO(){
        VipStrategyVO vo=new VipStrategyVO();
        vo.setAddition(this.getAddition());
        vo.setBasis(this.getBasis());
        vo.setId(this.getId());
        vo.setPrice(this.getPrice());
        vo.setViptype(this.getViptype());
        return vo;

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
