package com.practica.usersapi.controller;

import com.practica.usersapi.dto.UsersRequestDTO;
import com.practica.usersapi.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import com.practica.usersapi.dto.UsersResponseDTO;
import com.practica.usersapi.exception.InvalidEmailFormatException;
import com.practica.usersapi.exception.InvalidPasswordFormatException;
import com.practica.usersapi.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService userService;
    private final Pattern passwordPattern;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Autowired
    public UsersController(UsersService userService, UsersRepository userRepository, Pattern passwordPattern) {
        this.userService = userService;
		this.passwordPattern = passwordPattern;
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Formato de correo o contraseña inválido",
            content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<UsersResponseDTO> createUser(@RequestBody UsersRequestDTO userRequestDTO) throws Exception {
        validateEmail(userRequestDTO.getEmail());
        validatePassword(userRequestDTO.getPassword());
    	UsersResponseDTO createdUser = userService.createUser(userRequestDTO);
	    try {
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	    } catch (Exception e) {
	    	createdUser.setError(e.getMessage().toString());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdUser);
	    }
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<UsersResponseDTO>> getAllUsers() {
    	List<UsersResponseDTO> users = userService.getAllUsers();
        try {
            return ResponseEntity.ok(users);
        } catch (Exception e) {
        	((UsersResponseDTO) users).setError(e.getMessage().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
