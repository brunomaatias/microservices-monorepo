package com.portfolio.fsm.user_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.fsm.user_service.dto.UserProfileRequest;
import com.portfolio.fsm.user_service.dto.UserProfileResponse;
import com.portfolio.fsm.user_service.services.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filters for this slice test
@ActiveProfiles("test")
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID authUuid;
    private UserProfileRequest request;
    private UserProfileResponse response;

    @BeforeEach
    void setUp() {
        authUuid = UUID.randomUUID();
        
        com.portfolio.fsm.user_service.dto.UserPrincipal mockPrincipal = 
                new com.portfolio.fsm.user_service.dto.UserPrincipal("testuser", authUuid.toString());
                
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
            .standaloneSetup(new UserProfileController(userProfileService))
            .setCustomArgumentResolvers(new org.springframework.web.method.support.HandlerMethodArgumentResolver() {
                @Override
                public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
                    return parameter.getParameterType().isAssignableFrom(com.portfolio.fsm.user_service.dto.UserPrincipal.class);
                }
                @Override
                public Object resolveArgument(org.springframework.core.MethodParameter parameter, 
                                              org.springframework.web.method.support.ModelAndViewContainer mavContainer, 
                                              org.springframework.web.context.request.NativeWebRequest webRequest, 
                                              org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                    return mockPrincipal;
                }
            })
            .build();
            
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

        response = new UserProfileResponse(
                1L,
                authUuid,
                "John",
                "Doe",
                "123456789",
                "http://avatar.url/john",
                "123 Main St",
                "New York",
                "USA"
        );
    }

    // Creating a mock user principal for testing
    // To properly test this, we should use @WithMockUser or mock the AuthenticationPrincipal.
    // Given the current test setup, let's keep it simple. If we need to mock it properly, we can use SecurityMockMvcRequestPostProcessors.
    // For now, let's mock it using SecurityMockMvcRequestPostProcessors.user if needed, but since filters are disabled, it might just be null.
    // I will use RequestPostProcessor to inject a UserPrincipal.
    
    @Test
    void createProfile_shouldReturnOkAndResponse() throws Exception {
        // Arrange
        when(userProfileService.createProfile(any(UserProfileRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUuid").value(authUuid.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.city").value("New York"));
    }

    @Test
    void updateProfile_shouldReturnOkAndResponse() throws Exception {
        // Arrange
        when(userProfileService.updateProfile(any(UserProfileRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUuid").value(authUuid.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.city").value("New York"));
    }

    @Test
    void getProfile_whenProfileExists_shouldReturnOkAndResponse() throws Exception {
        // Arrange
        when(userProfileService.getProfile(authUuid)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/users/profile/{authUuid}", authUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.authUuid").value(authUuid.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
