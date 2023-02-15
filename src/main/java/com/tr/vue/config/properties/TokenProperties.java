package com.tr.vue.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: TR
 * @Date: 2023/2/15 13:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private Integer expireTime;
    private String secretKey;

}
