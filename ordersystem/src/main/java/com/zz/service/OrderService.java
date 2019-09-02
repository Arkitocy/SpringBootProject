package com.zz.service;

import com.zz.entity.UserOrder;
import com.zz.repository.DTODao;
import com.zz.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service

public class OrderService {
    @Resource
    OrderRepository or;
    @Resource
    DTODao dd;

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


    public Page<Object[]> showOrderPageByUseridAndStatus(String userid, String status, Pageable pageable) {
        return or.showOrderByStatusAndUserid(userid, status, pageable);
    }

    public Page<Object[]> showOrderByUserid(String userid, Pageable pageable) {
        return or.showOrderByUserid(userid, pageable);
    }

    public UserOrder findAllById(String id) {
        return or.findAllById(id);
    }

    public Page<Object[]> showAllOrder(Pageable pageable) {
        return or.showAllOrder(pageable);
    }

    public Page<Object[]> showOrderByStatus(String status, Pageable pageable) {
        return or.showOrderByStatus(status, pageable);
    }
    public Page<Object[]> adminselectOrder(String perm,Pageable pageable){
        return or.adminselectOrder(perm,pageable);
    }

}
