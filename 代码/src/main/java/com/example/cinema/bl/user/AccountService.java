package com.example.cinema.bl.user;

import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;

/**
 * @author huwen
 * @date 2019/3/23
 */
public interface AccountService {

    /**
     * 注册账号
     * @return
     */
    ResponseVO registerAccount(UserForm userForm);

    /**
     * 用户登录，登录成功会将用户信息保存再session中
     * @return
     */
    UserVO login(UserForm userForm);

    /**
     * 搜索管理员账户
     * @author zzy
     * @date 6/10
     */
    ResponseVO searchAllAdmin();

    /**
     * 修改账户信息
     * @param userForm
     * @author zzy
     * @date 5/31
     */
    ResponseVO updateAdmin(UserForm userForm);

    /**
     * 删除账户
     * @author zzy
     * @date 6/3
     */
    ResponseVO deleteAdmin(int id);

    /**
     * 搜索管理员账户
     * @author zzy
     * @date 6/10
     */
    ResponseVO searchAllUser();

    /**
     * 修改账户信息
     * @param userForm
     * @author zzy
     * @date 5/31
     */
    ResponseVO updateUser(UserForm userForm);

    /**
     * 删除账户
     * @author zzy
     * @date 6/3
     */
    ResponseVO deleteUser(int id);

    ResponseVO getNamebyId(int id);

    ResponseVO getVipConsumeforUseHome();//首页获取冲销记录

}
