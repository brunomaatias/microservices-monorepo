package com.portfolio.fsm.api_gateway.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${api.security.token.secret}")
    private String secret;

    // List of public routes that don't require authentication
    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/login-device",
            "/auth/login-nfc"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. Check if the path is an open endpoint (like Login)
        if (isSecured(path)) {
            // 2. Extract Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header");
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization Header Format");
            }

            String token = authHeader.substring(7);

            // 3. Cryptographically Verify the JWT Token
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWT.require(algorithm)
                        .withIssuer("auth-api")
                        .build()
                        .verify(token);
                // If it doesn't throw an exception, the token is perfectly valid!
            } catch (JWTVerificationException exception) {
                return onError(exchange, "Invalid or Expired JWT Token");
            }
        }

        // 4. If valid (or if open endpoint), let the request pass through to the Microservice!
        return chain.filter(exchange);
    }

    private boolean isSecured(String path) {
        return OPEN_ENDPOINTS.stream().noneMatch(path::contains);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Run this filter before routing!
    }
}
