package com.zz.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class UserOrder {
    @Id
    @Column(length = 100)
    private String id;
    private String userid;
    private String productid;
    private String num;
    private String cheap;
    private BigDecimal sum;
    private String sellerid;
    private String postid;
    private String addressid;
    private String status;
    private String payid;
    private String createtime;
    private String paytime;
    private String completetime;



}
