package com.zz.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DTODao {
    @Resource
    private JdbcTemplate jdbcTemplate;


}
