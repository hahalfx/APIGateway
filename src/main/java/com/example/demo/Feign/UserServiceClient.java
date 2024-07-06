package com.example.demo.Feign;
import com.example.demo.entity.User;
import com.example.demo.entity.UserWithBLOBs;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserServiceClient {
    @PostMapping("/user/add")
    boolean addUser(User user);

    @PutMapping("/user/update")
    boolean updateuSER(User user);

    @DeleteMapping("/user/delete")
    boolean delete(@RequestParam(value="userId",required=true)String _userId);

    @GetMapping("/user/details")
    User findByUserID(@RequestParam(value="userId",required=true)String _userId);

    @GetMapping("/user/getAll")
    List<UserWithBLOBs> findAll();

    @PostMapping("/user/phone/login/code/send")
    String login(@RequestBody LoginRequest loginRequest);

    class LoginRequest {
        private String userName;
        private String userPhoneNumber;

        // Getters and setters

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhoneNumber() {
            return userPhoneNumber;
        }

        public void setUserPhoneNumber(String userPhoneNumber) {
            this.userPhoneNumber = userPhoneNumber;
        }
    }

    
}