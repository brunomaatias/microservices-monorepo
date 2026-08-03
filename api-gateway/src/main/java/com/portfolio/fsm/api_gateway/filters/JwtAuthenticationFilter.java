package com.portfolio.fsm.api_gateway.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequestWrapper;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/login-device",
            "/auth/login-nfc"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isSecured(path)) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Missing or Invalid Authorization Header");
                return;
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

                // Create a request wrapper to add custom headers for downstream microservices
                HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                    private final Map<String, String> customHeaders = new HashMap<>();
                    
                    {
                        if (username != null) customHeaders.put("X-User-Name", username);
                        if (uuidUser != null) customHeaders.put("X-User-Uuid", uuidUser);
                        if (role != null) customHeaders.put("X-User-Role", role);
                        if (authorities != null && !authorities.isEmpty()) {
                            customHeaders.put("X-User-Authorities", String.join(",", authorities));
                        }
                    }

                    @Override
                    public String getHeader(String name) {
                        if (customHeaders.containsKey(name)) {
                            return customHeaders.get(name);
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (customHeaders.containsKey(name)) {
                            return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> names = Collections.list(super.getHeaderNames());
                        names.addAll(customHeaders.keySet());
                        return Collections.enumeration(names);
                    }
                };
                
                filterChain.doFilter(requestWrapper, response);
                return;
                
            } catch (JWTVerificationException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or Expired JWT Token");
                return;
            }
        }

        // Token is valid (or endpoint is open), let request proceed
        filterChain.doFilter(request, response);
    }

    private boolean isSecured(String path) {
        return OPEN_ENDPOINTS.stream().noneMatch(path::contains);
    }
}
