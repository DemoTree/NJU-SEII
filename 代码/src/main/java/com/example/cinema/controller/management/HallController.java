package com.example.cinema.controller.management;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.vo.HallBatchDeleteForm;
import com.example.cinema.vo.HallForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**影厅管理
 * @author fjj,zzy
 * @date 2019/4/12 1:59 PM
 */
@RestController
public class HallController {
    @Autowired
    private HallService hallService;

    @RequestMapping(value = "/hall/all", method = RequestMethod.GET)
    public ResponseVO searchAllHall(){
        return hallService.searchAllHall();
    }

    @RequestMapping(value = "/hall/add", method = RequestMethod.POST)
    public ResponseVO addHall(@RequestBody HallForm hallform){
        return hallService.addHall(hallform);
    }

    @RequestMapping(value = "/hall/update", method = RequestMethod.POST)
    public ResponseVO updateHall(@RequestBody HallForm hallform) {
        return hallService.updateHall(hallform);
    }

    @RequestMapping(value = "/hall/delete/batch", method = RequestMethod.DELETE)
    public ResponseVO deleteHall(@RequestBody HallBatchDeleteForm hallform) {
        return hallService.deleteHall(hallform);
    }


}
