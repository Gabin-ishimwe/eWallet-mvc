package com.ewallet.userService.services;

import com.ewallet.userService.dto.AccountResponseDto;
import com.ewallet.userService.dto.AuthResponseDto;
import com.ewallet.userService.dto.LoginDto;
import com.ewallet.userService.dto.RegisterDto;
import com.ewallet.userService.entities.Role;
import com.ewallet.userService.entities.User;
import com.ewallet.userService.exceptions.NotFoundException;
import com.ewallet.userService.exceptions.UserAuthException;
import com.ewallet.userService.exceptions.UserExistsException;
import com.ewallet.userService.repositories.RoleRepository;
import com.ewallet.userService.repositories.UserRepository;
import com.ewallet.userService.utils.JwtUserDetailService;
import com.ewallet.userService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebClient.Builder webClient;

    @Transactional
    public AuthResponseDto userRegister(RegisterDto registerDto) throws UserExistsException, NotFoundException {
        User findUser = userRepository.findByEmail(registerDto.getEmail());
        List<Role> roles = new ArrayList<>();
        if (findUser != null) {
            throw new UserExistsException("User already exists");
        }
        Role userRole = roleRepository.findByName("USER");
        if(userRole == null) {
            throw new NotFoundException("Role not found");
        }
        roles.add(userRole);

        // create user instance
        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .contactNumber(registerDto.getContactNumber())
                .roles(roles)
//                .account(savedAccount)
                .build();

        // save user
        userRepository.save(user);

        // call account service with web client to register account
        AccountResponseDto accountResponseDto = webClient.build().post().uri("http://localhost:8082/api/v1/account")
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();

        System.out.println(accountResponseDto);
        return AuthResponseDto
                .builder()
                .message("User Registered Successfully")
                .build();
    }

    public AuthResponseDto createJwt(String message, User user) {
        String userEmail = user.getEmail();
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userEmail);
        String token = jwtUtil.generateToken(userDetails);
        return AuthResponseDto.builder()
                .message(message)
                .token(token)
                .build();

    }
    public AuthResponseDto userLogin(LoginDto loginDto) throws NotFoundException, UserAuthException {
        String userEmail = loginDto.getEmail();
        String password = loginDto.getPassword();
        User findUser = userRepository.findByEmail(userEmail);
        if(findUser != null) {
            boolean passwordVerification = passwordEncoder.matches(password, findUser.getPassword());
            if(passwordVerification) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
                return createJwt("User Logged in Successfully", findUser);
            } else {
                throw new UserAuthException("Invalid Credential, Try again");
            }
        }else {
            throw new UserAuthException("Invalid Credential, Try again");
        }
    }

    public AuthResponseDto getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return AuthResponseDto.builder()
                .message("All users")
                .users(allUsers)
                .build();
    }

    public AuthResponseDto getOneUser() throws NotFoundException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        return AuthResponseDto.builder()
                .message("User profile")
                .user(findUser)
                .build();
    }
}
