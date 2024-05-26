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

    public void storeUserPermissions(String userId) {
        String url = "http://USER-SERVICE/api/user/permissions?userId=" + userId;
        Set<String> permissions = restTemplate.getForObject(url, Set.class);
        if (permissions != null) {
            redisTemplate.opsForValue().set(userId, new HashSet<>(permissions), 1, TimeUnit.HOURS);
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> getUserPermissions(String userId) {
        Object permissions = redisTemplate.opsForValue().get(userId);
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
}

