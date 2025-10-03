package com.example.todoapp.controller;

import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;    //  Används för att simulera HTTP-anrop

    //  Bygger MockMvc för att kunna testa våra endpoints
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Ali");
        user.setEmail("ali@example.com");

        //  Mockar så att getAllUsers() returnerar vår testanvändare
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())  // Förväntar status 200 OK
                .andExpect(jsonPath("$[0].name").value("Ali"))
                .andExpect(jsonPath("$[0].email").value("ali@example.com"));
    }
    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Ali");
        user.setEmail("ali@example.com");

        // Mockar så att create() returnerar vår user
        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")  //  Simulerar POST /api/users
                        .contentType(MediaType.APPLICATION_JSON)  // Anger att vi skickar JSON
                        .content("""
                {
                    "name": "Ali",
                    "email": "ali@example.com"
                }
            """))
                .andExpect(status().isOk())  //  Förväntar status 200 OK
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ali"))
                .andExpect(jsonPath("$.email").value("ali@example.com"));
    }

}
