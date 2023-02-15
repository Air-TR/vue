package com.tr.vue.controller;

import com.tr.vue.controller.dto.LoginDto;
import com.tr.vue.controller.dto.RegisterDto;
import com.tr.vue.entity.User;
import com.tr.vue.service.impl.LoginAndOutServiceImpl;
import com.tr.vue.service.impl.UserServiceImpl;
import com.tr.vue.util.ConvertKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: TR
 * @Date: 2023/2/14 10:50
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private LoginAndOutServiceImpl loginAndOutService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDto loginDto) {
        return loginAndOutService.login(ConvertKit.convert(loginDto, User.class));
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Boolean logout(@RequestParam String token) {
        return loginAndOutService.logout(token);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public User register(@RequestBody RegisterDto registerDto) {
        return userService.register(ConvertKit.convert(registerDto, User.class));
    }

    @ApiOperation("根据用户名查找用户")
    @GetMapping("/{username}")
    public User findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

}
