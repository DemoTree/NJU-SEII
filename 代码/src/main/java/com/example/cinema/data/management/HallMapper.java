package com.example.cinema.data.management;

import com.example.cinema.po.Hall;
import com.example.cinema.vo.HallForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fjj
 * @date 2019/4/11 3:46 PM
 */
@Mapper
public interface HallMapper {
    /**
     * 查询所有影厅信息
     * @return
     */
    List<Hall> selectAllHall();

    /**
     * 根据id查询影厅
     * @return
     */
    Hall selectHallById(@Param("hallId") int hallId);

    /**
     * 插入一条影厅信息
     * @author zzy
     * @date 5/31
     */
    int insertOneHall(HallForm hallform);

    /**
     * 更新影厅信息
     * @author zzy
     * @date 5/31
     */
    void updateHall(HallForm hallform);

    /**
     * 根据ID删除影厅信息
     * @author zzy
     * @date 5/31
     */
    int deleteHall(HallForm hallform);

    /**
     * 检查重名影厅
     * @author zzy
     * @date 6/3
     */
    int checkHallName(String hallName, int hallId);
}
