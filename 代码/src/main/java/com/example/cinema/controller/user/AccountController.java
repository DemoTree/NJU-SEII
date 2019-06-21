package com.example.cinema.controller.user;

import com.example.cinema.blImpl.user.AccountServiceImpl;
import com.example.cinema.config.InterceptorConfiguration;
import com.example.cinema.vo.UserForm;
import com.example.cinema.vo.ResponseVO;
import com.example.cinema.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author huwen
 * @date 2019/3/23
 */
@RestController
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR="用户名或密码错误";
    @Autowired
    private AccountServiceImpl accountService;
    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserForm userForm, HttpSession session){
        UserVO user = accountService.login(userForm);
        if(user==null){
            return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        //注册session
        session.setAttribute(InterceptorConfiguration.SESSION_KEY,userForm);
        return ResponseVO.buildSuccess(user);
    }
    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserForm userForm){
        return accountService.registerAccount(userForm);
    }




    @GetMapping("/getvipconsumeforuserhome")
    public ResponseVO getVipConsumeForUserHome(){
        return accountService.getVipConsumeforUseHome();
    }





    @PostMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute(InterceptorConfiguration.SESSION_KEY);
        return "index";
    }

    @RequestMapping(value = "/manager/admin/all", method = RequestMethod.GET)
    public ResponseVO searchAllAdmin() {
        return accountService.searchAllAdmin();
    }

    @RequestMapping(value = "/manager/admin/update", method = RequestMethod.POST)
    public ResponseVO updateAdmin(@RequestBody UserForm userForm) {
        return accountService.updateAdmin(userForm);
    }

    @RequestMapping(value = "/manager/admin/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVO deleteAdmin(@RequestBody @PathVariable(value = "id") int id) {
        return accountService.deleteAdmin(id);
    }

    @RequestMapping(value = "/manager/user/all", method = RequestMethod.GET)
    public ResponseVO searchAllUser() {
        return accountService.searchAllUser();
    }

    @RequestMapping(value = "/manager/user/update", method = RequestMethod.POST)
    public ResponseVO updateUser(@RequestBody UserForm userForm) {
        return accountService.updateUser(userForm);
    }

    @RequestMapping(value = "/manager/user/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVO deleteUser(@RequestBody @PathVariable(value = "id") int id) {
        return accountService.deleteUser(id);
    }

}
