package com.portfolio.fsm.user_service.services;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.models.UserProfile;
import com.portfolio.fsm.user_service.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfileResponse createOrUpdateProfile(UserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUuid(request.authUuid())
                .orElse(new UserProfile());

        profile.setAuthUuid(request.authUuid());
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setAddressLine(request.addressLine());
        profile.setCity(request.city());
        profile.setCountry(request.country());

        UserProfile saved = userProfileRepository.save(profile);
        return mapToResponse(saved);
    }

    public UserProfileResponse getProfile(UUID authUuid) {
        UserProfile profile = userProfileRepository.findByAuthUuid(authUuid)
                .orElseThrow(() -> new RuntimeException("Profile not found for UUID: " + authUuid));
        return mapToResponse(profile);
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getAuthUuid(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhoneNumber(),
                profile.getAvatarUrl(),
                profile.getAddressLine(),
                profile.getCity(),
                profile.getCountry()
        );
    }
}
