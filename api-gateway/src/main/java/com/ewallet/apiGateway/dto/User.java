package com.ewallet.apiGateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;


    private String contactNumber;
    private List<Role> roles;

}