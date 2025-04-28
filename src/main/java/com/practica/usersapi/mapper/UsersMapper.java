package com.practica.usersapi.mapper;

import com.practica.usersapi.dto.UsersRequestDTO;
import com.practica.usersapi.dto.UsersResponseDTO;
import com.practica.usersapi.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "active", ignore = true)
    Users toEntity(UsersRequestDTO userRequestDTO);

    UsersResponseDTO toDto(Users user);
}