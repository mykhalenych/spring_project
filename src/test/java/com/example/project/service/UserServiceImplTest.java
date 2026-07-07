package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.project.dto.UserRegistrationRequestDto;
import com.example.project.dto.UserResponseDto;
import com.example.project.exception.RegistrationException;
import com.example.project.mapper.UserMapper;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_ValidRequest_ReturnsResponseDto() throws RegistrationException {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRepeatPassword("password");
        request.setFirstName("First");
        request.setLastName("Last");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toModel(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = userService.register(request);

        assertEquals(responseDto, actual);
    }

    @Test
    void register_DuplicateEmail_ThrowsRegistrationException() {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(request));
    }
}
