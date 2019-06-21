package com.example.cinema.blImpl.sales;

import com.example.cinema.CinemaApplicationTest;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.ConsumeHistory;
import com.example.cinema.po.Ticket;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.ScheduleWithSeatVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TicketServiceImplTest extends CinemaApplicationTest {
    @Autowired
    TicketServiceImpl ticketService;


    @Test
    public void getTicketByUser() {
        List<Ticket> list = (ArrayList<Ticket>) ticketService.getTicketByUser(103).getContent();
        for (int i = 0; i < list.size(); i++)
            assertEquals(103, list.get(i).getUserId());
    }


    @Test
    public void getConsumeHistorybyUser() {
        List<ConsumeHistory> list = (ArrayList<ConsumeHistory>) ticketService.getConsumeHistorybyUser(103).getContent();
        for (int i = 0; i < list.size(); i++)
            assertEquals(103, list.get(i).getUserId());
    }


    @Test
    public void getBySchedule() {
        ScheduleWithSeatVO scheduleWithSeatVO=(ScheduleWithSeatVO)ticketService.getBySchedule(69).getContent();
        assertEquals((Integer) 69,scheduleWithSeatVO.getScheduleItem().getId() );

    }

}