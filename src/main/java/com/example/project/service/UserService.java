package com.example.project.service;

import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
