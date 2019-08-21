package com.zz.service;

import com.zz.entity.TokenEntity;
import com.zz.entity.User;
import com.zz.repository.TokenRepository;
import com.zz.utils.KeyUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Resource
    TokenRepository tokenRepository;

    public String creatToken(String userId, Date date) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT") // 设置header
                .setHeaderParam("alg", "HS256").setIssuedAt(date) // 设置签发时间
                .setExpiration(new Date(date.getTime() + 1000 * 60 * 60))
                .claim("userId", String.valueOf(userId)) // 设置内容
                .setIssuer("lws")// 设置签发人
                .signWith(signatureAlgorithm, "签名"); // 签名，需要算法和key
        String jwt = builder.compact();
        return jwt;
    }


    public Map<String, Object> operateToKen(User user, String userId) {
        Map map = new HashMap();
        //根据数据库的用户信息查询Token
        TokenEntity token = tokenRepository.findByUserId(userId);
        //为生成Token准备
        String TokenStr = "";
        Date date = new Date();
        int nowTime = (int) (date.getTime() / 1000);
        //生成Token
        TokenStr = creatToken(userId, date);
        if (null == token) {
            //第一次登陆
            token = new TokenEntity();
            token.setToken(TokenStr);
            token.setBuildTime(nowTime);
            token.setUserId(userId);
            token.setId(KeyUtils.genUniqueKey());
            tokenRepository.save(token);
        } else {
            //登陆就更新Token信息
            TokenStr = creatToken(userId, date);
            token.setToken(TokenStr);
            token.setBuildTime(nowTime);
            tokenRepository.save(token);
        }
        //返回Token信息给客户端
        map.put("user", user);
        map.put("TokenId", TokenStr);
        return map;
    }

}
