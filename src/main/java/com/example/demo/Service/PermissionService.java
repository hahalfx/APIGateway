package com.example.demo.Service;

import com.example.demo.Feign.UserServiceClient;
import com.example.demo.Feign.UserServiceClient.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class PermissionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserServiceClient userServiceClient;

    private static final String SECRET_KEY = "your-256-bit-secret";
    private static final String PERMISSIONS_CACHE_PREFIX = "permissions:";

    public String login(String userName, String userPhoneNumber) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(userName);
        loginRequest.setUserPhoneNumber(userPhoneNumber);
        // 调用 Feign 客户端登录并获取 token
        String token = userServiceClient.login(loginRequest);
        
        // 从 token 中提取权限信息
        String userId = getUserIdFromToken(token);
        Set<String> permissions = extractPermissionsFromToken(token);

        // 将权限信息存储到 Redis 中
        storeUserPermissions(userId, permissions);
        
        return token;
    }

    public Set<String> getUserPermissions(String userId) {
        // 尝试从 Redis 中获取用户权限
        Object permissions = redisTemplate.opsForValue().get(PERMISSIONS_CACHE_PREFIX + userId);
        if (permissions instanceof Set<?>) {
            return castPermissionsToSet(permissions);
        }

        // 如果 Redis 中没有权限信息，则返回空集合
        return new HashSet<>();
    }

    public void storeUserPermissions(String userId, Set<String> permissions) {
        // 将权限信息存储到 Redis 中，并设置有效期
        redisTemplate.opsForValue().set(PERMISSIONS_CACHE_PREFIX + userId, new HashSet<>(permissions), 1, TimeUnit.HOURS);
    }

    @SuppressWarnings("unchecked")
    private Set<String> castPermissionsToSet(Object permissions) {
        if (permissions instanceof Set<?>) {
            Set<?> rawSet = (Set<?>) permissions;
            Set<String> stringSet = new HashSet<>();
            for (Object obj : rawSet) {
                if (obj instanceof String) {
                    stringSet.add((String) obj);
                }
            }
            return stringSet;
        }
        return new HashSet<>();
    }

    public Set<String> extractPermissionsFromToken(String token) {
        Set<String> permissions = new HashSet<>();
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String roles = (String) claims.get("roles");
            if (roles != null) {
                String[] rolesArray = roles.split(",");
                for (String role : rolesArray) {
                    permissions.add(role.trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid token");
        }
        return permissions;
    }

    private String getUserIdFromToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();  // 假设用户 ID 存储在 JWT 的 subject 中
        } catch (Exception e) {
            return null;
        }
    }
}



