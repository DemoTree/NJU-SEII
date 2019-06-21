package com.example.cinema.data.user;

import com.example.cinema.po.User;
import com.example.cinema.vo.UserForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huwen
 * @date 2019/3/23
 */
@Mapper
public interface AccountMapper {

    /**
     * 创建一个新的账号
     * @return
     */
    public int createNewAccount(User user);

    /**
     * 根据用户名查找账号
     * @param username
     * @return
     */
    public User getAccountByName(@Param("username") String username);

    /**
     * 更新账户信息
     * @author zzy
     * @date 6/4
     */
    int updateAdmin(UserForm userForm);

    /**
     * 根据ID删除账户信息
     * @author zzy
     * @date 6/4
     */
    int deleteAdmin(@Param("id") int id);

    /**
     * 获取所有管理员账户
     * @author zzy
     * @date 6/10
     */
    List<User> selectAllAdmin();

    /**
     * 获取所有用户账户
     * @author zzy
     * @date 6/10
     */
    List<User> selectAllUser();

    /**
     * 更新账户信息
     * @author zzy
     * @date 6/4
     */
    int updateUser(UserForm userForm);

    /**
     * 根据ID删除账户信息
     * @author zzy
     * @date 6/4
     */
    int deleteUser(@Param("id") int id);

    /**
     * 检查重名账户
     * @author zzy
     * @date 6/3
     */
    int checkAccountName(String username, int id);

    User getNamebyId(int id);

}
