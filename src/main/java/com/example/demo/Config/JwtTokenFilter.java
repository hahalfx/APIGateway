package com.example.demo.Config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenFilter extends AbstractGatewayFilterFactory<JwtTokenFilter.Config> {

    public JwtTokenFilter() {
        super(Config.class);
    }

    //定义过滤器的行为，接受一个 Config 类型的参数，并返回一个 GatewayFilter 对象
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                exchange.getRequest().mutate().header("Authorization", token).build();
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 配置类
    }
}

