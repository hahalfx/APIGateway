package com.example.demo.Config;

import com.example.demo.Security.RequiredPermission;
import com.example.demo.Service.PermissionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private PermissionService permissionService;

    private static final String SECRET_KEY = "your-256-bit-secret";

    //@Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .authorizeHttpRequests(authorizeRequests ->
    //             authorizeRequests
    //                 .requestMatchers("/api/public/**").permitAll()
    //                 .anyRequest().authenticated()
    //         )
    //         .addFilterBefore(new OncePerRequestFilter() {
    //             @Override
    //             protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //                 String authorizationHeader = request.getHeader("Authorization");
    //                 if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
    //                     String token = authorizationHeader.substring(7);
    //                     Set<String> permissions = extractPermissionsFromToken(token);
    //                     String requestUri = request.getRequestURI();

    //                     // 获取当前处理的方法
    //                     Object handler = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
    //                     if (handler instanceof HandlerMethod) {
    //                         HandlerMethod handlerMethod = (HandlerMethod) handler;
    //                         Method method = handlerMethod.getMethod();

    //                         // 检查方法上是否有 RequiredPermission 注解
    //                         if (method.isAnnotationPresent(RequiredPermission.class)) {
    //                             RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
    //                             String requiredPermissionValue = requiredPermission.value();
                                
    //                             // 验证用户是否拥有所需的权限
    //                             if (!permissions.contains(requiredPermissionValue)) {
    //                                 response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    //                                 return;
    //                             }
    //                         }
    //                     }
    //                 }
    //                 filterChain.doFilter(request, response);
    //             }
    //         }, UsernamePasswordAuthenticationFilter.class)
    //         .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
    //     return http.build();
    // }

    public Set<String> extractPermissionsFromToken(String token) {
        Set<String> permissions = new HashSet<>();
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String roles = (String) claims.get("roles");
            if (roles != null) {
                String[] rolesArray = roles.split(",");
                for (String role : rolesArray) {
                    permissions.add(role.trim());
                }
            }
        } catch (SignatureException e) {
            System.out.println("Invalid token");
        }
        return permissions;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}




