package com.example.usermanagement.service;

import com.example.usermanagement.dto.JwtResponseDTO;
import com.example.usermanagement.dto.LoginRequestDTO;
import com.example.usermanagement.dto.UserRegistrationDTO;
import com.example.usermanagement.dto.UserDTO;

public interface AuthService {
    JwtResponseDTO login(LoginRequestDTO loginRequest);
    UserDTO register(UserRegistrationDTO registrationDTO);
} 