package com.tr.vue.service.impl;

import com.alibaba.fastjson.JSON;
import com.tr.vue.config.properties.TokenProperties;
import com.tr.vue.constant.RedisKey;
import com.tr.vue.entity.User;
import com.tr.vue.service.LoginAndOutService;
import com.tr.vue.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author taorun
 * @date 2023/1/30 9:43
 */
@Service
public class LoginAndOutServiceImpl implements LoginAndOutService {

    @Resource
    private TokenProperties tokenProperties;

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取 userDetail
        org.springframework.security.core.userdetails.User userDetail = (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        // 使用 username 生成 token（如要用 user_id 生成，根据 username 去获取 user_id）
        String token = JwtUtil.createJWT(userDetail.getUsername());
        // token 存入 redis
        stringRedisTemplate.opsForValue().set(RedisKey.TOKEN + userDetail.getUsername(), token, 3600, TimeUnit.SECONDS);
        // authorities 存入 redis
        stringRedisTemplate.opsForValue().set(RedisKey.AUTHORITIES + userDetail.getUsername(), JSON.toJSONString(userDetail.getAuthorities()), 3600, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public Boolean logout(String token) {
        String username = JwtUtil.getUsername(token);
        return stringRedisTemplate.delete(RedisKey.TOKEN + username);
    }

}
