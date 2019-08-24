package com.zz.service;

import com.zz.entity.UserOrder;
import com.zz.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service

public class OrderService {
    @Resource
    OrderRepository or;

    public UserOrder findByUseridAndStatus(String userid, String status) {
        return or.findByUseridAndStatus(userid, status);
    }

    public List<UserOrder> findAllByUserid(String userid) {
        return or.findAllByUserid(userid);
    }

    public void deleteByUseridAndId(String userid, String id) {
        or.deleteByUseridAndId(userid, id);
    }

    public UserOrder save(UserOrder userOrder) {
        return or.save(userOrder);
    }
}
