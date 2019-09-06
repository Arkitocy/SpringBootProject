package com.zz.repository;

import com.zz.entity.UserOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<UserOrder, String> {
    UserOrder findByUseridAndStatus(String userid, String status);

    List<UserOrder> findAllByUserid(String userid);

    void deleteByUseridAndId(String userid, String id);

    @Query(value = "select product.name ,product.price ,user_order.num ,user_order.sum,user_order.status,user_order.id from ( user_order left join product on (user_order.productid=product.id)) where user_order.userid= ?1 and user_order.status like CONCAT('%',?2,'%') ",
            nativeQuery = true)
    Page<Object[]> showOrderByStatusAndUserid(String userid, String status, Pageable pageable);

    @Query(value = "select product.name ,product.price ,user_order.num ,user_order.sum,user_order.status,user_order.id from ( user_order left join product on (user_order.productid=product.id)) where user_order.userid= ?1",
            nativeQuery = true)
    Page<Object[]> showOrderByUserid(String userid, Pageable pageable);


    @Query(value = "select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.createtime,user_order.completetime,user_order.status from ( user_order left join product on (user_order.productid=product.id)) where user_order.status like CONCAT('%',?1,'%')",
            nativeQuery = true)
    Page<Object[]> showOrderByStatus(String status, Pageable pageable);

    @Query(value = "select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.createtime,user_order.completetime,user_order.status from ( user_order left join product on (user_order.productid=product.id)) ",
            nativeQuery = true)
    Page<Object[]> showAllOrder(Pageable pageable);

    @Query(value = "select id,userid,name,price,num,sum,createtime,completetime,status FROM (select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.status,user_order.createtime,user_order.completetime from ( user_order left join product on (user_order.productid=product.id)) where  user_order.userid=?1 or product.name=?1 or user_order.id=?1) AS a where status like CONCAT('%',?2,'%')", nativeQuery = true)
    Page<Object[]> adminselectOrder(String parm, String status, Pageable pageable);

    @Query(value = "select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.createtime,user_order.completetime,user_order.status from ( user_order left join product on (user_order.productid=product.id)) where  user_order.userid=?1 or product.name=?1 or user_order.id=?1",
            nativeQuery = true)
    Page<Object[]> adminselectOrder(String parm, Pageable pageable);

    @Query(value = "select id,userid,name,price,num,sum,createtime,completetime,status FROM(\n" +
            "select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.createtime,user_order.completetime,user_order.status from ( user_order left join product on (user_order.productid=product.id)) where  user_order.userid=?1 or product.name=?1 or user_order.id=?1) as a where\n" +
            " str_to_date(createtime, '%Y-%m-%d') BETWEEN str_to_date(?2, '%Y-%m-%d') AND str_to_date(?3, '%Y-%m-%d') OR str_to_date(completetime, '%Y-%m-%d') BETWEEN str_to_date(?2, '%Y-%m-%d') AND str_to_date(?3, '%Y-%m-%d')\n",
            nativeQuery = true)
    Page<Object[]> adminselectOrder(String parm, String starttime,String finishtime,Pageable pageable);

    @Query(value = "select id,userid,name,price,num,sum,createtime,completetime,status FROM (select id,userid,name,price,num,sum,createtime,completetime,status FROM (select user_order.id,user_order.userid,product.name ,product.price ,user_order.num ,user_order.sum,user_order.status,user_order.createtime,user_order.completetime from ( user_order left join product on (user_order.productid=product.id)) where  user_order.userid=?1 or product.name=?1 or user_order.id=?1) AS a where status like CONCAT('%',?2,'%') )as b where str_to_date(createtime, '%Y-%m-%d') BETWEEN str_to_date(?3, '%Y-%m-%d') AND str_to_date(?4, '%Y-%m-%d') OR str_to_date(completetime, '%Y-%m-%d') BETWEEN str_to_date(?3, '%Y-%m-%d') AND str_to_date(?4, '%Y-%m-%d')\n", nativeQuery = true)
    Page<Object[]> adminselectOrder(String parm, String status,String starttime,String finishtime, Pageable pageable);

    UserOrder findAllById(String id);


}
