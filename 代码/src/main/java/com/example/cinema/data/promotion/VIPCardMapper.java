package com.example.cinema.data.promotion;

import com.example.cinema.po.RechangeRecord;
import com.example.cinema.po.VIPCard;
import com.example.cinema.po.VipConsume;
import com.example.cinema.po.VipStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liying on 2019/4/14.
 */
@Mapper
public interface VIPCardMapper {

    int insertOneCard(VIPCard vipCard);

    VIPCard selectCardById(int id);

    void deleteCardById(int id);

    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    VIPCard selectCardByUserId(int userId);

    void updateViptype(int type,int userId);//更新会员卡等级

    List<VIPCard> selectAllCard();

    int insertRechangeRecord(RechangeRecord rechangeRecord);//插入充值记录

    List<RechangeRecord> selectRechangeRecordbyUser(int userId);//根据用户返回充值记录

    int insertVipStrategy(VipStrategy vipStrategy);//插入会员卡优惠策略

    void updateVipStrategy(int type,double cell,double basis,double addition);

    List<VipStrategy> selectLastVipStrategy();


    //会员消总和
    int insertVipConsume(VipConsume vipConsume);
    void updateVipConsume(double consume,int vipId);
    List<VipConsume> selectVipConsumebyVip(int vipId);
    List<VipConsume> selectVipConsumebyMoney(int money);//根据消费金额返回所有大于此金额的会员信息

    void addVipCoupon(int userId,int couponId);//给指定会员赠送优惠券

    VipStrategy selectVipStrategybyViptype(int viptype);//根据不同等级获取会员策略

    VipStrategy trueselectlaststrategy();//获取最高等级的会员策略








}
