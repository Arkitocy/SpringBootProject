package com.zz.controller;

import com.google.gson.Gson;
import com.zz.entity.Cookies;
import com.zz.entity.User;
import com.zz.service.TokenService;
import com.zz.service.UserService;

import com.zz.utils.KeyUtils;
import com.zz.utils.Md5Util;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("user")
public class UserController {
    //使用SpringIOC控制反转，让spring容器创建对象
    //userservice上添加@Service注解
    @Resource
    UserService us;
    @Resource
    TokenService ts;

    /**
     * 注册方法
     *
     * @param user
     * @return
     */
    @RequestMapping("register")
    public Object save(@RequestBody User user) {
        User user1 = new User();
        Md5Util md5 = new Md5Util();
        user1.setId(KeyUtils.genUniqueKey());
        user1.setUsername(md5.StringInMd5(user.getUsername()));
        user1.setPassword(md5.StringInMd5(user.getPassword()));
        user1.setEmail(md5.StringInMd5(user.getEmail()));
        if (us.save(user1) != null) {
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 比较是否有重名
     *
     * @param username
     * @return
     */
    @RequestMapping("checkName/{username}")
    @ResponseBody
    public Map checkName(@PathVariable("username") String username) {
        Md5Util md5 = new Md5Util();
        String name = md5.StringInMd5(username);//md5加密
        List<User> user = us.findByUserName(name);
        boolean result = false;
        if (user.size() > 0) {
            result = true;
        }
        Map map = new HashMap();
        map.put("result", result);
        return map;
    }

    /**
     * 检查邮箱
     *
     * @param email
     * @return
     */
    @RequestMapping("checkEmail/{email}")
    @ResponseBody
    public Map checkEmail(@PathVariable("email") String email) {
        Md5Util md5 = new Md5Util();
        String emailcode = md5.StringInMd5(email);//md5加密
        List<User> user = us.findByEmail(emailcode);
        boolean result = false;
        if (user.size() > 0) {
            result = true;
        }
        Map map = new HashMap();
        map.put("result", result);
        return map;
    }

    /**
     * 登录，使用cookie存放登陆用户名
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("login")
    public Map loginValidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        Md5Util md5 = new Md5Util();
        //添加 Cookie
        String loginUsername = request.getParameter("username");
        String loginPassword = request.getParameter("password");
//        us.addCookie(loginUsername, response, request);
        User user = us.findByUserNameAndPassword(md5.StringInMd5(loginUsername), md5.StringInMd5(loginPassword));
        if (user != null) {
//            Map rsmap = ts.operateToKen(user,user.getId());
//            System.out.println(rsmap);
            us.addCookie(loginUsername, response, request);
            map.put("result", "index.html");
        } else {
            map.put("result", "login.html");
        }

        return map;
    }


    /**
     *     	 * 获取 Cookie中的信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("getCookie")
    @ResponseBody
    public String getCookie(HttpServletRequest request) throws IOException {
        String loginUsername = "";
        String loginPassword = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            //遍历Cookie
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                //此处类似与Map
                if ("loginUsername".equals(cookie.getName())) {
                    loginUsername = cookie.getValue();
                }
            }
        }
        //自己定义的javabean Cookies
        Cookies cookie = new Cookies();
        cookie.setLoginUsername(loginUsername);
        Gson gson = new Gson();
        return gson.toJson(cookie);
    }

    /**
     * 登出清除cookie
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("logoutCookie")
    @ResponseBody
    public Map logoutCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        Cookie loginUsernameCookie = new Cookie("loginUsername", null);//新建一个cookie
        loginUsernameCookie.setMaxAge(0);//设置生命为0 即清除
        loginUsernameCookie.setPath("/");
        response.addCookie(loginUsernameCookie);//覆盖原有cookie
        map.put("result", "success");
        return map;
    }
}
