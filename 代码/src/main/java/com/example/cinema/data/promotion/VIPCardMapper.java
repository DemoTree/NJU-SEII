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

    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    VIPCard selectCardByUserId(int userId);



    void updateViptype(int type,int userId);

    List<VIPCard> selectAllCard();







    int insertRechangeRecord(RechangeRecord rechangeRecord);
    int insertRechangeRecords(List<RechangeRecord> rechangeRecords);
    void deleteRechangeRecord(int id);
    List<RechangeRecord> selectRechangeRecordbyUser(int userId);
    int insertVipStrategy(VipStrategy vipStrategy);
    void deleteVipStrategy();
    void updateVipStrategy(int type,double cell,double basis,double addition);
    List<VipStrategy> selectLastVipStrategy();
    int insertVipConsume(VipConsume vipConsume);
    void deleteVipConsume(int vipId);
    void updateVipConsume(double consume,int vipId);
    List<VipConsume> selectVipConsumebyVip(int vipId);
    List<VipConsume> selectVipConsumebyMoney(int money);
    void addVipCoupon(int userId,int couponId);

    VipStrategy selectVipStrategybyViptype(int viptype);
    VipStrategy trueselectlaststrategy();








}
