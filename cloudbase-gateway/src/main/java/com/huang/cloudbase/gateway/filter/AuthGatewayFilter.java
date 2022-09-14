package com.huang.cloudbase.gateway.filter;

import com.huang.cloudbase.gateway.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author huangjunbiao_cdv
 */
@Component
public class AuthGatewayFilter implements GatewayFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AuthGatewayFilterFactory.Config config;

    public AuthGatewayFilter(AuthGatewayFilterFactory.Config config) {
        this.config = config;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String first = exchange.getRequest().getHeaders().getFirst("Cookie");
        this.logger.info("request:{}", first);
        if ("_jwt=123".equals(first)) {
            ServerHttpRequest request = exchange.getRequest().mutate()
                .header("userId", "user1")//User#id
                .header("jwt_id", "jwtid123")//JwtToken#id
                .build();
            return chain.filter(exchange.mutate().request(request).build());
        } else {
            throw new AuthenticationException(401, "不合法");
        }
    }
}
