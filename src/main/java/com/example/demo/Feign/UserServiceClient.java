package com.example.demo.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users")
    String getUsers();
}