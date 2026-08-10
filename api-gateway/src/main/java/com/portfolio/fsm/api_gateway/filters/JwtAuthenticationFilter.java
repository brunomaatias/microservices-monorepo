package com.portfolio.fsm.api_gateway.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${api.security.token.secret}")
    private String secret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/login-device",
            "/auth/login-nfc"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isSecured(path)) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return sendErrorResponse(exchange.getResponse(), "Missing or Invalid Authorization Header");
            }

            String token = authHeader.substring(7);

            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .withIssuer("auth-api")
                        .build()
                        .verify(token);

                String username = decodedJWT.getSubject();
                String uuidUser = decodedJWT.getClaim("uuidUser").asString();
                String role = decodedJWT.getClaim("role").asString();
                List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);

                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Name", username != null ? username : "")
                        .header("X-User-Uuid", uuidUser != null ? uuidUser : "")
                        .header("X-User-Role", role != null ? role : "")
                        .header("X-User-Authorities", authorities != null ? String.join(",", authorities) : "")
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();
                return chain.filter(mutatedExchange);

            } catch (JWTVerificationException exception) {
                return sendErrorResponse(exchange.getResponse(), "Invalid or Expired JWT Token");
            }
        }

        return chain.filter(exchange);
    }

    private boolean isSecured(String path) {
        return OPEN_ENDPOINTS.stream().noneMatch(path::contains);
    }

    private Mono<Void> sendErrorResponse(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", java.time.LocalDateTime.now().toString());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorDetails.put("message", message);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorDetails);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
