package com.example.cinema.data.sales;

import com.example.cinema.po.ConsumeHistory;
import com.example.cinema.po.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Mapper
public interface TicketMapper {
    int insertConsumeHistory(ConsumeHistory consumeHistory);
    int insertConsumeHistorys(List<ConsumeHistory> consumeHistorys);
    void deleteConsumeHistory(int id);
    List<ConsumeHistory> selectConsumeHistorysByUser(int userId);

    int insertTicket(Ticket ticket);

    int insertTickets(List<Ticket> tickets);

    void deleteTicket(int ticketId);

    void updateTicketState(@Param("ticketId") int ticketId, @Param("state") int state);

    void updateTicketPrice(@Param("ticketId") int ticketId,@Param("price") double price);

    void updateTicketBuyway(@Param("ticketId") int ticketId,@Param("ifUseVIP") boolean ifUseVIP);

    void updateTicketOut(@Param("ticketId") int ticketId,@Param("isOut") boolean isOut);

    List<Ticket> selectTicketsBySchedule(int scheduleId);

    Ticket selectTicketByScheduleIdAndSeat(@Param("scheduleId") int scheduleId, @Param("column") int columnIndex, @Param("row") int rowIndex);

    Ticket selectTicketById(int id);

    List<Ticket> selectTicketByUser(int userId);

    @Scheduled(cron = "0/1 * * * * ?")
    void cleanExpiredTicket();
}

