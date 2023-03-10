package com.ewallet.userService.dto;

import com.ewallet.userService.validation.passwordValidation.Password;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @ApiModelProperty(notes = "Email", example = "bernard@gmail.com", required = true)
    @NotBlank(
            message = "Email is required"
    )
    @Email(
            message = "Email must be valid"
    )
    private String email;

    @ApiModelProperty(notes = "Password", example = "#Password123", required = true)
    @NotBlank(
            message = "PasswordAnnotation is required"
    )
    @Password
    private String password;
}
