package com.example.cinema.data.promotion;

import com.example.cinema.po.VIPCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liying on 2019/4/14.
 */
@Mapper
public interface VIPCardMapper {

    int insertOneCard(VIPCard vipCard);

    VIPCard selectCardById(int id);

    void updateCardBalance(@Param("id") int id,@Param("balance") double balance);

    VIPCard selectCardByUserId(int userId);



}
