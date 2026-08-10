package com.portfolio.fsm.user_service.mapper;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.models.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileResponse toResponse(UserProfile profile) {
        if (profile == null) {
            return null;
        }

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

    public UserProfile toEntity(UserProfileRequest request) {
        if (request == null) {
            return null;
        }

        UserProfile profile = new UserProfile();
        profile.setAuthUuid(request.authUuid());
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setAddressLine(request.addressLine());
        profile.setCity(request.city());
        profile.setCountry(request.country());

        return profile;
    }

    public void updateEntityFromRequest(UserProfileRequest request, UserProfile profile) {
        if (request == null || profile == null) {
            return;
        }

        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setAddressLine(request.addressLine());
        profile.setCity(request.city());
        profile.setCountry(request.country());
    }
}
