package com.zz.controller;


import com.zz.entity.ResponseBo;
import com.zz.service.UserService;
import com.zz.utils.KeyUtils;
import com.zz.utils.Md5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.zz.entity.User;
import com.zz.utils.MD5Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Resource
    UserService us;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseBo login(String username, String password, Boolean rememberMe) {
        password = MD5Utils.encrypt(username, password);
//        password= Md5Util.StringInMd5(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return ResponseBo.ok();
        } catch (UnknownAccountException e) {
            return ResponseBo.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return ResponseBo.error(e.getMessage());
        } catch (LockedAccountException e) {
            return ResponseBo.error(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseBo.error("认证失败！");
        }
    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user);
        return "index.html";
    }

//    @GetMapping("/register")
//    public String register(){
//        return "register.html";
//    }
//    @PostMapping("/register")
//    @ResponseBody
//    public ResponseBo register(User user){
//        User user1 = new User();
//        Md5Util md5 = new Md5Util();
//        user1.setId(md5.StringInMd5(KeyUtils.genUniqueKey()));
//        user1.setUsername(user.getUsername());
//        user1.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
//        user1.setEmail(user.getEmail());
//        user1.setHeadimgid(user.getHeadimgid());
//        user1.setBeinvitedcode(user.getBeinvitedcode());
//        user1.setInvitecode(md5.StringInMd5(KeyUtils.genUniqueKey() + user.getUsername()));
//        us.save(user1);
//        return ResponseBo.ok();
//    }

    @PostMapping("/getUser")
    @ResponseBody
    private Map getUser(){
        Map map = new HashMap();
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        map.put("username",user.getUsername());
//        map.put("user")
        return map;
    }

}