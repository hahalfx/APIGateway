package com.example.demo.Feign;

import com.example.demo.entity.*;
import com.example.demo.Common.Result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("message-service")
public interface MessageServiceClient {

    //从messagecontroller开始
    @PostMapping("/sendMessage")
    //发送消息
    ResponseEntity<String> sendMessage(@RequestBody MessageTask messageTask);

    //NoticeController
    //公告信息表操作
    @PostMapping("/notice/add")
    Result add(@RequestBody Notice notice);

    @DeleteMapping("/notice/delete/{id}")
    Result deleteById(@PathVariable Integer id);

    @DeleteMapping("/notice/delete/batch")
    Result deleteBatch(@RequestBody List<Integer> ids);

    @PutMapping("/notice/update")
    Result updateById(@RequestBody Notice notice);


}