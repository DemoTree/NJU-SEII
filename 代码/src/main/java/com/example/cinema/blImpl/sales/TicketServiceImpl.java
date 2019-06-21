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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 * 6.5
 * 新增getconsumehistorybyueser方法，改动两个complete方法
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
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            double price=schedule.getFare();
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
            }
            ticketMapper.insertTickets(tickets);
            List<Ticket> result=tickets;
            return ResponseVO.buildSuccess(result);
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
            double price=schedule.getFare();
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                totalPrice=totalPrice+price;
                ticketMapper.updateTicketState(id.get(i),1);
                ticket.setState(1);
                movieId=schedule.getMovieId();
            }
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
            }
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                ticket.setPrice(totalPrice/id.size());
                ticketMapper.updateTicketPrice(id.get(i),totalPrice/id.size());
                ticket.setIfUseVIP(false);
                ticketMapper.updateTicketBuyway(id.get(i),false);
            }
            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=activities1.get(j).getCoupon().getId();
                couponMapper.insertCouponUser(CouponId,userId);
            }
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=activities2.get(j).getCoupon().getId();
                    couponMapper.insertCouponUser(CouponId,userId);
                }
            }


            List<ConsumeHistory> consumeHistories=new ArrayList<>();
            ConsumeHistory currentConsumeHistory=null;
            currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(userId);
            currentConsumeHistory.setAmountOfMoney(totalPrice);
            currentConsumeHistory.setConsumeType(1);
            currentConsumeHistory.setConsumeWay(1);
            currentConsumeHistory.setConsumeCardId(123123123);
            consumeHistories.add(currentConsumeHistory);
            ticketMapper.insertConsumeHistorys(consumeHistories);//插入新的消费记录

            VIPCard card=new VIPCard();
            card=vipCardMapper.selectCardByUserId(userId);
            if (card!=null){
                vipCardMapper.updateVipConsume(vipCardMapper.selectVipConsumebyVip(card.getId()).get(0).getConsume()+totalPrice,card.getId());

            }

            AudiencePrice audiencePrice=new AudiencePrice();
            audiencePrice.setTotalPrice(totalPrice);
            audiencePrice.setUserId(userId);
            AudiencePriceVO audiencePriceVO=new AudiencePriceVO();
            audiencePriceVO.setDate(date);
            audiencePriceVO.setPrice(audiencePrice.getTotalPrice());



            return ResponseVO.buildSuccess(audiencePriceVO);
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
            });
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
            double price=schedule.getFare();
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                totalPrice=totalPrice+price;
                ticketMapper.updateTicketState(id.get(i),1);
                ticket.setState(1);
                movieId=schedule.getMovieId();
            }
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
            }
            for(int i=0;i<id.size();i++){
                Ticket ticket=ticketMapper.selectTicketById(id.get(i));
                ticket.setPrice(totalPrice/id.size());
                ticketMapper.updateTicketPrice(id.get(i),totalPrice/id.size());
                ticket.setIfUseVIP(true);
                ticketMapper.updateTicketBuyway(id.get(i),true);
            }
            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=activities1.get(j).getCoupon().getId();
                couponMapper.insertCouponUser(CouponId,userId);
            }
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=activities2.get(j).getCoupon().getId();
                    couponMapper.insertCouponUser(CouponId,userId);
                }
            }
            VIPCard vipCard=vipCardMapper.selectCardByUserId(userId);
            double balance=vipCard.getBalance();
            balance=balance-totalPrice;
            vipCard.setBalance(balance);
            vipCardMapper.updateCardBalance(vipCard.getId(),balance);
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

            VIPCard card=vipCardMapper.selectCardByUserId(userId);
            vipCardMapper.updateVipConsume(vipCardMapper.selectVipConsumebyVip(card.getId()).get(0).getConsume()+totalPrice,card.getId());

            AudiencePrice audiencePrice=new AudiencePrice();
            audiencePrice.setTotalPrice(totalPrice);
            audiencePrice.setUserId(userId);
            AudiencePriceVO audiencePriceVO=new AudiencePriceVO();
            audiencePriceVO.setDate(date);
            audiencePriceVO.setPrice(audiencePrice.getTotalPrice());
            return ResponseVO.buildSuccess(audiencePriceVO);
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
			reprice=ticket.getPrice()*refund.getPersent();



            ConsumeHistory currentConsumeHistory=null;
            currentConsumeHistory=new ConsumeHistory();
            currentConsumeHistory.setUserId(ticket.getUserId());
            currentConsumeHistory.setAmountOfMoney(reprice);
            currentConsumeHistory.setConsumeType(2);



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



}
