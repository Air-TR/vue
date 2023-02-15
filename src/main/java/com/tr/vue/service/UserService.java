package com.tr.vue.service;

import com.tr.vue.entity.User;

public interface UserService {

    User findByUsername(String username);

    User register(User user);

}
