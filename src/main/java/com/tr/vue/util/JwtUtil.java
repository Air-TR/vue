package com.tr.vue.util;

import com.tr.vue.common.exception.BusinessException;
import com.tr.vue.config.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author taorun
 * @date 2023/1/30 11:59
 */
@Component
public class JwtUtil {

    @Resource
    private TokenProperties tokenProperties;

    private static JwtUtil jwtUtil;

    @PostConstruct
    public void init(){
        jwtUtil = this;
        jwtUtil.tokenProperties = this.tokenProperties;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成 jtw (token)
     *
     * @param subject token 中要存放的数据（json 格式）
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成 jtw (token)
     *
     * @param subject   token 中要存放的数据（json 格式）
     * @param ttlMillis token 超时时间
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    /**
     * 生成 jtw (token)
     *
     * @param id
     * @param subject   token 中要存放的数据（json 格式）
     * @param ttlMillis token 超时时间
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = jwtUtil.tokenProperties.getExpireTime() * 1000L; // 这边需要转换为毫秒，所以 * 1000
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)            // 唯一 ID
                .setSubject(subject)    // 主题，可以是 JSON 数据
                .setIssuer("TR")        // 签发者
                .setIssuedAt(now)       // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用 HS256 对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(jwtUtil.tokenProperties.getSecretKey());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 解析 token 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String username = claims.getSubject();
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("无效 token");
        }
    }

}
