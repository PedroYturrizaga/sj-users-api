package com.practica.usersapi.controller;

import com.practica.usersapi.dto.UsersRequestDTO;
import com.practica.usersapi.service.UsersService;
import com.practica.usersapi.dto.UsersResponseDTO;
import com.practica.usersapi.exception.InvalidEmailFormatException;
import com.practica.usersapi.exception.InvalidPasswordFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService userService;
    private final Pattern passwordPattern;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Autowired
    public UsersController(UsersService userService, Pattern passwordPattern) {
        this.userService = userService;
        this.passwordPattern = passwordPattern;
    }
    
    @PostMapping
    public ResponseEntity<UsersResponseDTO> createUser(@RequestBody UsersRequestDTO userRequestDTO) {
        validateEmail(userRequestDTO.getEmail());
        validatePassword(userRequestDTO.getPassword());
        UsersResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    private void validateEmail(String email) {
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new InvalidEmailFormatException("Formato de correo inválido");
        }
    }

    private void validatePassword(String password) {
        if (!passwordPattern.matcher(password).matches()) {
            throw new InvalidPasswordFormatException("Formato de contraseña inválido");
        }
    }
}
