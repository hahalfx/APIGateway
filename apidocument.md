# 这里记录了所有的api的url路径

## 网关

端口号：8001

### 用户登录注册

http://localhost:8001/auth

用户登录请求

POST http://localhost:8001/auth/login

用户注册请求

POST http://localhost:8001/auth/register

### 消息通告

http://localhost:8001/message

### 用户管理

http://localhost:8001/usermanage

添加用户信息

http://localhost:8001/usermanage/user/add

更新用户信息

http://localhost:8001/usermanage/user/update

删除用户信息

http://localhost:8001/usermanage/user/delete

通过用户ID来查找用户信息

http://localhost:8001/usermanage/user/findByUserId

查找所有用户信息

http://localhost:8001/usermanage/user/findAll

## 消息通告

端口：8089

### 文件管理

http://localhost:8089/files

文件上传

POST http://localhost:8089/files/upload

获取文件

GET http://localhost:8089/files/{flag}

删除文件

DELETE http://localhost:8089/files/{flag}

### 邮件发送

POST http://localhost:8089/sendEmail

### 消息控制

发送消息

POST http://localhost:8089/sendMessage

### 公告控制

新增公告

POST http://localhost:8089/notice/add

删除公告

DELETE http://localhost:8089/notice/delete/{id}

批量删除公告

DELETE http://localhost:8089/notice/delete/batch

修改公告

PUT http://localhost:8089/notice/update

根据id查询公告

GET http://localhost:8089/notice/selectById/{id}

查询所有公告

GET http://localhost:8089/notice/selectAll

分页查询

GET http://localhost:8089/notice/selectPage

### 短信控制

POST http://localhost:8089/sendSms

### 短信发送控制

POST http://localhost:8089/sendSmsPASTUSELESS

### 消息模版控制

新增消息模版





