package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class PermissionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://USER-SERVICE/api/user/permissions?userId=";

    public void storeUserPermissions(String userId) {
        Set<String> permissions = fetchUserPermissionsFromService(userId);
        if (permissions != null) {
            redisTemplate.opsForValue().set(userId, new HashSet<>(permissions), 1, TimeUnit.HOURS);
        }
    }

    public Set<String> getUserPermissions(String userId) {
        // 尝试从Redis中获取用户权限
        Object permissions = redisTemplate.opsForValue().get(userId);
        if (permissions instanceof Set<?>) {
            return castPermissionsToSet(permissions);
        }

        // 如果Redis中没有权限信息，则从用户管理服务中获取
        Set<String> fetchedPermissions = fetchUserPermissionsFromService(userId);

        //将从用户管理服务中获取的权限信息存储到Redis中，并设置有效期
        if (fetchedPermissions != null) {
            redisTemplate.opsForValue().set(userId, new HashSet<>(fetchedPermissions), 1, TimeUnit.HOURS);
        }
        return fetchedPermissions != null ? fetchedPermissions : new HashSet<>();
    }

    @SuppressWarnings("unchecked")
    //从Redis中获取的权限信息进行类型转换，确保它是一个Set<String>类型的集合。
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

    //从用户管理服务中获取用户权限信息
    private Set<String> fetchUserPermissionsFromService(String userId) {
        String url = USER_SERVICE_URL + userId;
        return restTemplate.getForObject(url, Set.class);
    }
}


