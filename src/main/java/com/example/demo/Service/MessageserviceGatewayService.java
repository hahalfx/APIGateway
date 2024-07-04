package com.example.demo.Service;


import com.example.demo.entity.User;
import com.example.demo.entity.UserWithBLOBs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Feign.MessageServiceClient;

@Service
public class MessageserviceGatewayService {
    @Autowired
    private MessageServiceClient messageServiceClient;

    

}
