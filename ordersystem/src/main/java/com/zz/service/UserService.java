package com.zz.service;


import com.zz.entity.User;
import com.zz.entity.UserCheap;
import com.zz.repository.DTODao;
import com.zz.repository.UserCheapRepository;
import com.zz.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.rmi.CORBA.Util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {
    @Resource
    UserRepository ur;
    @Resource
    DTODao dd;
    @Resource
    UserCheapRepository ucr;

    public User save(User user) {
        return ur.save(user);
    }

    public List<User> findByUserName(String name) {
        return ur.findByUsername(name);
    }

    public List<User> findByEmail(String email) {
        return ur.findByEmail(email);
    }

    public User findByUserNameAndPassword(String name, String password) {
        return ur.findByUsernameAndPassword(name, password);
    }

    /**
     * 添加cookie
     *
     * @param loginUsername
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     */
    public void addCookie(String loginUsername, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        if (!loginUsername.equals("")) {
            //创建  Cookie
            Cookie loginUsernameCookie = new Cookie("loginUsername", loginUsername);
            //设置Cookie的父路经
            loginUsernameCookie.setPath("/");
            //获取是否保存Cookie（例如：复选框的值）
//            String rememberMe = request.getParameter("rememberMe");
//            if( rememberMe==null || rememberMe.equals(false) ){
//                //不保存Cookie
//                loginUsernameCookie.setMaxAge(0);
//                System.out.println(rememberMe.equals("false"));
//            }else{
            //保存Cookie的时间长度，单位为秒
            loginUsernameCookie.setMaxAge(7 * 24 * 60 * 60);
//            }
            //加入Cookie到响应头
            response.addCookie(loginUsernameCookie);
        }
    }

    public List<Map<String, Object>> findusercheap(String userid) {
        return dd.findusercheap(userid);
    }

    public List<Map<String, Object>> findinviterbyid(String userid) {
        return dd.findinviterbyid(userid);
    }

    public UserCheap save(UserCheap userCheap) {
        return ucr.save(userCheap);
    }
}
