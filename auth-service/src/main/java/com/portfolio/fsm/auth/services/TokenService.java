package com.portfolio.fsm.auth.services;

import com.portfolio.fsm.auth.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    @Value("${api.security.token.secret:default-secret-key-for-dev}")
    private String secret;

    private final ConcurrentHashMap<String, Boolean> tokenBlacklist = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("uuidUser", user.getUuidUser().toString())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            logger.info("Token generated for user: {}", user.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            logger.error("Error generating token: ", exception);
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String generateTokenWithPermissions(String username, String uuidUser, List<String> permissions, String roleName) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(username)
                    .withClaim("uuidUser", uuidUser)
                    .withClaim("role", roleName)
                    .withClaim("authorities", permissions)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            logger.info("Definitive token generated for user: {} with {} permissions", username, permissions.size());
            return token;

        } catch (JWTCreationException exception) {
            logger.error("Error generating definitive token: ", exception);
            throw new RuntimeException("Error generating token with permissions", exception);
        }
    }

    public List<String> extractPermissionsFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            List<String> authorities = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getClaim("authorities")
                    .asList(String.class);

            return authorities != null ? authorities : new ArrayList<>();

        } catch (JWTVerificationException exception) {
            return new ArrayList<>();
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            logger.warn("Failed to validate token: {}", token);
            return ""; 
        }
    }

    public boolean isTokenValid(String token) {
        boolean isValid = !tokenBlacklist.containsKey(token);
        return isValid;
    }

    public void invalidateToken(String token) {
        tokenBlacklist.put(token, true);
        logger.info("Token invalidated: {}", token);
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
