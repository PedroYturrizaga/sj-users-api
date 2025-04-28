package com.practica.usersapi.repository;

import com.practica.usersapi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}