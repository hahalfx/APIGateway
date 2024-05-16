package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Feign.NotificationServiceClient;
import com.example.demo.Feign.UserServiceClient;

@Service
public class GatewayService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    public String getUserServiceData() {
        return userServiceClient.getUsers();
    }

    public String getNotificationServiceData() {
        return notificationServiceClient.getNotifications();
    }
}