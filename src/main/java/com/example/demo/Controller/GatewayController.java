package com.example.demo.Controller;
import com.example.demo.entity.User;
import com.example.demo.entity.UserWithBLOBs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.GatewayService;

@RestController
@RequestMapping("gateway")
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @PostMapping("user/add")
    public boolean addUser(User user) {
        return gatewayService.addUser(user);
    }

    @PutMapping("user/update")
    public boolean updateuSER(User user) {
        return gatewayService.updateuSER(user);
    }

    @DeleteMapping("user/delete")
    public boolean deleteUser(String _userid) {
        return gatewayService.deleteUser(_userid);
    }

    @GetMapping("user/findByUserId")
    public User findByUserID(@RequestParam(value="userId",required=true)String _userId)  {
        return gatewayService.findByUserID(_userId);
    }

    @GetMapping("user/findAll")
    public List<UserWithBLOBs> findAll()   {
        return gatewayService.findAll();
     }

    @GetMapping("notifications")
    public String getNotifications() {
        return gatewayService.getNotificationServiceData();
    }

    @GetMapping("internal")
    public String handleInternalRequest() {
        // 处理来自微服务的请求
        return "Handled by Gateway";
    }
}
