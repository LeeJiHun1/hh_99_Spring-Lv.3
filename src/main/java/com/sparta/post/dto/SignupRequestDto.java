package com.sparta.post.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Size(min=4, max=10)
    @Pattern(regexp = "(^[0-9]+[a-z]+$)")
    private String username;

    @Size(min=8, max=15)
    @Pattern(regexp = "(^[0-9]+[a-zA-Z]+[`~!@#$%^&*()-_=+\\|{};:'\",.<>/?]+$)")
    private String password;

    private boolean admin = false;
    private String adminToken ="";

}
