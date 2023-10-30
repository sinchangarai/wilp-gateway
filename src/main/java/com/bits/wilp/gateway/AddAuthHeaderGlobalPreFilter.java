package com.bits.wilp.gateway;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AddAuthHeaderGlobalPreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> jwtToken = headers.get("Authorization");
        ServerWebExchange mutatedExchange;


        if(jwtToken != null && jwtToken.size() > 0) {
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("Authorization", "Bearer " + jwtToken != null ? jwtToken.get(0) : "")
                    .build();

            mutatedExchange = exchange.mutate().request(request).build();
        } else {
            mutatedExchange = exchange.mutate().build();
        }

        return chain.filter(mutatedExchange);
    }
}