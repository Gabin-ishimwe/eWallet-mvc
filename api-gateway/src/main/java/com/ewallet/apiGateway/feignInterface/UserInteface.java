package com.ewallet.apiGateway.feignInterface;

import com.ewallet.apiGateway.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/api/v1/auth")
@Component
public interface UserInteface {
    @GetMapping("user")
    User findUserByEmail(@RequestParam String email);
}
