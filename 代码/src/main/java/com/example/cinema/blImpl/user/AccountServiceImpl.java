package com.example.cinema.blImpl.user;

import com.example.cinema.bl.user.AccountService;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.user.AccountMapper;
import com.example.cinema.po.User;
import com.example.cinema.po.VipConsume;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import com.example.cinema.vo.VipConsumeForUserHomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huwen
 * @date 2019/3/23
 */

@Service
public class AccountServiceImpl implements AccountService {
    private static final String NAME_SET_ERROR_MESSAGE = "有账号重名，无法删除或修改";
    private final static String ACCOUNT_EXIST = "账号已存在";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private VIPCardMapper vipCardMapper;

    @Override
    public ResponseVO registerAccount(UserForm userForm) {
        try {
            ResponseVO responseVO = preCheck(userForm);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            accountMapper.createNewAccount(userForm.getPO());
        } catch (Exception e) {
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public UserVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getUsername());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return new UserVO(user);
    }

    /**
     * 搜索管理员账户
     * @author zzy
     * @date 6/10
     */
    @Override
    public ResponseVO searchAllAdmin() {
        try {
            return ResponseVO.buildSuccess(userList2UserVOList(accountMapper.selectAllAdmin()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    private List<UserVO> userList2UserVOList(List<User> userList) {
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            userVOList.add(new UserVO(user));
        }
        return userVOList;
    }

    /**
     * 修改账户信息
     * @author zzy
     * @date 6/3
     */
    @Override
    public ResponseVO updateAdmin(UserForm userForm){
        try {
            ResponseVO responseVO = preCheck(userForm);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            accountMapper.updateAdmin(userForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("修改账户失败");
        }
    }

    /**
     * 删除账户
     * @author zzy
     * @date 6/3
     */
    @Override
    public ResponseVO deleteAdmin(int id){
        try {
            accountMapper.deleteAdmin(id);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("删除账户失败");
        }
    }

    /**
     * 搜索用户账户
     * @author zzy
     * @date 6/10
     */
    @Override
    public ResponseVO searchAllUser() {
        try {
            return ResponseVO.buildSuccess(userList2UserVOList(accountMapper.selectAllUser()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    /**
     * 修改账户信息
     * @author zzy
     * @date 6/3
     */
    @Override
    public ResponseVO updateUser(UserForm userForm){
        try {
            ResponseVO responseVO = preCheck(userForm);
            if(!responseVO.getSuccess()){
                return responseVO;
            }
            accountMapper.updateUser(userForm);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("修改账户失败");
        }
    }

    /**
     * 删除账户
     * @author zzy
     * @date 6/3
     */
    @Override
    public ResponseVO deleteUser(int id){
        try {
            accountMapper.deleteUser(id);
            return ResponseVO.buildSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("删除账户失败");
        }
    }


    @Override
    public ResponseVO getNamebyId(int id){
        try {
            return ResponseVO.buildSuccess(accountMapper.getNamebyId(id));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("删除账户失败");
        }
    }

    /**
     * 新增或修改影厅信息的公共前置检查，判断是否重名
     * @author zzy
     * @date 6/3
     */
    ResponseVO preCheck(UserForm userForm){
        try {
            //检查是否重名
            if (accountMapper.checkAccountName(userForm.getUsername(), userForm.getId()) == 1){
                return ResponseVO.buildFailure(NAME_SET_ERROR_MESSAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO getVipConsumeforUseHome(){
        try {
            List<VipConsume>vipConsumes= vipCardMapper.selectVipConsumebyMoney(0);
            VipConsumeForUserHomeVO vo=new VipConsumeForUserHomeVO();
            List<VipConsumeForUserHomeVO> consumes=new ArrayList<>();
            //获取消费记录并且添加上用户名后返回
            for (int i=0;i<vipConsumes.size()&&i<10;i++){
                vo.setName(accountMapper.getNamebyId(vipConsumes.get(i).getUserId()).getUsername());
                vo.setConsume(vipConsumes.get(i).getConsume());
                vo.setUserId(vipConsumes.get(i).getUserId());
                vo.setVipId(vipConsumes.get(i).getVipId());
                consumes.add(vo);
            }
            return ResponseVO.buildSuccess(consumes);


        }catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("删除账户失败");
        }
    }






}



