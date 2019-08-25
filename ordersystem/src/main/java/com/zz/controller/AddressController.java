package com.zz.controller;

import com.google.gson.Gson;
import com.zz.entity.Cookies;
import com.zz.entity.User;
import com.zz.entity.UserAddress;
import com.zz.service.AddressService;
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
@RequestMapping("address")
public class AddressController {
    @Resource
    AddressService as;
    @Resource
    UserService us;
    @RequestMapping("addAddress/{username}")
    public Map saveUserAddress(@RequestBody UserAddress ua, @PathVariable("username") String  username){
        Map map = new HashMap();
        UserAddress ua1=new UserAddress();
        Md5Util md5 = new Md5Util();
        List<User> user = us.findByUserName(username);
        User us1=user.get(0);
        String userid=us1.getId();
        ua1.setId(md5.StringInMd5(KeyUtils.genUniqueKey()));
        ua1.setUserid(userid);
        ua1.setPhone(ua.getPhone());
        ua1.setReciever(ua.getReciever());
        ua1.setMainaddress(ua.getMainaddress());
        ua1.setDetailaddress(ua.getDetailaddress());
        if (as.save(ua1) != null) {
            map.put("result", "success");
        } else {
            map.put("result", "fail");
        }
        return map;

    }
    @RequestMapping("showAddress/{username}")
    public List<UserAddress> showUserAddress(@PathVariable("username") String  username){
        System.out.println(username);
        List<User> user = us.findByUserName(username);
        User us1=user.get(0);
        String userid=us1.getId();
        return as.getByUserid(userid);
    }

    @RequestMapping("deleteAddress/{id}")
    public Map deleteAddressById(@PathVariable("id") String  id){
        Map map = new HashMap();
      if(id!=null){
          map.put("result", "success");
          as.deleteAddress(id);
      }else{
          map.put("result", "fail");
      }
      return map;
    }


}
