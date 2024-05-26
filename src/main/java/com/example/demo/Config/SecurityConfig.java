package com.example.demo.Config;

import com.example.demo.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //用于获取用户权限。
    @Autowired
    private PermissionService permissionService;

    //配置安全过滤链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    //允许所有对匹配 /api/public/** 的请求无需认证。
                    .requestMatchers("/api/public/**").permitAll()
                    //所有其他请求都需要认证。
                    .anyRequest().authenticated()
                    //自定义访问控制逻辑
                    .access((authentication, request, configAttributes) -> {
                        String userId = authentication.getName();
                        String requestUri = request.getRequestURI();
                        Set<String> permissions = permissionService.getUserPermissions(userId);
                        //检查用户是否有权限访问请求的URI，如果有，允许访问；否则抛出 AccessDeniedException。
                        if (permissions != null && permissions.contains(requestUri)) {
                            return null; // 允许访问
                        }
                        throw new AccessDeniedException("Access Denied");
                    })
            )
            //配置JWT认证转换器。
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }
    

    //配置JWT认证转换器。
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        //创建一个 JwtGrantedAuthoritiesConverter 对象，并设置权限前缀为 "ROLE_"。
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        //创建一个 JwtAuthenticationConverter 对象，并设置JWT授予的权限转换器。
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
