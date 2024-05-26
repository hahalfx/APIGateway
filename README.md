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
│   │   │               ├── Controller/
│   │   │               │   ├── GatewayControl.java            # 网关配置
│   │   │               ├── Feign/
│   │   │               │   ├── NotificationServiceClient.java # 与消息通告服务通信
│   │   │               │   ├── UserServiceClient.java         # 与用户管理服务通信
│   │   │               ├── Service/
│   │   │               │   ├── GatewayService.java            
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

