package com.portfolio.fsm.user_service.services;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.models.UserProfile;
import com.portfolio.fsm.user_service.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UUID authUuid;
    private UserProfileRequest request;
    private UserProfile existingProfile;

    @BeforeEach
    void setUp() {
        authUuid = UUID.randomUUID();
        request = new UserProfileRequest(
                authUuid,
                "John",
                "Doe",
                "123456789",
                "http://avatar.url/john",
                "123 Main St",
                "New York",
                "USA"
        );

        existingProfile = new UserProfile(
                1L,
                authUuid,
                "Jane",
                "Smith",
                "987654321",
                "http://avatar.url/jane",
                "456 Oak St",
                "Los Angeles",
                "USA"
        );
    }

    @Test
    void createProfile_whenProfileDoesNotExist_shouldCreateNewProfile() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile savedProfile = invocation.getArgument(0);
            savedProfile.setId(10L); // simulate auto-generation of ID
            return savedProfile;
        });

        // Act
        UserProfileResponse response = userProfileService.createProfile(request);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(authUuid, response.authUuid());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("123456789", response.phoneNumber());
        assertEquals("http://avatar.url/john", response.avatarUrl());
        assertEquals("123 Main St", response.addressLine());
        assertEquals("New York", response.city());
        assertEquals("USA", response.country());

        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void createProfile_whenProfileExists_shouldThrowException() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.of(existingProfile));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProfileService.createProfile(request);
        });

        assertEquals("Profile already exists for UUID: " + authUuid, exception.getMessage());
        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void updateProfile_whenProfileExists_shouldUpdateExistingProfile() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserProfileResponse response = userProfileService.updateProfile(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id()); // Preserves existing ID
        assertEquals(authUuid, response.authUuid());
        assertEquals("John", response.firstName()); // Updated fields
        assertEquals("Doe", response.lastName());
        assertEquals("123456789", response.phoneNumber());

        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
        verify(userProfileRepository, times(1)).save(existingProfile);
    }

    @Test
    void updateProfile_whenProfileDoesNotExist_shouldThrowException() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProfileService.updateProfile(request);
        });

        assertEquals("Profile not found for UUID: " + authUuid, exception.getMessage());
        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void getProfile_whenProfileExists_shouldReturnProfile() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.of(existingProfile));

        // Act
        UserProfileResponse response = userProfileService.getProfile(authUuid);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(authUuid, response.authUuid());
        assertEquals("Jane", response.firstName());
        assertEquals("Smith", response.lastName());

        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
    }

    @Test
    void getProfile_whenProfileDoesNotExist_shouldThrowException() {
        // Arrange
        when(userProfileRepository.findByAuthUuid(authUuid)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProfileService.getProfile(authUuid);
        });

        assertEquals("Profile not found for UUID: " + authUuid, exception.getMessage());
        verify(userProfileRepository, times(1)).findByAuthUuid(authUuid);
    }
}
