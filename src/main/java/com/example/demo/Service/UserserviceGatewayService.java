package com.example.demo.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserWithBLOBs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Feign.UserServiceClient;

@Service
public class UserserviceGatewayService {

    @Autowired
    private UserServiceClient userServiceClient;

    public boolean addUser(User user){
        return userServiceClient.addUser(user);
    }

    public boolean updateuSER(User user) {
        return userServiceClient.updateuSER(user);
    }

    public boolean deleteUser(String _userid) {
        return userServiceClient.delete(_userid);
    }

    public User findByUserID(@RequestParam(value="userId",required=true)String _userId){
        return userServiceClient.findByUserID(_userId);
    }

    public List<UserWithBLOBs> findAll(){
        return userServiceClient.findAll();
    }
}