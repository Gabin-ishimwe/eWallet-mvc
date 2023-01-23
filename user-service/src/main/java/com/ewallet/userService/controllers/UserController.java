package com.ewallet.userService.controllers;

import com.ewallet.userService.dto.AuthResponseDto;
import com.ewallet.userService.dto.LoginDto;
import com.ewallet.userService.dto.RegisterDto;
import com.ewallet.userService.exceptions.NotFoundException;
import com.ewallet.userService.exceptions.UserAuthException;
import com.ewallet.userService.exceptions.UserExistsException;
import com.ewallet.userService.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authentication")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    @ApiOperation(
            value = "User register",
            notes = "User registration in our application",
            response = AuthResponseDto.class
    )
    public AuthResponseDto authRegister(@RequestBody @Valid RegisterDto registerDto) throws UserExistsException, NotFoundException {
        return userService.userRegister(registerDto);
    }

    @PostMapping(path = "/login")
    @ApiOperation(
            value = "User login",
            notes = "User login authentication in our application"
    )
    public AuthResponseDto authLogin(@RequestBody @Valid LoginDto loginDto) throws NotFoundException, UserAuthException {
        return userService.userLogin(loginDto);
    }

    @GetMapping(path = "/users")
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(
            value = "Get all users",
            notes = "Api to get all application user (only accessible to admins)"
    )
    public AuthResponseDto getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/profile")
//    @PreAuthorize("hasRole('USER')")
    @ApiOperation(
            value = "Get one user",
            notes = "Api to get user profile"
    )
    public AuthResponseDto getOneUser() throws NotFoundException {
        return userService.getOneUser();
    }
}
