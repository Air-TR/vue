package com.tr.vue.config;

import com.tr.vue.config.filter.TokenFilter;
import com.tr.vue.config.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author taorun
 * @date 2023/1/30 8:35
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.ex-path}")
    private String securityExPath;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private TokenFilter tokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证用户的来源（从数据库获取）
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 将不需要鉴权的请求路径写在 antMatchers() 中，这边是真正让 SpringSecurity 过滤放行的地方，而不在 TokenFilter 判断（TokenFilter 仅做 Token 校验）
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** 来自于 orion */
        http.authorizeRequests()
            .antMatchers(securityExPath.split(","))
            .permitAll().and()
            .authorizeRequests().anyRequest().authenticated()
            .and().csrf().disable().cors();

        /** 来自于网上 */
//        http.csrf().disable() // 关闭 csrf
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 不通过 Session 获取 SecurityContext
//            .and()
//            .authorizeRequests()
//            .antMatchers(securityExPath.split(",")).anonymous() // 允许匿名访问的路径
//            .anyRequest().authenticated(); // 除上面外的所有请求全部需要鉴权认证

        // 把 token 校验过滤器添加到过滤器链中
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 重写这个方法干什么不知道，但是 orion 和网上都有
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
