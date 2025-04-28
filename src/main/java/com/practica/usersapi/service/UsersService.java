package com.practica.usersapi.service;

import com.practica.usersapi.dto.UsersRequestDTO;
import com.practica.usersapi.dto.UsersResponseDTO;
import com.practica.usersapi.exception.EmailAlreadyExistsException;
import com.practica.usersapi.mapper.UsersMapper;
import com.practica.usersapi.model.Users;
import com.practica.usersapi.repository.UsersRepository;
import com.practica.usersapi.security.EncryptionPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository userRepository;
    private final UsersMapper userMapper;

    @Autowired
    public UsersService(UsersRepository userRepository, UsersMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UsersResponseDTO createUser(UsersRequestDTO userRequestDTO) throws Exception {
        if (userRepository.findByEmail(userRequestDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }
        
        String encryptedPassword = EncryptionPassword.encrypt(userRequestDTO.getPassword());

        Users user = userMapper.toEntity(userRequestDTO);
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(encryptedPassword);
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(UUID.randomUUID().toString()); // Generar un token (UUID)
        user.setActive(true);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
    
    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UsersResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }
}