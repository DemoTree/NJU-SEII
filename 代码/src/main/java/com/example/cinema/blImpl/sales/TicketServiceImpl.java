package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.promotion.ActivityMapper;
import com.example.cinema.data.promotion.CouponMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
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

    @Override
    @Transactional
    public ResponseVO addTicket(TicketForm ticketForm) {
        try {
            int userId=ticketForm.getUserId();
            int scheduleId=ticketForm.getScheduleId();
            List<SeatForm> seats=ticketForm.getSeats();
            Ticket ETicket=new Ticket();
            int id=ticketMapper.insertTicket(ETicket);//获取当前票的ID
            ticketMapper.deleteTicket(id);
            ArrayList<Ticket> tickets=new ArrayList<>();
            Timestamp time=new Timestamp(System.currentTimeMillis());
            for(int i=0;i<seats.size();i++){
                Ticket ticket=new Ticket();
                ticket.setId(id+i);
                ticket.setScheduleId(scheduleId);
                ticket.setUserId(userId);
                ticket.setState(0);
                ticket.setColumnIndex(seats.get(i).getColumnIndex());
                ticket.setRowIndex(seats.get(i).getRowIndex());
                ticket.setTime(time);
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
                }
            }
            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=couponMapper.insertCoupon(activities1.get(j).getCoupon());
                couponMapper.insertCouponUser(CouponId,userId);
            }
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=couponMapper.insertCoupon(activities2.get(j).getCoupon());
                    couponMapper.insertCouponUser(CouponId,userId);
                }
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
                }
            }
            List<Activity> activities1=activityMapper.selectActivitiesByMovie(movieId);
            for(int j=0;j<activities1.size();j++){
                int CouponId=couponMapper.insertCoupon(activities1.get(j).getCoupon());
                couponMapper.insertCouponUser(CouponId,userId);
            }
            List<Activity> activities2=activityMapper.selectActivitiesWithoutMovie();
            for(int j=0;j<activities2.size();j++){
                if(activities2.get(j).getStartTime().before(date) && date.before(activities2.get(j).getEndTime())){
                    int CouponId=couponMapper.insertCoupon(activities2.get(j).getCoupon());
                    couponMapper.insertCouponUser(CouponId,userId);
                }
            }
            VIPCard vipCard=vipCardMapper.selectCardByUserId(userId);
            double balance=vipCard.getBalance();
            balance=balance-totalPrice;
            vipCard.setBalance(balance);
            vipCardMapper.updateCardBalance(vipCard.getId(),balance);
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



}
