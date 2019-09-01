package com.zz.entity;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Product {
    @Id
    @Column(length=100)
    private String id;
    private String name;
    private BigDecimal price;
    private int num;



}