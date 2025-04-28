package com.practica.usersapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practica.usersapi.dto.PhoneDTO;
import com.practica.usersapi.dto.UsersRequestDTO;
import com.practica.usersapi.dto.UsersResponseDTO;
import com.practica.usersapi.exception.EmailAlreadyExistsException;
import com.practica.usersapi.service.UsersService;

public class UsersControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UsersService userService;
    @Mock
    private Pattern passwordPattern;  // Mock del patrón de la contraseña
    @InjectMocks
    private UsersController userController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        // Arrange
        UsersRequestDTO userRequestDTO = new UsersRequestDTO();
        userRequestDTO.setName("Juan Rodriguez");
        userRequestDTO.setEmail("juan@rodriguez.org");
        userRequestDTO.setPassword("Test1234");
        List<PhoneDTO> phones = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("1234567");
        phoneDTO.setCitycode("1");
        phoneDTO.setCountrycode("57");
        phones.add(phoneDTO);
        userRequestDTO.setPhones(phones);

        UsersResponseDTO userResponseDTO = new UsersResponseDTO();
        userResponseDTO.setId(UUID.randomUUID());
        userResponseDTO.setName(userRequestDTO.getName());
        userResponseDTO.setEmail(userRequestDTO.getEmail());
        userResponseDTO.setPhones(userRequestDTO.getPhones());
        userResponseDTO.setActive(true);

        Matcher matcherMock = org.mockito.Mockito.mock(Matcher.class);
        when(passwordPattern.matcher(any())).thenReturn(matcherMock); 
        when(matcherMock.matches()).thenReturn(true); 
        when(userService.createUser(any())).thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userRequestDTO.getName()))
                .andExpect(jsonPath("$.email").value(userRequestDTO.getEmail()));
    }

    @Test
    public void testCreateUser_InvalidEmail() throws Exception {
        // Arrange
        UsersRequestDTO userRequestDTO = new UsersRequestDTO();
        userRequestDTO.setName("Juan Rodriguez");
        userRequestDTO.setEmail("invalid-email");
        userRequestDTO.setPassword("Test1234");
        List<PhoneDTO> phones = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("1234567");
        phoneDTO.setCitycode("1");
        phoneDTO.setCountrycode("57");
        phones.add(phoneDTO);
        userRequestDTO.setPhones(phones);

        Matcher matcherMock = org.mockito.Mockito.mock(Matcher.class);
        when(passwordPattern.matcher(any())).thenReturn(matcherMock); 
        when(matcherMock.matches()).thenReturn(true); 
        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de correo inválido"));
    }

    @Test
    public void testCreateUser_InvalidPassword() throws Exception {
        // Arrange
        UsersRequestDTO userRequestDTO = new UsersRequestDTO();
        userRequestDTO.setName("Juan Rodriguez");
        userRequestDTO.setEmail("juan@rodriguez.org");
        userRequestDTO.setPassword("invalid");
        List<PhoneDTO> phones = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("1234567");
        phoneDTO.setCitycode("1");
        phoneDTO.setCountrycode("57");
        phones.add(phoneDTO);
        userRequestDTO.setPhones(phones);

        Matcher matcherMock = org.mockito.Mockito.mock(Matcher.class);
        when(passwordPattern.matcher(any())).thenReturn(matcherMock); 
        when(matcherMock.matches()).thenReturn(true); 

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de contraseña inválido"));
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() throws Exception {
        // Arrange
        UsersRequestDTO userRequestDTO = new UsersRequestDTO();
        userRequestDTO.setName("Juan Rodriguez");
        userRequestDTO.setEmail("juan@rodriguez.org");
        userRequestDTO.setPassword("Test1234");
        List<PhoneDTO> phones = new ArrayList<>();
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("1234567");
        phoneDTO.setCitycode("1");
        phoneDTO.setCountrycode("57");
        phones.add(phoneDTO);
        userRequestDTO.setPhones(phones);

        Matcher matcherMock = org.mockito.Mockito.mock(Matcher.class);
        when(passwordPattern.matcher(any())).thenReturn(matcherMock); 
        when(matcherMock.matches()).thenReturn(true); 
        // when(passwordPattern.matcher(any())).thenReturn(true);
        when(userService.createUser(any())).thenThrow(new EmailAlreadyExistsException("El correo ya está registrado"));

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
    }
}