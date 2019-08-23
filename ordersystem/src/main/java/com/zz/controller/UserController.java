package com.zz.controller;

import com.google.gson.Gson;
import com.zz.entity.Cookies;
import com.zz.entity.User;
import com.zz.service.TokenService;
import com.zz.service.UserService;

import com.zz.utils.KeyUtils;
import com.zz.utils.Md5Util;
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


@RestController
@RequestMapping("user")
public class UserController {
    //使用SpringIOC控制反转，让spring容器创建对象
    //userservice上添加@Service注解
    @Resource
    UserService us;
    @Resource
    TokenService ts;
    @Value("${fileUpLoadPath}")
    String filepath;

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
        user1.setId(md5.StringInMd5(KeyUtils.genUniqueKey()));
        user1.setUsername(user.getUsername());
        user1.setPassword(md5.StringInMd5(user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setHeadimgid(user.getHeadimgid());
        if (us.save(user1) != null) {
            return "success";
        } else {
            return "fail";
        }
    }

    @RequestMapping("update/{username}")
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
        String name = username;//md5加密
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
        String emailcode = email;//md5加密
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
     * 查询邮箱
     *
     * @param username
     * @return
     */
    @RequestMapping("getEmail/{username}")
    public Map getEmail(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("email", user.get(0).getEmail());
        }
        return map;
    }

    //根据用户名得到id
    @RequestMapping("getId/{username}")
    public Map getId(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("id", user.get(0).getId());
        }
        return map;
    }

    //根据用户名得到头像id
    @RequestMapping("getHeadImg/{username}")
    public Map getHeadImg(@PathVariable("username") String username) {
        List<User> user = us.findByUserName(username);
        Map map = new HashMap();
        if (user.size() > 0) {
            map.put("headimgid", user.get(0).getHeadimgid());
        }
        return map;
    }



    /**
     * 上传头像
     * @param myFile
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "uploadheadimg/{username}", method = RequestMethod.POST)
    public Map setHeadImg(@RequestParam MultipartFile myFile,@PathVariable("username") String  username, HttpSession session) throws IOException {
        String originalFilename = myFile.getOriginalFilename();
        Map map = new HashMap();
        List<User> user = us.findByUserName(username);
        int pos = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(pos);
        String uuid = UUID.randomUUID().toString();
        String fullPath = filepath + File.separator + uuid + suffix;
        String headimgid = File.separator + uuid + suffix;
        String lastpath=filepath + user.get(0).getHeadimgid();
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
        if(user2.getId()!=""){
            map.put("result","success");
        }else{
            map.put("result","fail");
        }
        map.put("headimgid", headimgid);
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
        User user = us.findByUserNameAndPassword(loginUsername, md5.StringInMd5(loginPassword));
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

    @RequestMapping("setCookie")
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
