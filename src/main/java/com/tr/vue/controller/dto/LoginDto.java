package com.tr.vue.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: TR
 * @Date: 2023/2/14 13:33
 */
@Data
public class LoginDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
