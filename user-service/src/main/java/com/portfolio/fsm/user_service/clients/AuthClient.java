package com.portfolio.fsm.user_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;

@FeignClient(name = "auth-service")
public interface AuthClient {
 
    @PostMapping("/auth/login")
    Object loginWithDeviceId(@RequestBody HashMap<String, String> deviceData);

}
