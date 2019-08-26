package com.zz.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class DTODao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findusercheap(String userid) {
        String sql = "select * from user_cheap where userid=?";
        Object[] args = {userid};
        int[] argTypes = {Types.VARCHAR};
        return this.jdbcTemplate.queryForList(sql, args, argTypes);
    }

}
