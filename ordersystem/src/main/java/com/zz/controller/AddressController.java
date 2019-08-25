package com.zz.controller;

import com.google.gson.Gson;
import com.zz.entity.Cookies;
import com.zz.entity.User;
import com.zz.entity.UserAddress;
import com.zz.service.AddressService;
import com.zz.service.UserService;

import com.zz.utils.KeyUtils;
import com.zz.utils.Md5Util;
import io.swagger.annotations.ApiImplicitParam;
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
    @ApiOperation(value = "查询用户地址信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path")
    @PostMapping("getAddress/{id}")
    public Map getAddress(@PathVariable("id") String id) {
        List<UserAddress> ua = as.findById(id);
        Map map = new HashMap();
        if (ua.size() > 0) {
            map.put("id",ua.get(0).getId());
            map.put("userid",ua.get(0).getUserid());
            map.put("reciever", ua.get(0).getReciever());
            String[] a =  ua.get(0).getMainaddress().split(" ");
            map.put("cmbProvince",a[0]);
            map.put("cmbCity",a[1]);
            map.put("cmbArea",a[2]);
            map.put("detailaddress", ua.get(0).getDetailaddress());
            map.put("phone", ua.get(0).getPhone());
        }
        return map;
    }



    @RequestMapping("alterAddress/{id}")
    public Map alterAddressById(@RequestBody UserAddress ua,@PathVariable("id") String id){
        Map map=new HashMap();
        UserAddress ua1=new UserAddress();
        ua1.setId(ua.getId());
        ua1.setUserid(ua.getUserid());
        ua1.setPhone(ua.getPhone());
        ua1.setReciever(ua.getReciever());
        ua1.setMainaddress(ua.getMainaddress());
        ua1.setDetailaddress(ua.getDetailaddress());
        if(as.save(ua1)!=null){
            map.put("result","success1");
        }else{
            map.put("result","fail1");
        }
        return map;
    }

}
