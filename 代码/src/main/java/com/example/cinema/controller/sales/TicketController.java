package com.example.cinema.controller.sales;

import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @PostMapping("/vip/buy")
    public ResponseVO buyTicketByVIPCard(@RequestParam List<Integer> ticketId, @RequestParam int couponId){
        return ticketService.completeByVIPCard(ticketId,couponId);
    }

    @PostMapping("/lockSeat")
    public ResponseVO lockSeat(@RequestBody TicketForm ticketForm){
        return ticketService.addTicket(ticketForm);
    }
    @PostMapping("/buy")
    public ResponseVO buyTicket(@RequestParam List<Integer> ticketId,@RequestParam int couponId){
        return ticketService.completeTicket(ticketId,couponId);
    }
    @GetMapping("/get/{userId}")
    public ResponseVO getTicketByUserId(@PathVariable int userId){
        return ticketService.getTicketByUser(userId);
    }

    @GetMapping("/get/occupiedSeats")
    public ResponseVO getOccupiedSeats(@RequestParam int scheduleId){
        return ticketService.getBySchedule(scheduleId);
    }
    @PostMapping("/cancel")
    public ResponseVO cancelTicket(@RequestParam List<Integer> ticketId){
        return ticketService.cancelTicket(ticketId);
    }





}
