package com.example.cinema.po;

import com.example.cinema.vo.VipStrategyVO;

public class VipStrategy {
    private int id;
    private double price;//会员卡价格
    private double basis;//基础充值金额
    private double addition;//满赠金额
    private int viptype;//会员卡类型
    private double cell;//会员卡升级上限

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
