package com.portfolio.fsm.auth.services;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import com.portfolio.fsm.auth.models.User;
import com.portfolio.fsm.auth.services.TokenService;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(); 
        ReflectionTestUtils.setField(tokenService, "secret", "my-super-secret-key-for-testing");
    }

    @Test
    void testGenerateToken_ShouldReturnValidToken() {
        // Arrange
        User user = new User();
        user.setUsername("testadmin");

        // Act
        String token = tokenService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateToken_WithValidToken_ShouldReturnUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testadmin");
        String token = tokenService.generateToken(user);

        // Act
        String validatedUsername = tokenService.validateToken(token);

        // Assert
        assertEquals("testadmin", validatedUsername);
    }

    @Test
    void testValidateToken_WithInvalidToken_ShouldReturnEmptyString() {
        // Act
        String result = tokenService.validateToken("invalid.token.string");

        // Assert
        assertEquals("", result);
    }

    @Test
    void testGenerateTokenWithPermissions_ShouldIncludeClaims() {
        // Arrange
        String username = "tech_user";
        List<String> permissions = List.of("WORK_ORDER_READ", "WORK_ORDER_WRITE");
        String role = "TECHNICIAN";

        // Act
        String token = tokenService.generateTokenWithPermissions(username, permissions, role);
        List<String> extractedPermissions = tokenService.extractPermissionsFromToken(token);
        String validatedUsername = tokenService.validateToken(token);

        // Assert
        assertEquals("tech_user", validatedUsername);
        assertEquals(2, extractedPermissions.size());
        assertTrue(extractedPermissions.contains("WORK_ORDER_WRITE"));
    }

    @Test
    void testTokenBlacklist_ShouldInvalidateToken() {
        // Arrange
        User user = new User();
        user.setUsername("hacker");
        String token = tokenService.generateToken(user);

        // Act & Assert (Before Invalidation)
        assertTrue(tokenService.isTokenValid(token));

        // Act (Invalidate)
        tokenService.invalidateToken(token);

        // Assert (After Invalidation)
        assertFalse(tokenService.isTokenValid(token));
    }
}
