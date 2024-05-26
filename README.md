# APIGateway

## 文件结构

```
APIGateway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── Config/
│   │   │               │   ├── AppConfig.java                 # App配置
│   │   │               │   ├── SecurityConfig.java            # 安全配置
│   │   │               │   ├── RedisConfig.java               # Redis配置
│   │   │               ├── Controller/
│   │   │               │   ├── GatewayControl.java            # 网关配置
│   │   │               │   ├── AuthController.java            # 登录,注册处理逻辑
│   │   │               ├── Feign/
│   │   │               │   ├── NotificationServiceClient.java # 与消息通告服务通信
│   │   │               │   ├── UserServiceClient.java         # 与用户管理服务通信
│   │   │               ├── Service/
│   │   │               │   ├── GatewayService.java
│   │   │               │   ├── PermissionService.java         # 权限存储服务
│   │   │               └── ApiGatewayApplication.java         # Spring Boot应用的入口类
│   │   └── resources/
│   │       ├── application.properties              
│   │       ├── application.yaml                      # 应用配置文件
│   │       ├── eureka-client.properties              # Eureka客户端的配置示例
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── demo/
├── pom.xml                                           # Maven项目对象模型文件
└── README.md
```

## 安全配置



## 网关鉴权的流程

​        用户的信息，角色，权限这些信息都存在用户管理微服务中，是在用户登录成功后，api网关去用户管理服务中查询，看看用户直接关联的权限有哪些，用户所在分组的权限有哪些，将这些权限整合起来形成集合，再将集合存到Redis中。用户在访问api网关的时候会带着token，在Redis中一比较用户就知道用户的权限有哪些，并向其开放相应的微服务接口访问权限

### 设计概述

1. **用户登录**：用户登录成功后，API网关调用用户管理服务，获取用户的直接权限和分组权限，合并后存储到Redis。
2. **存储到Redis**：将用户的权限集合存储到Redis中，设置适当的过期时间。
3. **权限验证**：用户每次访问API网关时，带上JWT令牌，API网关从Redis中查询用户权限，并基于权限决定是否允许访问相应的微服务接口。

#### 1. 用户管理服务

**用户管理服务**需要提供一个API，用于根据用户ID查询用户的权限。

#### 2. API网关

在API网关中添加一个服务，用于处理用户登录后权限的获取和存储。
