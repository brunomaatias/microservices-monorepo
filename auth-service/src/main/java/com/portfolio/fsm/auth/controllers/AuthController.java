package com.portfolio.fsm.auth.controllers;

import com.portfolio.fsm.auth.dto.LoginRequest;
import com.portfolio.fsm.auth.dto.LoginResponseDto;
import com.portfolio.fsm.auth.dto.UserDto;
import com.portfolio.fsm.auth.models.Access;
import com.portfolio.fsm.auth.models.Permission;
import com.portfolio.fsm.auth.models.User;
import com.portfolio.fsm.auth.repositories.AccessRepository;
import com.portfolio.fsm.auth.repositories.UserRepository;
import com.portfolio.fsm.auth.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequest data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);
            var deviceId = data.deviceId();
            var nfc = data.nfc();

            if (deviceId != null && !deviceId.isEmpty()) {
                user.setDeviceId(deviceId);
                userRepository.save(user);
            }

            if (nfc != null && !nfc.isEmpty()) {
                user.setNfcId(nfc);
                userRepository.save(user);
            }

            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getUuidUser(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole() != null ? user.getRole().getName() : null
            );

            return ResponseEntity.ok(new LoginResponseDto(token, userDto));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/select-access")
    public ResponseEntity<LoginResponseDto> selectAccess(@RequestBody Long accessId) {
        Access access = accessRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Access not found or invalid"));

        List<String> permissions = access.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        String roleName = access.getUser().getRole() != null ? access.getUser().getRole().getName() : "";
        String definitiveToken = tokenService.generateTokenWithPermissions(
                access.getUser().getUsername(), access.getUser().getUuidUser().toString(), permissions, roleName);

        User user = access.getUser();
        UserDto userDto = new UserDto(
                user.getId(),
                user.getUuidUser(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                roleName
        );

        return ResponseEntity.ok(new LoginResponseDto(definitiveToken, userDto));
    }

    @PostMapping("/login-device")
    public ResponseEntity<?> loginWithDeviceId(@RequestBody HashMap<String, String> deviceData) {
        String deviceId = deviceData.get("deviceId");
        User user = userRepository.findByDeviceId(deviceId);

        if (user != null) {
            var token = tokenService.generateToken(user);
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getUuidUser(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole() != null ? user.getRole().getName() : null
            );
            return ResponseEntity.ok(new LoginResponseDto(token, userDto));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Device not registered.");
        }
    }

    @PostMapping("/login-nfc")
    public ResponseEntity<?> loginWithNfc(@RequestBody HashMap<String, String> nfcData) {
        String nfcId = nfcData.get("nfc");
        User user = userRepository.findByNfcId(nfcId);

        if (user != null) {
            var token = tokenService.generateToken(user);
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getUuidUser(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole() != null ? user.getRole().getName() : null
            );
            return ResponseEntity.ok(new LoginResponseDto(token, userDto));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NFC not registered.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Token not provided or invalid format.");
            }

            String token = authorizationHeader.substring(7);

            if (token.isEmpty()) {
                return ResponseEntity.badRequest().body("Empty token.");
            }

            tokenService.invalidateToken(token);

            return ResponseEntity.ok("Logout successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during logout.");
        }
    }
}
