package com.example.cinema.data.sales;

import com.example.cinema.po.Refund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefundMapper {
    void insertRefund(Refund refund);

    void updateRefund(@Param("refundId") int id, @Param("time") int time, @Param("persent") double persent);

    Refund getRefund(@Param("refundId") int id);
}
