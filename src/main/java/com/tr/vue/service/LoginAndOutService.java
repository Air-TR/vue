package com.tr.vue.service;

import com.tr.vue.entity.User;
import org.springframework.http.ResponseEntity;

public interface LoginAndOutService {

    String login(User user);

    Boolean logout(String token);

}
