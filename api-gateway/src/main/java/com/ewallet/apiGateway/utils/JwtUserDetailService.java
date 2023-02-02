package com.ewallet.apiGateway.utils;

import com.ewallet.apiGateway.dto.User;
import com.ewallet.apiGateway.feignInterface.UserInteface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtUserDetailService implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    UserInteface userInteface;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userInteface.findUserByEmail(username);
        if(user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private List<SimpleGrantedAuthority> authorities(User user) {
        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        user.getRoles().forEach(role -> {
            auths.add(new SimpleGrantedAuthority( "ROLE_"+ role.getName()));
        });

        return auths;
    }
}
