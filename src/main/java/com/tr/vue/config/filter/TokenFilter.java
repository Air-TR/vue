package com.tr.vue.config.filter;

import com.alibaba.fastjson.JSON;
import com.tr.vue.constant.RedisKey;
import com.tr.vue.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author taorun
 * @date 2023/1/30 13:10
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 这边过滤每一个进入系统的请求
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取 token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析 token 获取用户名
        String username = JwtUtil.getUsername(token);
        // 从 redis 中获取用户信息
        String userToken = stringRedisTemplate.opsForValue().get(RedisKey.TOKEN + username);
        if (Objects.isNull(userToken)) {
            throw new RuntimeException("用户未登录");
        }
        // 从 redis 中获取用户权限
        String authoritiesValue = stringRedisTemplate.opsForValue().get(RedisKey.AUTHORITIES + username);
        List<SimpleGrantedAuthority> authorities = JSON.parseArray(authoritiesValue, SimpleGrantedAuthority.class);
        // 构造 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userToken, null, authorities);
        // 存入 SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }

}
