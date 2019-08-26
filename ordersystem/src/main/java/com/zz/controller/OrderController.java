package com.zz.controller;

import com.zz.entity.User;
import com.zz.entity.UserOrder;
import com.zz.service.OrderService;
import com.zz.service.ProductService;
import com.zz.service.UserService;
import com.zz.utils.KeyUtils;
import com.zz.utils.Md5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(value = "订单Controller")
@RestController
@RequestMapping("order")
public class OrderController {
    @Resource
    OrderService os;
    @Resource
    UserService us;
    @Resource
    ProductService ps;

    @ApiOperation(value = "添加订单")
    @PostMapping("save/{username}")
    public UserOrder save(@RequestBody UserOrder userOrder, @PathVariable("username") String username) {
        UserOrder uo = new UserOrder();
        User user = us.findByUserName(username).get(0);
        uo.setId(Md5Util.StringInMd5(KeyUtils.genUniqueKey()));
        uo.setUserid(user.getId());
        System.out.println(userOrder.getAddressid());
        uo.setAddressid(userOrder.getAddressid());
        uo.setProductid(userOrder.getProductid());
        uo.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        uo.setSellerid(userOrder.getSellerid());
        uo.setCheap(userOrder.getCheap());
        uo.setNum(userOrder.getNum());
        uo.setSum(userOrder.getSum());
        if (userOrder.getPayid() != null) {
            uo.setPayid(userOrder.getPayid());
            uo.setPaytime(userOrder.getPaytime());
            uo.setPostid("");
            uo.setStatus("待发货");
        } else {
            uo.setPayid("");
            uo.setPaytime("");
            uo.setPostid("");
            uo.setStatus("待付款");
        }
        return os.save(uo);
    }

}
