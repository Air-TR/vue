package com.tr.vue.service.impl;

import com.tr.vue.common.exception.BusinessException;
import com.tr.vue.entity.User;
import com.tr.vue.jpa.UserRepository;
import com.tr.vue.service.UserService;
import com.tr.vue.util.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author taorun
 * @date 2023/1/30 9:27
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public User register(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
