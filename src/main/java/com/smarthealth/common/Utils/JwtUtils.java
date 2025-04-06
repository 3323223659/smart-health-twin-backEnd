package com.smarthealth.common.Utils;

import com.smarthealth.domain.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // JWT 密钥(建议从配置文件中读取)
    private static final String SECRET_KEY = "smart-health-twin-123-smart-health-twin-smart-health-twin";

    // JWT 有效期(单位:毫秒)
    private static final long EXPIRATION_TIME = 86400000L; // 24 小时

    /**
     * 生成 JWT
     *
     * @param subject 主题（通常是用户ID或用户名）
     * @param claims  自定义 claims（负载信息）
     * @return JWT 字符串
     */
    public static String generateToken(String subject, Map<String, Object> claims) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setClaims(claims) // 设置 claims
                .setSubject(subject) // 设置主题
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
                .signWith(key) // 签名
                .compact();
    }

    /**
     * 解析 JWT
     *
     * @param token JWT 字符串
     * @return Claims 对象（包含负载信息）
     */
    public static Claims parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key) // 设置签名密钥
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取 JWT 中的主题（subject）
     *
     * @param token JWT 字符串
     * @return 主题（通常是用户ID或用户名）
     */
    public static String getSubject(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 获取 JWT 中的自定义 claim
     *
     * @param token JWT 字符串
     * @param claimName claim 名称
     * @return claim 值
     */
    public static Object getClaim(String token, String claimName) {
        return parseToken(token).get(claimName);
    }

    /**
     * 验证 JWT 是否有效
     *
     * @param token JWT 字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true; // 如果解析成功，说明JWT有效
        } catch (JwtException e) {
            return false; // 解析失败
        }
    }


}
