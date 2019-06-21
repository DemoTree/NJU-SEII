package com.example.cinema.bl.promotion;

import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VipStrategyForm;

import java.util.List;


/**
 * Created by liying on 2019/4/14.
 */

public interface VIPService {

    ResponseVO addVIPCard(int userId);

    ResponseVO getCardById(int id);

    ResponseVO getVIPInfo(int userId);

    ResponseVO charge(VIPCardForm vipCardForm);

    ResponseVO getCardByUserId(int userId);

    ResponseVO getRechangeRecordByUserId(int userId);

    ResponseVO getLastVipStrategy();

    ResponseVO updateVipStrategy(double basis,double addition,double cell,int viptype);

    ResponseVO addVipStrategy(VipStrategyForm vipStrategyForm);

    ResponseVO getVipConsumebyMoney(int money);//根据消费金额获取满足条件的用户

    ResponseVO addCoupon(List<Integer> userList,int couponId);//指定用户赠送优惠券



}
