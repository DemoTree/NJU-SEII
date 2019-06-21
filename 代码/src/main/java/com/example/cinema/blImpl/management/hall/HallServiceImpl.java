package com.example.cinema.blImpl.management.hall;

import com.example.cinema.bl.management.HallService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.po.Hall;
import com.example.cinema.vo.HallBatchDeleteForm;
import com.example.cinema.vo.HallForm;
import com.example.cinema.vo.HallVO;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fjj
 * @date 2019/4/12 2:01 PM
 */
@Service
public class HallServiceImpl implements HallService, HallServiceForBl {
    private static final String VIEW_CONFLICT_ERROR_MESSAGE = "有排片信息已对观众可见，无法删除或修改";
    private static final String NAME_SET_ERROR_MESSAGE = "有影厅重名，无法删除或修改";

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private ScheduleServiceForBl scheduleServiceForBl;

    @Override
    public ResponseVO searchAllHall() {
        try {
            return ResponseVO.buildSuccess(hallList2HallVOList(hallMapper.selectAllHall()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("查找影厅失败");
        }
    }

    @Override
    public Hall getHallById(int id) {
        try {
            return hallMapper.selectHallById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

//    private List<HallForm> hallList2HallVOList(List<Hall> hallList){
//        List<HallForm> hallVOList = new ArrayList<>();
//        for(Hall hall : hallList){
//            hallVOList.add(new HallForm(hall));
//        }
//        return hallVOList;
//    }

    private List<HallVO> hallList2HallVOList(List<Hall> hallList){
        List<HallVO> hallVOList = new ArrayList<>();
        for(Hall hall : hallList){
            hallVOList.add(new HallVO(hall));
        }
        return hallVOList;
    }

//    /**
//     * type转换
//     * @param hallList
//     * @return
//     */
//    private Integer string2Int(HallForm hallform){
//        Integer typeInteger=0;
//        switch(){
//            case "2D厅":
//                typeInteger=1;
//                break;
//            case "3D厅":
//                typeInteger=2;
//                break;
//            case "IMAX厅":
//                typeInteger=3;
//                break;
//            default:
//                typeInteger=0;
//        }
//        return typeInteger;
//    }

    /**
     * 添加影厅信息
     * @author zzy
     * @date 5/31
     */
    public ResponseVO addHall(HallForm hallform){
        try {
            hallMapper.insertOneHall(hallform);
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("添加影厅失败");
        }
    }

    /**
     * 修改影厅信息
     * @author zzy
     * @date 6/3
     */
    public ResponseVO updateHall(HallForm hallform){
        try {
            ResponseVO responseVO = preCheck(hallform);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            //检查影厅是否有排片
            if(isAudienceCanView(hallform.getId())){
                return ResponseVO.buildFailure(VIEW_CONFLICT_ERROR_MESSAGE);
            }
            hallMapper.updateHall(hallform);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("修改影厅失败");
        }
    }

    /**
     * 删除影厅
     * @author zzy
     * @date 6/3
     */
    public ResponseVO deleteHall(HallBatchDeleteForm hallform){
        try {
            System.out.println(hallform.getHallIdList().get(0));
//            List<Integer> hallIdList = hallform.getScheduleIdList();
                if(isAudienceCanView(hallform.getHallIdList().get(0))){
                    return ResponseVO.buildFailure(VIEW_CONFLICT_ERROR_MESSAGE);
                }
            hallMapper.deleteHall(hallform.getHallIdList().get(0));
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("删除影厅失败");
        }
    }

    /**
     * 删除影厅的公共前置检查
     * @author zzy
     * @date 6/3
     */
    boolean isAudienceCanView(int hallId){
        //检查影厅是否在使用
        int flag = 0;
        List<Integer> busyHalls = scheduleServiceForBl.getBusyHalls();
        for(int i=0; i<busyHalls.size(); i++){
            if(hallId==busyHalls.get(i)){
                flag=1;
                break;
            }
        }
        //flag==1 影厅在使用，返回true
        //flag==0 影厅不在使用，返回false
        return flag==1;
    }

    /**
     * 新增或修改影厅信息的公共前置检查
     * @author zzy
     * @date 6/3
     */
    ResponseVO preCheck(HallForm hallform){
        try {
            //检查是否重名
            System.out.println(hallform.getId());
            System.out.println(hallform.getName());
            if (hallMapper.checkHallName(hallform.getName(), hallform.getId()) == 1){
                return ResponseVO.buildFailure(NAME_SET_ERROR_MESSAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseVO.buildSuccess();
    }
}
