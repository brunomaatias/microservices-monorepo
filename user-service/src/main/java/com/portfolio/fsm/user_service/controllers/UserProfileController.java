package com.portfolio.fsm.user_service.controllers;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.portfolio.fsm.user_service.dto.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileResponse> createOrUpdate(
            @RequestBody UserProfileRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        
        UUID tokenUuid = UUID.fromString(principal.uuidUser());
        
        UserProfileRequest securedRequest = new UserProfileRequest(
                tokenUuid,
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.avatarUrl(),
                request.addressLine(),
                request.city(),
                request.country()
        );
        
        return ResponseEntity.ok(userProfileService.createOrUpdateProfile(securedRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        UUID tokenUuid = UUID.fromString(principal.uuidUser());
        return ResponseEntity.ok(userProfileService.getProfile(tokenUuid));
    }
    
    @GetMapping("/{authUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable UUID authUuid) {
        return ResponseEntity.ok(userProfileService.getProfile(authUuid));
    }
}
