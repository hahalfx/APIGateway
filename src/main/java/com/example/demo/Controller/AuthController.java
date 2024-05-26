package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.Service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionService permissionService;

    private final String SECRET_KEY = "your-secret-key";

    /*处理登录请求 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            //进行身份认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 获取用户权限
            Set<String> permissions = permissionService.getUserPermissions(username);

            // 生成JWT令牌
            String token = generateJwtToken(authentication, permissions);

            //创建一个Map对象来存储令牌。
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            // 返回HTTP 200 OK响应，包含令牌
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // 处理身份验证异常，返回HTTP 401 Unauthorized响应
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    //该方法处理HTTP POST请求，并映射到/register URL路径。
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");

        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(password);

        return ResponseEntity.ok("Registration successful");
    }

    private String generateJwtToken(Authentication authentication, Set<String> permissions) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("permissions", permissions) // 将权限添加到令牌中
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000)) // 24小时有效期
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs") // 使用HS512算法和一个密钥签名
                .compact();
    }
}
