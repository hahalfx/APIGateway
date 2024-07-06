package com.example.demo.Feign;

import com.example.demo.entity.*;
import com.example.demo.Common.Result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("message-service")
public interface MessageServiceClient {

    //从messagecontroller开始
    @PostMapping("/sendMessage")
    //发送消息
    ResponseEntity<String> sendMessage(@RequestBody MessageTask messageTask);

    //NoticeController
    //公告信息表操作
    @PostMapping("/notice/add")
    Result addnotice(@RequestBody Notice notice);

    @DeleteMapping("/notice/delete/{id}")
    Result deletenoticeById(@PathVariable Integer id);

    @DeleteMapping("/notice/delete/batch")
    Result deletenoticeBatch(@RequestBody List<Integer> ids);

    @PutMapping("/notice/update")
    Result updatenoticeById(@RequestBody Notice notice);

    @GetMapping("/notice/selectById/{id}")
    Result selectnoticeById(@PathVariable Integer id);

    @GetMapping("/notice/selectAll")
    Result selectAllnotice(Notice notice );

    @GetMapping("/notice/selectPage")
    Result selectnoticePage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize);

    //SMSController
    @PostMapping("/sendSms")
    ResponseEntity<String> sendSms(@RequestParam("mobile") String mobile);

    //SMSPastController
    @PostMapping("/sendSmsPASTUSELESS")
    ResponseEntity<String> sendSms();

    //TblMessageTpmController消息模版管理控制
    @PostMapping("/tblMessageTpm/add")
    Result addMessageTpm(@RequestBody TblMessageTpm tblMessageTpm);

    @DeleteMapping("/tblMessageTpm/delete/{id}")
    Result deleteMessageTpmById(@PathVariable Integer id);

    @DeleteMapping("/tblMessageTpm/delete/batch")
    Result deleteMessageTpmBatch(@RequestBody List<Integer> ids);

    @PutMapping("/tblMessageTpm/update")
    Result updateMessageTpmById(@RequestBody TblMessageTpm tblMessageTpm);

    @GetMapping("/tblMessageTpm/selectById/{id}")
    Result selectMessageTpmById(@PathVariable Integer id);

     @GetMapping("/tblMessageTpm/selectAll")
    Result selectallMessageTpm(TblMessageTpm tblMessageTpm);

    @GetMapping("/tblMessageTpm/selectPage")
    Result selectMessageTpmPage(TblMessageTpm tblMessageTpm, 
        @RequestParam(defaultValue = "1") Integer pageNum,  
        @RequestParam(defaultValue = "10") Integer pageSize);

}