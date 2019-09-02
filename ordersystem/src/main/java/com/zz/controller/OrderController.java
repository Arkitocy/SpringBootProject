package com.zz.controller;

import com.zz.entity.Product;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        UserOrder uo1 = new UserOrder();
        User user = us.findByUserName(username).get(0);
        Product product = ps.findAllById(userOrder.getProductid());
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
        if(Integer.parseInt(userOrder.getNum())<product.getNum()){
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
            uo1=os.save(uo);
        }
        return uo1;
    }



    @RequestMapping("orderPage/{username}/{status}/{page}")
    public Page<Object[]> showOrder(@PathVariable("username") String  username,@PathVariable("status") String  status,@PathVariable("page") String  page){
        List list = new ArrayList();
        String userid = us.findByUserName(username).get(0).getId();
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return os.showOrderPageByUseridAndStatus(userid,status,pageable);

    }

    @RequestMapping("cancelOrder/{id}")
    public Map cancelOrder(@PathVariable("id") String id){
        UserOrder order=os.findAllById(id);
        Map map=new HashMap();
        if(("待付款").equals(order.getStatus())){
            order.setStatus("已取消");
            UserOrder uo  = os.save(order);
            System.out.println(uo.getStatus());
            map.put("result","success");

        }else{
            map.put("result","fail");
        }
        return map;
    }

    @RequestMapping("pay/{id}")
    public Map payOrder(@PathVariable("id") String id){
        UserOrder order=os.findAllById(id);
        Map map=new HashMap();
        if(("待付款").equals(order.getStatus())){
            order.setStatus("待发货");
            UserOrder uo  = os.save(order);
            System.out.println(uo.getStatus());
            map.put("result","success");

        }else{
            map.put("result","fail");
        }
        return map;
    }
    @RequestMapping("refund/{id}")
    public Map refundOrder(@PathVariable("id") String id){
        UserOrder order=os.findAllById(id);
        Map map=new HashMap();
        if(("待发货").equals(order.getStatus())){
            order.setStatus("待退款");
            UserOrder uo  = os.save(order);
            map.put("result","success");

        }else{
            map.put("result","fail");
        }
        return map;
    }

    @RequestMapping("confirmReceipt/{id}")
    public Map connfirmOrder(@PathVariable("id") String id){
        UserOrder order=os.findAllById(id);
        Map map=new HashMap();
        if(("待收货").equals(order.getStatus())){
            order.setStatus("完成");
            UserOrder uo  = os.save(order);
            map.put("result","success");

        }else{
            map.put("result","fail");
        }
        return map;
    }

    @RequestMapping("all/{username}/{page}")
    public Page<Object[]> showAllOrder(@PathVariable("username") String  username,@PathVariable("page") String  page){
        List list = new ArrayList();
        String userid = us.findByUserName(username).get(0).getId();
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return os.showOrderByUserid(userid,pageable);

    }

    @RequestMapping("adminall/{page}")
    public Page<Object[]> showAllOrder(@PathVariable("page") String  page){
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return os.showAllOrder(pageable);
    }

    @RequestMapping("adminOrderPage/{status}/{page}")
    public Page<Object[]> showOrderByStatus(@PathVariable("status") String  status,@PathVariable("page") String  page){
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return os.showOrderByStatus(status,pageable);
    }

    @RequestMapping("adminSelectOrder/{perm}/{page}")
    public Page<Object[]> adminselectOrder(@PathVariable("perm") String  perm,@PathVariable("page") String  page){
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return os.adminselectOrder(perm,pageable);
    }




}
