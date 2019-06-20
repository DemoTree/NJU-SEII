package com.example.cinema.vo;

import com.example.cinema.po.User;

/**
 * @author huwen
 * @date 2019/3/23
 */
public class UserForm {
    private int id;
    /**
     * 用户名，不可重复
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;

    private String role;

    public User getPO(){
        User user = new User();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setRole(this.getRole());
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
