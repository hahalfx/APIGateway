package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.GatewayService;

@RestController
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @GetMapping("/gateway/users")
    public String getUsers() {
        return gatewayService.getUserServiceData();
    }

    @GetMapping("/gateway/notifications")
    public String getNotifications() {
        return gatewayService.getNotificationServiceData();
    }

    @GetMapping("/gateway/internal")
    public String handleInternalRequest() {
        // 处理来自微服务的请求
        return "Handled by Gateway";
    }
}
