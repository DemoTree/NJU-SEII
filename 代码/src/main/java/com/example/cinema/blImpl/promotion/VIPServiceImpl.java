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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liying on 2019/4/14.
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
            ConsumeHistory currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(userId);
            currentConsumeHistory.setAmountOfMoney(VIPCard.price);
            currentConsumeHistory.setConsumeType(0);
            currentConsumeHistory.setConsumeWay(1);
            currentConsumeHistory.setConsumeCardId(123123123);
            ticketMapper.insertConsumeHistory(currentConsumeHistory);//插入新的消费记录

            VipConsume vipConsume=new VipConsume();
            vipConsume.setUserId(userId);
            vipConsume.setVipId(vipCardMapper.selectCardByUserId(userId).getId());
            vipConsume.setConsume(0);
            vipCardMapper.insertVipConsume(vipConsume);//插入新的会员消费总和



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
        int viptype=vipCardMapper.selectCardByUserId(userId)==null?1:vipCardMapper.selectCardByUserId(userId).getViptype();
        VipStrategy vipStrategy=vipCardMapper.selectVipStrategybyViptype(viptype);
        String description="满"+vipStrategy.getBasis()+"送"+vipStrategy.getAddition();



        List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(userId);
        int totalrecord=0;
        for(int j=0;j<rechangeRecordList.size();j++){
            totalrecord+=rechangeRecordList.get(j).getAmountOfMoney();
        }


        double chaju=vipStrategy.getCell()-totalrecord;
        String level="";
        if (chaju<0){
            level="恭喜你已到达最高等级";
        }else {
            level="距离下一级仍需充值"+chaju+"元";}


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
        double balance = vipCard.calculate(vipCardForm.getAmount(),vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getBasis(),vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getAddition());
        double bonus=balance-vipCardForm.getAmount();
        vipCard.setBalance(vipCard.getBalance() + balance);
        try {
            vipCardMapper.updateCardBalance(vipCardForm.getVipId(), vipCard.getBalance());
            RechangeRecord rechangeRecord=new RechangeRecord();
            rechangeRecord.setAmountOfMoney(vipCardForm.getAmount());
            rechangeRecord.setConsumeCardId(123123123);
            rechangeRecord.setUserId(vipCard.getUserId());
            rechangeRecord.setBalance(vipCard.getBalance());
            rechangeRecord.setBonus(bonus);
            vipCardMapper.insertRechangeRecord(rechangeRecord);

            List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(vipCard.getUserId());
            int totalrecord=0;
            for(int i=0;i<rechangeRecordList.size();i++){
                totalrecord+=rechangeRecordList.get(i).getAmountOfMoney();
            }


            while(totalrecord>=vipCardMapper.selectVipStrategybyViptype(vipCard.getViptype()).getCell()&&vipCard.getViptype()<vipCardMapper.trueselectlaststrategy().getViptype()){

                vipCardMapper.updateViptype(vipCard.getViptype()+1,vipCard.getUserId());
                vipCard.setViptype(vipCard.getViptype()+1);
                System.out.println("正在循环");
            };






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
        System.out.println(viptype);
        System.out.println(cell);
        System.out.println(basis);
        System.out.println(addition);
        try {
            vipCardMapper.updateVipStrategy(viptype,cell,basis,addition);


            //这里所有的会员都应该更新他们的会员等级
            List<VIPCard> vipCardList=vipCardMapper.selectAllCard();
            VIPCard currentvipCard;
            for (int i=0;i<vipCardList.size();i++){
                currentvipCard=vipCardList.get(i);


                List<RechangeRecord> rechangeRecordList=vipCardMapper.selectRechangeRecordbyUser(currentvipCard.getUserId());
                int totalrecord=0;
                for(int j=0;j<rechangeRecordList.size();j++){
                    totalrecord+=rechangeRecordList.get(j).getAmountOfMoney();
                }

                while(totalrecord>=vipCardMapper.selectVipStrategybyViptype(currentvipCard.getViptype()).getCell()&&currentvipCard.getViptype()<vipCardMapper.trueselectlaststrategy().getViptype()){

                    vipCardMapper.updateViptype(currentvipCard.getViptype()+1,currentvipCard.getUserId());
                    currentvipCard.setViptype(currentvipCard.getViptype()+1);
                };

                if (currentvipCard.getViptype()!=1){


                    while (totalrecord < vipCardMapper.selectVipStrategybyViptype(currentvipCard.getViptype() - 1).getCell() && currentvipCard.getViptype() > 1) {
                        vipCardMapper.updateViptype(currentvipCard.getViptype() - 1, currentvipCard.getUserId());
                        currentvipCard.setViptype(currentvipCard.getViptype() - 1);
                        if (currentvipCard.getViptype() == 1) break;
                    }
                }







            }




            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }


    @Override
    public ResponseVO addVipStrategy(VipStrategyForm vipStrategyForm){
        try {
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






}
