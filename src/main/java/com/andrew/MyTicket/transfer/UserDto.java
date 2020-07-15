package com.andrew.MyTicket.transfer;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Password confirmation cannot be empty")
    private String password2;
    @NotEmpty(message = "Email cannot be empty")
    private String email;
}
