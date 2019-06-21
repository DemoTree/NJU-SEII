package com.example.cinema.blImpl.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VIPInfoVO;
import com.example.cinema.vo.VipStrategyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liying on 2019/4/14.
 */
/**
 * @author 蔡明卫
 * @date 6/1
 */

@Service
public class VIPServiceImpl implements VIPService {
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    TicketMapper ticketMapper;

    @Override
    public ResponseVO addVIPCard(int userId) {
        VIPCard vipCard = new VIPCard();
        vipCard.setUserId(userId);
        vipCard.setBalance(0);
        try {
            int id = vipCardMapper.insertOneCard(vipCard);

            //购卡时插入购卡的消费记录
            ConsumeHistory currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(userId);
            currentConsumeHistory.setAmountOfMoney(VIPCard.price);
            currentConsumeHistory.setConsumeType(0);
            currentConsumeHistory.setConsumeWay(1);
            currentConsumeHistory.setConsumeCardId(123123123);
            ticketMapper.insertConsumeHistory(currentConsumeHistory);


            //会员开户时数据库新建会员消费的记录
            VipConsume vipConsume=new VipConsume();
            vipConsume.setUserId(userId);
            vipConsume.setVipId(vipCardMapper.selectCardByUserId(userId).getId());
            vipConsume.setConsume(0);
            vipCardMapper.insertVipConsume(vipConsume);


            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getCardById(int id) {
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectCardById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getVIPInfo(int userId) {
        VIPInfoVO vipInfoVO = new VIPInfoVO();
        //根据会员等级获取相应的优惠策略
        int viptype=vipCardMapper.selectCardByUserId(userId)==null?1:vipCardMapper.selectCardByUserId(userId).getViptype();
        VipStrategy vipStrategy=vipCardMapper.selectVipStrategybyViptype(viptype);
        //计算满减
        String description="满"+vipStrategy.getBasis()+"送"+vipStrategy.getAddition();

        //计算会员距离下一级的差距
        List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(userId);
        int totalrecord=0;
        for(int j=0;j<rechangeRecordList.size();j++){
            totalrecord+=rechangeRecordList.get(j).getAmountOfMoney();
        }
        double gap=vipStrategy.getCell()-totalrecord;
        String level="";
        if (gap<0){
            level="恭喜你已到达最高等级";
        }else {
            level="距离下一级仍需充值"+gap+"元";}


        vipInfoVO.setDescription(description);
        vipInfoVO.setPrice(vipStrategy.getPrice());
        vipInfoVO.setLevel(level);
        return ResponseVO.buildSuccess(vipInfoVO);
    }

    @Override
    public ResponseVO charge(VIPCardForm vipCardForm) {

        VIPCard vipCard = vipCardMapper.selectCardById(vipCardForm.getVipId());
        if (vipCard == null) {
            return ResponseVO.buildFailure("会员卡不存在");
        }
        //计算实到金额
        double balance = vipCard.calculate(vipCardForm.getAmount(),vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getBasis(),vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getAddition());
       //计算赠送金额
        double bonus=balance-vipCardForm.getAmount();
        //更新会员卡余额信息
        vipCard.setBalance(vipCard.getBalance() + balance);
        try {
            //插入充值记录
            vipCardMapper.updateCardBalance(vipCardForm.getVipId(), vipCard.getBalance());
            RechangeRecord rechangeRecord=new RechangeRecord();
            rechangeRecord.setAmountOfMoney(vipCardForm.getAmount());
            rechangeRecord.setConsumeCardId(123123123);
            rechangeRecord.setUserId(vipCard.getUserId());
            rechangeRecord.setBalance(vipCard.getBalance());
            rechangeRecord.setBonus(bonus);
            vipCardMapper.insertRechangeRecord(rechangeRecord);

            //计算至今充值金额并判断会员卡是否应该升级
            List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(vipCard.getUserId());
            int totalrecord=0;
            for(int i=0;i<rechangeRecordList.size();i++){
                totalrecord+=rechangeRecordList.get(i).getAmountOfMoney();
            }
            //判断等级
            while(totalrecord>=vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getCell()&&vipCard.getViptype()<vipCardMapper.trueselectlaststrategy().getViptype()){
                vipCardMapper.updateViptype(vipCard.getViptype()+1,vipCard.getUserId());
                vipCard.setViptype(vipCard.getViptype()+1);
            }

            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getCardByUserId(int userId) {
        try {
            VIPCard vipCard = vipCardMapper.selectCardByUserId(userId);
            if(vipCard==null){
                return ResponseVO.buildFailure("用户卡不存在");
            }
            return ResponseVO.buildSuccess(vipCard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getRechangeRecordByUserId(int userId){
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectRechangeRecordbyUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO getLastVipStrategy(){
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectLastVipStrategy());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO updateVipStrategy(double basis,double addition,double cell,int viptype){
        try {
            vipCardMapper.updateVipStrategy(viptype,cell,basis,addition);
            //更新优惠策略后考虑对已有会员卡用户的影响
            List<VIPCard> vipCardList=vipCardMapper.selectAllCard();
            VIPCard currentvipCard;
            for (int i=0;i<vipCardList.size();i++){//循环所有已有的会员卡
                currentvipCard=vipCardList.get(i);
                List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(currentvipCard.getUserId());
                int totalrecord=0;
                for(int j=0;j<rechangeRecordList.size();j++){
                    totalrecord+=rechangeRecordList.get(j).getAmountOfMoney();
                }//计算总充值额
                while(totalrecord>=vipCardMapper.selectVipStrategybyViptype(currentvipCard.getViptype()).getCell()&&currentvipCard.getViptype()<vipCardMapper.trueselectlaststrategy().getViptype()){
                    vipCardMapper.updateViptype(currentvipCard.getViptype()+1,currentvipCard.getUserId());
                    currentvipCard.setViptype(currentvipCard.getViptype()+1);
                }//是否应该升级
                if (currentvipCard.getViptype()!=1){
                    while (totalrecord < vipCardMapper.selectVipStrategybyViptype(currentvipCard.getViptype() - 1).getCell() && currentvipCard.getViptype() > 1) {
                        vipCardMapper.updateViptype(currentvipCard.getViptype() - 1, currentvipCard.getUserId());
                        currentvipCard.setViptype(currentvipCard.getViptype() - 1);
                        if (currentvipCard.getViptype() == 1) break;
                    }
                }//是否应该降级（1级会员卡不降级）
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }


    @Override
    public ResponseVO addVipStrategy(VipStrategyForm vipStrategyForm){
        try {//增加会员优惠策略
            VipStrategy vipStrategy=new VipStrategy();
            vipStrategy.setAddition(vipStrategyForm.getAddition());
            vipStrategy.setBasis(vipStrategyForm.getBasis());
            vipStrategy.setViptype(vipCardMapper.trueselectlaststrategy().getViptype()+1);
            vipStrategy.setCell(vipStrategyForm.getCell());
            vipCardMapper.insertVipStrategy(vipStrategy);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO getVipConsumebyMoney(int money) {
        try {
            return ResponseVO.buildSuccess(vipCardMapper.selectVipConsumebyMoney(money));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    @Override
    public ResponseVO addCoupon(List<Integer> idList,int couponId) {
        try {
            for (int i=0;i<idList.size();i++){
                vipCardMapper.addVipCoupon(idList.get(0),couponId);
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    //模拟会员卡
    public VIPCard Stub1(){
        System.out.println("return a vipcard");
        VIPCard card=new VIPCard();
        card.setId(8);
        card.setUserId(15);
        card.setBalance(100.00);
        card.setViptype(0);
        card.setJoinDate(new Timestamp(11231));

        return card;
    }


    //模拟获取的优惠策略
    public VipStrategy Stub2(){
        System.out.println("return a vipstrategy");
        VipStrategy vipStrategy=new VipStrategy();
        vipStrategy.setCell(200);
        vipStrategy.setPrice(25);
        vipStrategy.setViptype(1);
        vipStrategy.setBasis(200);
        vipStrategy.setAddition(30);
        vipStrategy.setId(30);

        return vipStrategy;
    }


    //模拟充值记录
    public List<RechangeRecord> Stub3(){
        System.out.println("retuen Rechangerecord");
        RechangeRecord rechangeRecord=new RechangeRecord();
        rechangeRecord.setBonus(30);
        rechangeRecord.setBalance(2000);
        rechangeRecord.setUserId(13);
        rechangeRecord.setConsumeCardId(123123123);
        rechangeRecord.setAmountOfMoney(200);
        rechangeRecord.setId(8);
        List<RechangeRecord> rechangeRecordList=new ArrayList<>();
        rechangeRecordList.add(rechangeRecord);
        return rechangeRecordList;
    }






}
