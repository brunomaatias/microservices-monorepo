package com.portfolio.fsm.user_service.services;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.mapper.UserProfileMapper;
import com.portfolio.fsm.user_service.models.UserProfile;
import com.portfolio.fsm.user_service.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileRequest request) {
        if (userProfileRepository.findByAuthUuid(request.authUuid()).isPresent()) {
            throw new RuntimeException("Profile already exists for UUID: " + request.authUuid());
        }
        
        UserProfile profile = userProfileMapper.toEntity(request);
        UserProfile saved = userProfileRepository.save(profile);
        return userProfileMapper.toResponse(saved);
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUuid(request.authUuid())
                .orElseThrow(() -> new RuntimeException("Profile not found for UUID: " + request.authUuid()));

        userProfileMapper.updateEntityFromRequest(request, profile);

        UserProfile saved = userProfileRepository.save(profile);
        return userProfileMapper.toResponse(saved);
    }

    public UserProfileResponse getProfile(UUID authUuid) {
        UserProfile profile = userProfileRepository.findByAuthUuid(authUuid)
                .orElseThrow(() -> new RuntimeException("Profile not found for UUID: " + authUuid));
        return userProfileMapper.toResponse(profile);
    }
}
