package com.huang.cloudbase.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 打印请求和响应简要日志
 */
@Component
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (logger.isDebugEnabled()) {
            String requestUrl = exchange.getRequest().getURI().getRawPath();
            String traceId = exchange.getRequest().getHeaders().getFirst("_trace_id");
            String method = exchange.getRequest().getMethodValue();
            logger.debug("Request ===> Method:{} Host:{} Path:{} Query:{}, TraceId:{}",
                method,
                exchange.getRequest().getRemoteAddress(),
                requestUrl,
                exchange.getRequest().getQueryParams(),
                traceId);
            exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            if (logger.isDebugEnabled()) {
                String requestUrl = exchange.getRequest().getURI().getRawPath();
                String traceId = exchange.getRequest().getHeaders().getFirst("_trace_id");
                String method = exchange.getRequest().getMethodValue();
                Long startTime = exchange.getAttribute(START_TIME);
                long executeTime = 0L;
                if (startTime != null) {
                    executeTime = (System.currentTimeMillis() - startTime);
                }
                logger.debug("Response <=== Status:{} Method:{} Path:{} Time:{}ms TraceId:{}",
                    response.getRawStatusCode(), method, requestUrl, executeTime, traceId);
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
