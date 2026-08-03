package com.portfolio.fsm.user_service.controllers;

import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileResponse> createOrUpdate(@RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(userProfileService.createOrUpdateProfile(request));
    }

    @GetMapping("/{authUuid}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable UUID authUuid) {
        return ResponseEntity.ok(userProfileService.getProfile(authUuid));
    }
}
