package com.authservice.service;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public APIResponse<String> register(UserDto userDto) {

        APIResponse<String> response = new APIResponse<>();

        // Check whether username already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            response.setMessage("Registration Failed");
            response.setStatus(500);
            response.setData("User with Username already exists");
            return response;
        }

        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        // Copy properties from DTO to entity
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(encryptedPassword); // Overwrite password with encoded one
        user.setRole("ROLE_USER"); // Set default role

        // Save the user
        userRepository.save(user);

        // Return success response
        response.setMessage("Registration Completed");
        response.setStatus(201);
        response.setData("User has been registered successfully");
        return response;
    }
}