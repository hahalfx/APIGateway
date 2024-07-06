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
│   │   │               ├── Common/
│   │   │               │   ├── enums/
│   │   │                   │   ├──ResultCodeEnum.java
│   │   │               │   ├── Result.java                    # 响应结果的模版
│   │   │               ├── Config/
│   │   │               │   ├── RestTemplateConfig.java        # RestTemplate配置
│   │   │               │   ├── JwtTokenFilter.java            # 一个自定义的过滤器
│   │   │               │   ├── SecurityConfig.java            # 安全配置
│   │   │               │   ├── RedisConfig.java               # Redis配置
│   │   │               ├── Controller/
│   │   │               │   ├── MessageServiceGatewayControl.java         # 处理发来给网关的消息服务请求
│   │   │               │   ├── UserServiceGatewayControl.java            # 处理发来给网关的用户管理服务请求
│   │   │               │   ├── AuthController.java            # 登录,注册处理逻辑
│   │   │               ├── entity/
│   │   │               │   ├── MessageTask.java               # 消息模版实体
│   │   │               │   ├── Notice.java                    # 消息通知实体
│   │   │               │   ├── User.java                      # 用户实体
│   │   │               │   ├── UserWithBLOBs.java             # 用户实体
│   │   │               ├── Feign/
│   │   │               │   ├── MessageServiceClient.java      # 与消息通告服务通信
│   │   │               │   ├── UserServiceClient.java         # 与用户管理服务通信
│   │   │               ├── Service/
│   │   │               │   ├── MessageserviceGatewayService.java          # 网关与消息通告服务交互的业务逻辑
│   │   │               │   ├── UserserviceGatewayService.java             # 网关与用户管理服务交互的业务逻辑
│   │   │               │   ├── PermissionService.java         # 鉴权服务的业务逻辑
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

# API网关实现的功能

设计图示例

```
[客户端]
    |
[API网关（鉴权、路由）]
    |                |
[用户管理服务]   [消息通告服务]
    |                |
[用户数据库]     [消息数据库]
```

1. **服务注册与发现**：使用Eureka管理微服务注册与发现。
2. **API网关**：使用Spring Cloud Gateway进行统一入口管理和鉴权。
3. **接口鉴权**：使用Spring Security和JWT实现用户认证和权限控制。
4. **微服务通信**：使用Feign Client实现微服务间的通信。

## 1. 路由配置（application.yaml）（未完全实现，需要各个微服务提供具体的接口地址）

API网关的主要任务是将客户端的请求路由到适当的微服务。

### 静态路由

静态路由预先定义好路径和对应的微服务。例如：

- `/api/user/**` 路由到用户管理服务
- `/api/notification/**` 路由到消息通告服务

### 动态路由

动态路由通过服务注册中心Eureka发现服务并配置路由。这样可以自动适应微服务的动态扩展和缩减。

## 2. 网关鉴权（AuthController.java）

​        用户的信息，角色，权限这些信息都存在用户管理微服务中，是在用户登录成功后，api网关会去用户管理服务中查询，看看用户直接关联的权限有哪些，用户所在分组的权限有哪些，将这些权限整合起来形成集合，再将集合存到Redis中。用户在访问api网关的时候会带着token，在Redis中一比较用户就知道用户的权限有哪些，并向其开放相应的微服务接口访问权限

### 设计概述

1. **用户登录**：用户登录成功后，API网关调用用户管理服务，获取用户的直接权限和分组权限，合并后存储到Redis。
2. **存储到Redis**：将用户的权限集合存储到Redis中，设置适当的过期时间。
3. **权限验证**：用户每次访问API网关时，带上JWT令牌，API网关从Redis中查询用户权限，并基于权限决定是否允许访问相应的微服务接口。

#### 1. 用户管理服务

**用户管理服务**需要提供一个API，用于根据用户ID查询用户的权限。

#### 2. API网关

在API网关中添加一个服务，用于处理用户登录后权限的获取和存储。

在用户成功登录后，API网关将会调用PermissionService.storeUserPermissions(userId)来查询用户的相应权限，然后基于查询到的用户权限创建JWT令牌供用户访问

