package com.tr.vue.config.filter;

import com.tr.vue.constant.RedisKey;
import com.tr.vue.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        String tokenKey = RedisKey.TOKEN + username;
        String user = stringRedisTemplate.opsForValue().get(tokenKey);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户未登录");
        }
        // 存入 SecurityContextHolder
        // TODO 获取权限信息封装到 Authentication 中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }

}
