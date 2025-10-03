package com.example.todoapp.service;

import com.example.todoapp.model.User;
import com.example.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Test
    void create_shouldSaveAndReturnUser() {

        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);   // Mockar UserRepository
        UserService userService = new UserService(mockRepo);    // Skapar UserService med mockRepo

        User user = new User();
        user.setName("Ali");
        user.setEmail("ali@example.com");

        when(mockRepo.save(user)).thenReturn(user);   // Mockar så att save() returnerar user
        // Act
        // Act
        User result = userService.create(user);

        // Assert
        assertNotNull(result);  // VG-level: säkerställ att objektet inte är null
        assertEquals("Ali", result.getName());
        assertEquals("ali@example.com", result.getEmail());
        verify(mockRepo, times(1)).save(user); // VG-level: verifiera att repository anropas exakt en gång
    }

    @Test
    void findAll_shouldReturnAllUsers() {

        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        User user1 = new User();
        user1.setName("Ali");
        user1.setEmail("ali@example.com");

        User user2 = new User();
        user2.setName("Sara");
        user2.setEmail("sara@example.com");

        List<User> userList = List.of(user1, user2);

        when(mockRepo.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result); // VG-level
        assertEquals(2, result.size());
        assertEquals("Ali", result.get(0).getName());
        assertEquals("Sara", result.get(1).getName());
        verify(mockRepo, times(1)).findAll(); // VG-level: verify exact interaction
    }
    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Arrange – skapa en mockad repository och service
        UserRepository mockRepo = mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        User user = new User();
        user.setId(1L);
        user.setName("Ali");
        user.setEmail("ali@example.com");

        when(mockRepo.findById(1L)).thenReturn(Optional.of(user)); // Mockar findById(1)

        // Act
        Optional<User> result = userService.findById(1L);

        // Assert – kontrollera att resultatet är korrekt
        assertTrue(result.isPresent());
        assertEquals("Ali", result.get().getName());
        verify(mockRepo, times(1)).findById(1L);
    }
    @Test
    void findById_shouldReturnEmpty_whenUserNotFound() {

        // Arrange – mocka repo utan att ha user
        UserRepository mockRepo = mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);


        // Mockar att findById(99) returnerar tomt
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());// Mockar att findById(99) returnerar tomt

        // Act
        Optional<User> result = userService.findById(99L);

        // Assert
        assertFalse(result.isPresent());  // Resultatet ska vara tomt
        verify(mockRepo, times(1)).findById(99L); // Kontroll att findById(99) kördes 1 gång
    }
    }





