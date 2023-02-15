package com.tr.vue.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author taorun
 * @date 2023/1/30 15:02
 */
public class PasswordUtil {

    /**
     * 密码：明文 & 密文
     *  123456: $2a$10$w6kikyPtTg3jHK375L48oeGqlRrw084eRD2DXPFwZm1j2YduNhP8e
     *  000000: $2a$10$9XegwO4I99UNZXpYytmmuez.NXp2OuNUzmcgEJ3SNhl75yH4mjXEq
     */
    public static void main(String[] args) {
        System.out.println(encode("000000"));
    }

    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
