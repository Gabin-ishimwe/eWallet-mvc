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
public class RegisterDto {
    @ApiModelProperty(notes = "First name", example = "Mark", required = true)
    @NotBlank(
            message = "First name is required"
    )
    private String firstName;

    @ApiModelProperty(notes = "Last name", example = "Bernard", required = true)
    @NotBlank(
            message = "Last name is required"
    )
    private String lastName;

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

    @ApiModelProperty(notes = "Contact number", example = "0787857036", required = true)
    @NotBlank(
            message = "Contact number is required"
    )
    private String ContactNumber;
}
