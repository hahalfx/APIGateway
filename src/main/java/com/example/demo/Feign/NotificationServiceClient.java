package com.example.demo.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("notification-service")
public interface NotificationServiceClient {
    @GetMapping("/notifications")
    String getNotifications();
}