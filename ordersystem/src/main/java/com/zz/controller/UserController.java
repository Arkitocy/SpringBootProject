package com.zz.controller;

import com.google.gson.Gson;
import com.zz.entity.Cookies;
import com.zz.entity.User;
import com.zz.entity.UserAddress;
import com.zz.service.AddressService;
import com.zz.service.UserService;

import com.zz.utils.KeyUtils;
import com.zz.utils.MD5Utils;
import com.zz.utils.Md5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.*;

@Api(value = "用户Controller")
@RestController
@RequestMapping("user")
public class UserController {
    //使用SpringIOC控制反转，让spring容器创建对象
    //userservice上添加@Service注解
    @Resource
    UserService us;
    @Value("${fileUpLoadPath}")
    String filepath;


    @ApiOperation(value = "注册")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping("register")
    public Object save(@RequestBody User user) {
        User user1 = new User();
        Md5Util md5 = new Md5Util();
        user1.setId(md5.StringInMd5(KeyUtils.genUniqueKey()));
        user1.setUsername(user.getUsername());
        user1.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setHeadimgid(user.getHeadimgid());
        user1.setBeinvitedcode(user.getBeinvitedcode());
        user1.setInvitecode(md5.StringInMd5(KeyUtils.genUniqueKey() + user.getUsername()));
        if (us.save(user1) != null) {
            return "success";
        } else {
            return "fail";
        }
    }

    @ApiOperation(value = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    })
    @PostMapping("update/{username}")
    public Map update(@RequestBody User user, @PathVariable("username") String username) {
        User user1 = new User();
        Map map = new HashMap();
        List<User> users = us.findByUserName(username);
        user1.setId(users.get(0).getId());
        user1.setUsername(user.getUsername());
        user1.setPassword(users.get(0).getPassword());
        user1.setEmail(user.getEmail());
        if (us.save(user1) != null) {
            map.put("result", "success");
        }
        return map;
    }

    @ApiOperation(value = "确认用户名是否重复")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("checkName")
    @ResponseBody
    public Map checkName(HttpServletRequest request) {
        Md5Util md5 = new Md5Util();
        Map map = new HashMap();
        String name = request.getParameter("username");//md5加密
        List<User> user = us.findByUserName(name);
        if (user.size() > 0) {
            map.put("result","false");
        }else {
            map.put("result","true");
        }
        return map;
    }

    @ApiOperation(value = "确认邮箱是否重复")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("checkEmail")
    @ResponseBody
    public Map checkEmail(HttpServletRequest request) {
        Map map = new HashMap();
        String email = request.getParameter("emailcode");
        String emailcode = email;//md5加密
        List<User> user = us.findByEmail(emailcode);
        if (user.size() > 0) {
            map.put("result","false");
        }else {
            map.put("result","true");
        }
        return map;
    }

    @ApiOperation(value = "查询邮箱")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("getEmail/{username}")
    public Map getEmail(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("email", user.get(0).getEmail());
        }
        return map;
    }


    @ApiOperation(value = "查询邀请码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("getInviteCode/{username}")
    public Map getInviteCode(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("myinvitecode", user.get(0).getInvitecode());
        }
        return map;
    }

    @ApiOperation(value = "查询用户id")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("getId/{username}")
    public Map getId(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("id", user.get(0).getId());
        }
        return map;
    }

    @ApiOperation(value = "查询用户头像id")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path")
    @PostMapping("getHeadImg/{username}")
    public Map getHeadImg(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("headimgid", user.get(0).getHeadimgid());
        }
        return map;
    }


    @ApiOperation(value = "上传头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "myFile", value = "头像文件", required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "uploadheadimg/{username}")
    public Map setHeadImg(@RequestParam MultipartFile myFile, @PathVariable("username") String username, HttpSession session) throws IOException {
        String originalFilename = myFile.getOriginalFilename();
        Map map = new HashMap();
        List<User> user = us.findByUserName(username);
        int pos = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(pos);
        String uuid = UUID.randomUUID().toString();
        String fullPath = filepath + File.separator + uuid + suffix;
        String headimgid = File.separator + uuid + suffix;
        String lastpath = filepath + user.get(0).getHeadimgid();
        InputStream in = null;
        try {
            in = myFile.getInputStream();
            OutputStream out = new FileOutputStream(new File(fullPath));
            int len = 0;
            byte[] buf = new byte[100 * 1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            File lastfile = new File(lastpath);
            if (lastfile.isFile() && lastfile.exists()) {
                lastfile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user1 = new User(user.get(0).getId(), user.get(0).getUsername(), user.get(0).getPassword(), user.get(0).getEmail(), headimgid);
        User user2 = us.save(user1);
        if (user2.getId() != "") {
            map.put("result", "success");
        } else {
            map.put("result", "fail");
        }
        map.put("headimgid", headimgid);
        return map;
    }


    @ApiOperation("登陆")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User", paramType = "Get")
    @GetMapping("login")
    public Map loginValidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        Md5Util md5 = new Md5Util();
        //添加 Cookie
        String loginUsername = request.getParameter("username");
        String loginPassword = request.getParameter("password");
        User user = us.findByUserNameAndPassword(loginUsername, md5.StringInMd5(loginPassword));
        if (user != null) {
            us.addCookie(loginUsername, response, request);
            map.put("result", "index.html");
        } else {
            map.put("result", "login.html");
        }
        return map;
    }

    @ApiOperation("设置cookie")
    @PostMapping("setCookie")
    public Map setCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        Md5Util md5 = new Md5Util();
        //添加 Cookie
        String loginUsername = request.getParameter("username");
//        us.addCookie(loginUsername, response, request);
        List<User> user = us.findByUserName(loginUsername);
        if (user.size() > 0) {
            us.addCookie(loginUsername, response, request);
            map.put("result", "success");
        } else {
            map.put("result", "fail");
        }
        return map;
    }

    @ApiOperation("获取cookie")
    @PostMapping("getCookie")
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

    @ApiOperation("登出清除cookie")
    @PostMapping("logoutCookie")
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

    @ApiOperation("保存邀请码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "beinvitedcode", value = "被邀请码", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("saveinvite/{username}/{beinvitedcode}")
    public Map savebeinvitedcod(@PathVariable("username") String username, @PathVariable("beinvitedcode") String beinvitedcode) {
        Map map = new HashMap();
        List<User> users = us.findByUserName(username);
        User user = users.get(0);
        if ("".equals(user.getBeinvitedcode())) {
            user.setBeinvitedcode(beinvitedcode);
            if (us.save(user) != null) {
                map.put("result", "success");
            } else {
                map.put("result", "fail");
            }
        } else {
            map.put("result", "already");
        }
        return map;
    }


}
