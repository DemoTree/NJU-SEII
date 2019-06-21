package com.example.cinema.controller.promotion;

import com.example.cinema.bl.promotion.VIPService;
import com.example.cinema.vo.VIPCardForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.VipStrategyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liying on 2019/4/14.
 */
@RestController()
@RequestMapping("/vip")
public class VIPCardController {
    @Autowired
    VIPService vipService;

    @PostMapping("/add")
    public ResponseVO addVIP(@RequestParam int userId){
        return vipService.addVIPCard(userId);
    }
    @GetMapping("{userId}/get")
    public ResponseVO getVIP(@PathVariable int userId){
        return vipService.getCardByUserId(userId);
    }

    @PostMapping("/getVIPInfo")
    public ResponseVO getVIPInfo(@RequestParam int userId){

        return vipService.getVIPInfo(userId);
    }

    @PostMapping("/charge")
    public ResponseVO charge(@RequestBody VIPCardForm vipCardForm){
        return vipService.charge(vipCardForm);
    }


    @GetMapping("/getRechangeRecord/{userId}")
    public ResponseVO getRechangeRecord(@PathVariable int userId){
        return vipService.getRechangeRecordByUserId(userId);
    }

    @GetMapping("/getLastVipStrategy")
    public ResponseVO getLastVipStrategy(){
        return vipService.getLastVipStrategy();
    }


    @PostMapping("/updateVipStrategy")
    public ResponseVO updateVipStrategy(@RequestParam double basis,@RequestParam double addition,@RequestParam double cell,@RequestParam int viptype){
        return vipService.updateVipStrategy(basis,addition,cell,viptype);
    }

    @PostMapping("/addVipStrategy")
    public ResponseVO addVipStrategy(@RequestBody VipStrategyForm vipStrategyForm){
        return vipService.addVipStrategy(vipStrategyForm);
    }


    @GetMapping("/getVipConsumebyMoney/{money}")
    public ResponseVO getVipConsumebyMoney(@PathVariable int money){
        return vipService.getVipConsumebyMoney(money);
    }


    @PostMapping("/addcoupon")
    public ResponseVO addCoupon(@RequestParam List<Integer> idList,@RequestParam int couponId){
        return vipService.addCoupon(idList,couponId);

    }










}
