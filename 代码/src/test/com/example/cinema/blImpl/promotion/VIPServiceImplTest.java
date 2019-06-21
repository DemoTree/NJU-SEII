package com.example.cinema.blImpl.promotion;

import com.example.cinema.CinemaApplicationTest;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.po.RechangeRecord;
import com.example.cinema.po.VIPCard;
import com.example.cinema.po.VipStrategy;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.VIPInfoVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class VIPServiceImplTest extends CinemaApplicationTest {


    @Autowired
    private VIPServiceImpl vipService;
    @Autowired
    private VIPCardMapper vipCardMapper;


    @Test
    public void getCardById() {
        VIPCard card=(VIPCard) vipService.getCardById(8).getContent();
        assertEquals(8,card.getId());
    }

    @Test
    public void getVIPInfo() {
        VIPInfoVO vipInfoVO=(VIPInfoVO) vipService.getVIPInfo(103).getContent();
        assertEquals("满200.0送30.0",vipInfoVO.getDescription());
    }

    @Test
    public void charge() {
        vipCardMapper.deleteCardById(12);
        VIPCard vipCard=new VIPCard();
        vipCard.setViptype(1);
        vipCard.setBalance(100);
        vipCard.setUserId(1);
        vipCardMapper.insertOneCard(vipCard);
        VIPCardForm vipCardForm=new VIPCardForm();
        vipCardForm.setAmount(1);
        vipCardForm.setVipId(13);
        VIPCard thiscard=(VIPCard) vipService.charge(vipCardForm).getContent();
        vipCardMapper.deleteCardById(13);


        assertEquals(vipCard.getBalance()+1,thiscard.getBalance(),0.0);



    }

    @Test
    public void getCardByUserId() {

    }

    @Test
    public void getRechangeRecordByUserId() {
        List<RechangeRecord> list=(ArrayList<RechangeRecord>)vipService.getRechangeRecordByUserId(103).getContent();
        for (int i=0;i<list.size();i++)
           assertEquals(vipCardMapper.selectRechangeRecordbyUser(103).get(i).getId(),list.get(i).getId());
    }

    @Test
    public void getLastVipStrategy() {
        List<VipStrategy> list=(ArrayList<VipStrategy>)vipService.getLastVipStrategy().getContent();
        for (int i=0;i<list.size();i++)
            assertEquals(vipCardMapper.selectLastVipStrategy().get(i).getId(),list.get(i).getId());
    }

}