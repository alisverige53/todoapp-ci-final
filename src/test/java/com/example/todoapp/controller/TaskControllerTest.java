package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.User;
import com.example.todoapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    //  Bygger upp en MockMvc-instans för att kunna göra HTTP-anrop i testerna
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }


    //  Testar att hämta alla uppgifter (GET /api/tasks)
    // Förväntar att en lista av Tasks returneras som JSON med korrekta fält
    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        User user = new User();
        user.setId(1L);
        user.setName("Ali");
        task.setUser(user);

        List<Task> tasks = List.of(task);
        when(taskService.findAll()).thenReturn(tasks);
        // När findAll() anropas i TaskService ska den returnera vår testlista

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk())  //  Förväntar att status är 200 OK
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write tests"))
                .andExpect(jsonPath("$[0].done").value(false));
    }
    // live demo change

    //  Testar att skapa en ny uppgift (POST /api/tasks)
    // Förväntar att den skapade Task returneras med id, titel och beskrivning
    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("New Task");
        task.setDescription("Task Description");
        task.setDone(false);

        when(taskService.create(any(Task.class))).thenReturn(task);

        String json = new ObjectMapper().writeValueAsString(task);
        //  Konverterar task-objektet till JSON-sträng
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)   // Skickar med JSON som datatyp
                        .content(json))   // Innehållet är vår task i JSON-format
                .andExpect(status().isOk())   // Förväntar att status är 200 OK
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.done").value(task.isDone()));
    }
    //  Testar att hämta en uppgift via ID (GET /api/tasks/{id})
    // Förväntar att rätt Task returneras
    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Find Task");
        task.setDescription("Find this task by ID");
        task.setDone(false);

        when(taskService.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.done").value(task.isDone()));
    }
    //  Testar att hämta en uppgift som inte existerar (GET /api/tasks/{id})
    // Förväntar att status 404 Not Found returneras
    @Test
    void getTaskById_shouldReturnNotFound() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());   // Förväntar status 404 Not Found
    }


    // Testar att radera en uppgift (DELETE /api/tasks/{id})
    // Förväntar att uppgiften tas bort och status 200 OK returneras
    @Test
    void deleteTask_shouldDeleteTaskSuccessfully() throws Exception {
        Long taskId = 1L;  // Förväntar 200 OK

        mockMvc.perform(delete("/api/tasks/{id}", taskId))  //  Simulerar DELETE /api/tasks/1
                .andExpect(status().isOk());

        verify(taskService).delete(taskId);  //  Förväntar 200 OK
    }
}
