package com.andrew.MyTicket.dto;

import com.andrew.MyTicket.model.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

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
