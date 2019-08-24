package com.zz.repository;

import com.zz.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<UserOrder, String> {
    UserOrder findByUseridAndStatus(String userid, String status);

    List<UserOrder> findAllByUserid(String userid);

    void deleteByUseridAndId(String userid, String id);
}
