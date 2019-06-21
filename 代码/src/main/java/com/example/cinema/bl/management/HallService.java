package com.example.cinema.bl.management;

import com.example.cinema.vo.HallBatchDeleteForm;
import com.example.cinema.vo.HallForm;
import com.example.cinema.vo.ResponseVO;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
public interface HallService {
    /**
     * 搜索所有影厅
     * @return
     */
    ResponseVO searchAllHall();

    /**
     * 添加影厅信息
     * @param hallform
     * @author zzy
     * @date 5/31
     */
    ResponseVO addHall(HallForm hallform);

    /**
     * 修改影厅信息
     * @param hallform
     * @author zzy
     * @date 5/31
     */
    ResponseVO updateHall(HallForm hallform);

    /**
     * 删除影厅
     * @param hallform
     * @author zzy
     * @date 6/3
     */
    ResponseVO deleteHall(HallBatchDeleteForm hallform);


}
