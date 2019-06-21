package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.RefundMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.rmi.CORBA.Stub;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 * 6.5
 * 新增getconsumehistorybyueser方法，改动两个complete方法
 */
/**
 * @author cmw
 * @date 5/24
 */

/**
 * @author zzy
 * @date 6/2
 */

/**
 * @author lxd
 * @date 6/10
 */

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    VIPCardMapper vipCardMapper;
    @Autowired
    RefundMapper refundMapper;

    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        try {
            int userId=ticketForm.getUserId();
            int scheduleId=ticketForm.getScheduleId();
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);//排片信息
            double price=schedule.getFare();//票价
            List<SeatForm> seats=ticketForm.getSeats();
            ArrayList<Ticket> tickets=new ArrayList<>();
            Timestamp time=new Timestamp(System.currentTimeMillis());
            for(int i=0;i<seats.size();i++){
                Ticket ticket=new Ticket();
                ticket.setScheduleId(scheduleId);
                ticket.setUserId(userId);
                ticket.setState(0);
                ticket.setColumnIndex(seats.get(i).getColumnIndex());
                ticket.setRowIndex(seats.get(i).getRowIndex());
                ticket.setTime(time);
                ticket.setPrice(price);
                ticket.setIfUseVIP(false);
                ticket.setIsOut(false);
                tickets.add(ticket);
            }//设置票的基本信息
            ticketMapper.insertTickets(tickets);
            //返回VO
            List<TicketVO> voList=new ArrayList<>();
            for (int i=0;i<tickets.size();i++){
                voList.add(tickets.get(i).getVO());
            }
            return ResponseVO.buildSuccess(voList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    @Transactional
    public ResponseVO completeTicket(List<Integer> id, int couponId) {
        try {
            double totalPrice=0;
            Ticket Eticket=ticketMapper.selectTicketById(id.get(0));
            Timestamp date=Eticket.getTime();
            int userId=Eticket.getUserId();
            int movieId=0;
            int scheduleId=Eticket.getScheduleId();
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            double price=schedule.getFare();//票价
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                totalPrice=totalPrice+price;
                ticketMapper.updateTicketState(id.get(i),1);
                ticket.setState(1);
                movieId=schedule.getMovieId();
            }//计算总价并修改票的状态
            if(couponId==0){
                totalPrice=totalPrice;
            }
            else{
                Coupon coupon=couponMapper.selectById(couponId);
                Timestamp start=coupon.getStartTime();
                Timestamp end=coupon.getEndTime();
                if(start.before(date)&&date.before(end)&&totalPrice>=coupon.getTargetAmount()) {
                    totalPrice = totalPrice - coupon.getDiscountAmount();
                    couponMapper.deleteCouponUser(couponId,userId);
                }
            }//校验优惠券
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                ticket.setPrice(totalPrice/id.size());
                ticketMapper.updateTicketPrice(id.get(i),totalPrice/id.size());
                ticket.setIfUseVIP(false);
                ticketMapper.updateTicketBuyway(id.get(i),false);
            }//计算退票时使用优惠券的实际支付
            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=activities1.get(j).getCoupon().getId();
                couponMapper.insertCouponUser(CouponId,userId);
            }//根据电影校验活动
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=activities2.get(j).getCoupon().getId();
                    couponMapper.insertCouponUser(CouponId,userId);
                }
            }//根据时间校验活动


            //插入新的消费记录
            List<ConsumeHistory> consumeHistories=new ArrayList<>();
            ConsumeHistory currentConsumeHistory=null;
            currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(userId);
            currentConsumeHistory.setAmountOfMoney(totalPrice);
            currentConsumeHistory.setConsumeType(1);
            currentConsumeHistory.setConsumeWay(1);
            currentConsumeHistory.setConsumeCardId(123123123);
            consumeHistories.add(currentConsumeHistory);
            ticketMapper.insertConsumeHistorys(consumeHistories);

            //更新会员消费总额
            VIPCard card=new VIPCard();
            card=vipCardMapper.selectCardByUserId(userId);
            if (card!=null){
                vipCardMapper.updateVipConsume(vipCardMapper.selectVipConsumebyVip(card.getId()).get(0).getConsume()+totalPrice,card.getId());
            }

            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });//标记已锁座位
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        try {
            List<Ticket> allTickets = ticketMapper.selectTicketByUser(userId);
            ArrayList<Ticket> tickets = new ArrayList<Ticket>();
            for(int i=0;i<allTickets.size();i++){
                tickets.add(allTickets.get(i));
            }
            List<Ticket> ticketsBought=tickets;
            return ResponseVO.buildSuccess(ticketsBought);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    @Override
    @Transactional
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
        try {
            double totalPrice=0;
            Ticket Eticket=ticketMapper.selectTicketById(id.get(0));
            Timestamp date=Eticket.getTime();
            int userId=Eticket.getUserId();
            int movieId=0;
            int scheduleId=Eticket.getScheduleId();
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            double price=schedule.getFare();//电影票价
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                totalPrice=totalPrice+price;
                ticketMapper.updateTicketState(id.get(i),1);
                ticket.setState(1);
                movieId=schedule.getMovieId();
            }//获取总价并更新票的状态
            if(couponId==0){
                totalPrice=totalPrice;
            }
            else{
                Coupon coupon=couponMapper.selectById(couponId);
                Timestamp start=coupon.getStartTime();
                Timestamp end=coupon.getEndTime();
                if(start.before(date)&&date.before(end)&&totalPrice>=coupon.getTargetAmount()) {
                    totalPrice = totalPrice - coupon.getDiscountAmount();
                    couponMapper.deleteCouponUser(couponId,userId);
                }
            }//校验优惠券

            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                ticket.setPrice(totalPrice/id.size());
                ticketMapper.updateTicketPrice(id.get(i),totalPrice/id.size());
                ticket.setIfUseVIP(true);
                ticketMapper.updateTicketBuyway(id.get(i),true);
            }//是否会员卡支付


            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=activities1.get(j).getCoupon().getId();
                couponMapper.insertCouponUser(CouponId,userId);
            }//根据电影校验优惠活动
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=activities2.get(j).getCoupon().getId();
                    couponMapper.insertCouponUser(CouponId,userId);
                }
            }//根据时间校验优惠活动

            //会员卡支付
            VIPCard vipCard=vipCardMapper.selectCardByUserId(userId);
            double balance=vipCard.getBalance();
            balance=balance-totalPrice;
            vipCard.setBalance(balance);
            vipCardMapper.updateCardBalance(vipCard.getId(),balance);

            //插入消费记录
            List<ConsumeHistory> consumeHistories=new ArrayList<>();
            ConsumeHistory currentConsumeHistory=null;
            currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(userId);
            currentConsumeHistory.setAmountOfMoney(totalPrice);
            currentConsumeHistory.setConsumeType(1);
            currentConsumeHistory.setConsumeWay(0);
            currentConsumeHistory.setConsumeCardId(vipCard.getId());
            consumeHistories.add(currentConsumeHistory);
            ticketMapper.insertConsumeHistorys(consumeHistories);

            //更新会员消费总额
            VIPCard card=vipCardMapper.selectCardByUserId(userId);
            vipCardMapper.updateVipConsume(vipCardMapper.selectVipConsumebyVip(card.getId()).get(0).getConsume()+totalPrice,card.getId());

            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
        try {
            for(int i=0;i<id.size();i++){
                ticketMapper.updateTicketState(id.get(i),2);
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }
    @Override
    public ResponseVO getConsumeHistorybyUser(int userId){

        try {
            return ResponseVO.buildSuccess(ticketMapper.selectConsumeHistorysByUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

	@Override
	@Transactional
	public ResponseVO outTicket(int ticketId){
		try{
			Ticket ticket=ticketMapper.selectTicketById(ticketId);
			ticket.setIsOut(true);
			ticketMapper.updateTicketOut(ticketId,true);
			return ResponseVO.buildSuccess();
		} catch (Exception e){
			e.printStackTrace();
			return ResponseVO.buildFailure("失败");
		}
	}

	@Override
	@Transactional
	public ResponseVO refundTicket(int ticketId){
		try{
			Ticket ticket=ticketMapper.selectTicketById(ticketId);
			Refund refund=refundMapper.getRefund(1);
			double reprice=0;
			reprice=ticket.getPrice()*refund.getPersent();//计算退票金额
            //插入退票记录
            ConsumeHistory currentConsumeHistory=null;
            currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(ticket.getUserId());
            currentConsumeHistory.setAmountOfMoney(reprice);
            currentConsumeHistory.setConsumeType(2);
            //会员卡支付返回退票金额给会员卡余额
			if(ticket.getIfUseVIP()){
                currentConsumeHistory.setConsumeWay(0);
				int userId=ticket.getUserId();
				VIPCard vipCard=vipCardMapper.selectCardByUserId(userId);
				double balance=vipCard.getBalance();
				vipCard.setBalance(balance+reprice);
				vipCardMapper.updateCardBalance(vipCard.getId(),(balance+reprice));
                currentConsumeHistory.setConsumeCardId(vipCard.getId());
			}else {
                currentConsumeHistory.setConsumeWay(1);
                currentConsumeHistory.setConsumeCardId(123123123);

            }
			ticket.setState(3);
			ticketMapper.updateTicketState(ticketId,3);
            ticketMapper.insertConsumeHistory(currentConsumeHistory);//插入新的消费记录
			return ResponseVO.buildSuccess();
		} catch (Exception e){
			e.printStackTrace();
			return ResponseVO.buildFailure("失败");
		}
	}

	//模拟获取一张票
	public Ticket Stub1(){
        System.out.println("return a ticket");
        Ticket ticket=new Ticket();
        ticket.setState(0);
        ticket.setIfUseVIP(true);
        ticket.setPrice(15);
        ticket.setIsOut(false);
        ticket.setColumnIndex(2);
        ticket.setRowIndex(2);
        ticket.setScheduleId(69);
        return ticket;
    }













    //模拟获取票的列表
    public List<Ticket> Stub2(){
        System.out.println("return TicketList");
        List<Ticket> ticketList=new ArrayList<>();
        Ticket ticket1=new Ticket();
        ticket1.setState(0);
        ticket1.setIfUseVIP(true);
        ticket1.setPrice(15);
        ticket1.setIsOut(false);
        ticket1.setColumnIndex(2);
        ticket1.setRowIndex(2);
        ticket1.setScheduleId(69);
        ticketList.add(ticket1);

        Ticket ticket2=new Ticket();
        ticket2.setState(0);
        ticket2.setIfUseVIP(true);
        ticket2.setPrice(16);
        ticket2.setIsOut(false);
        ticket2.setColumnIndex(2);
        ticket2.setRowIndex(3);
        ticket2.setScheduleId(69);
        ticketList.add(ticket2);

        return ticketList;


    }

















    //模拟获取历史消费记录
    public List<ConsumeHistory> Stub3(){
        System.out.println("return a ConsumeHistory");
        ConsumeHistory consumeHistory=new ConsumeHistory();
        consumeHistory.setId(99);
        consumeHistory.setAmountOfMoney(10);
        consumeHistory.setConsumeTime(new Timestamp(1111111));
        consumeHistory.setConsumeType(0);
        consumeHistory.setConsumeCardId(123123123);
        consumeHistory.setConsumeWay(0);
        consumeHistory.setUserId(13);
        List<ConsumeHistory> consumeHistoryList=new ArrayList<>();
        consumeHistoryList.add(consumeHistory);

        return consumeHistoryList;

    }


    //模拟会员卡
    public VIPCard Stub4(){
        System.out.println("return a vipcard");
        VIPCard card=new VIPCard();
        card.setId(8);
        card.setUserId(15);
        card.setBalance(100.00);
        card.setViptype(0);
        card.setJoinDate(new Timestamp(11231));

        return card;
    }


    //模拟退票策略
    public Refund Stub5(){
        System.out.println("return a Refund");
        Refund refund=new Refund();
        refund.setId(1);
        refund.setPersent(0.3);
        refund.setTime(30);

        return refund;
    }

    public ScheduleItem Stub6(){
        ScheduleItem scheduleItem=new ScheduleItem();
        scheduleItem.setEndTime(new Date());
        scheduleItem.setFare(15);
        scheduleItem.setHallId(2);
        scheduleItem.setHallName("激光厅");
        scheduleItem.setId(1);
        scheduleItem.setMovieId(12);
        scheduleItem.setMovieName("测试");

        return scheduleItem;

    }









}
