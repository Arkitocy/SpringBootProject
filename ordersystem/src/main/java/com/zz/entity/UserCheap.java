package com.zz.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class UserCheap {
    @Id
    @Column(length = 100)
    public String userid;
    public BigDecimal cheap;
    public String updatedate;
    public String updateuserid;
}
