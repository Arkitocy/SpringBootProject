package com.zz.repository;

import com.zz.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenEntity,String> {
    TokenEntity save(TokenEntity tokenEntity);
    TokenEntity findByUserId(String userid);
}