#### 3. 查询用户权限（PermissionService.java）

网关会先在Redis中进行查询，如果没有找到用户相关的权限信息，则去用户管理服务中查询，并将返回的权限存储到Redis中

#### 4. 对应的用户权限只能访问对应的接口，根据jwt来判断(还没有完成)

确保所有请求都经过认证，并根据用户权限来控制访问。（SecurityConfig.java)

## 3.微服务发现集成

### 微服务注册中心

**使用Eureka注册和发现服务**。微服务启动时注册到服务注册中心，API网关从注册中心获取服务实例列表。

## 4. 与各个微服务进行通信（Feign/）（实现）

### 网关向微服务发送请求（GatewayService.java）

在服务类中注入Feign客户端，并使用它们来发送请求

为每个服务定义Feign客户端接口（NotificationServiceClient.java，UserServiceClient.java）

### 网关处理微服务请求（GatewayControl.java ）

## 5. SSL/TLS加密（未实现）

确保数据在传输过程中被加密，以防止窃听和数据篡改。配置SSL/TLS证书，强制使用HTTPS访问API网关。

## 6. 只有通过网关，并且包含有效JWT令牌的请求才能访问微服务

1. **登录过程**：用户通过 `AuthController` 的 `login` 方法进行登录，成功后返回一个JWT令牌。
2. **API网关转发**：API网关在转发请求时，通过 `JwtTokenFilter` 过滤器将JWT令牌添加到请求头中。
3. **微服务验证**：微服务接收到请求后，通过验证JWT令牌来确保请求是经过身份验证的。

# 需要各个微服务协作的地方

## 这些功能的实现需要最后与微服务进行调试，如果将以下代码添加进微服务后对微服务有影响，就先不用加进来先，最后再进行调试整合

## 1. 让在微服务中添加Eureka的客户端，让网关能够通过Eureka发现微服务并与微服务进行通信

### 步骤

#### 1. 添加依赖

在每个微服务的pom.xml中添加以下依赖：

```XML
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

#### 2. 配置Eureka客户端

在每个微服务的application.yml中添加Eureka客户端的配置：

```YAML
eureka:
  client:
    service-url:
      defaultZone: <http://localhost:8761/eureka/>
  instance:
    prefer-ip-address: true

spring:
  application:
    name: [微服务名称]

```

#### 3. 启动类

确保每个微服务的启动类上添加`@EnableEurekaClient`注解：

如用户管理类：

```java
package com.yourcompany.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

## 2. 为了网关与微服务进行通信，需要各个微服务有Feign的客户端

### 步骤

#### 1. 添加依赖

在pom.xml中添加Feign依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

#### 2. 启用Feign客户端

在Spring Boot应用程序的主类或配置类上启用Feign客户端：

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```



## 3. 确保各个微服务只能由API网关来进行访问，而不能有由其地址访问

在微服务中，使用JWT令牌进行安全性控制。API网关在转发请求时会附带一个特定的JWT令牌，微服务验证这个令牌，以确保请求是从API网关转发来的。

### 微服务中的配置

#### 添加依赖

在微服务的`pom.xml`中添加`jjwt`依赖：

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

#### 配置安全性

#### 文件路径：`SecurityConfig.java`

```java
package com.example.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .anyRequest().authenticated();
    }

    public class JwtTokenFilter extends OncePerRequestFilter {

        private final String SECRET_KEY = "your-secret-key";

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String token = header.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                // 验证通过，继续处理请求
                filterChain.doFilter(request, response);
            } catch (SignatureException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
```

#### 3. 配置应用

在微服务的`application.properties`或`application.yml`中添加JWT的密钥配置：

```java
jwt.secret=your-secret-key
```

1. 微服务配置
   - 在`SecurityConfig.java`中配置了Spring Security，禁用了CSRF保护，并添加了一个自定义的过滤器`JwtTokenFilter`。
   - `JwtTokenFilter`会检查请求头中的JWT令牌，并验证该令牌。如果令牌验证失败，返回401 Unauthorized响应。
