package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.service.TaskService;
import com.example.todoapp.service.TaskServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;


@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        // Arrange – skapa en uppgift att returnera från tjänsten
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        // Mocka tjänstbeteendet: när findAll() anropas, returnera listan
        Mockito.when(taskService.getAllTasks()).thenReturn(List.of(task));



        // Act & Assert – skicka GET och kontrollera JSON-responsen
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write tests"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        // Arrange – skapa en ny uppgift och konvertera till JSON
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        String json = new ObjectMapper().writeValueAsString(task);

        // Mocka create() för att returnera samma objekt
        Mockito.when(taskService.create(Mockito.any(Task.class))).thenReturn(task);

        // Act & Assert – skicka POST med JSON och kontrollera svaret
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Do homework"))
                .andExpect(jsonPath("$.description").value("Write tests"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getTask_shouldReturnTaskById() throws Exception {
        // Arrange – mocka ett specifikt objekt vid ID=1
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert – kontrollera GET /api/tasks/1 svarar korrekt
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Do homework"))
                .andExpect(jsonPath("$.description").value("Write tests"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getTask_shouldReturnNotFound() throws Exception {
        // Arrange – mocka att ID 99 inte finns
        Mockito.when(taskService.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert – kontrollera att status blir 404
        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_shouldDeleteSuccessfully() throws Exception {
        // Arrange – sätt ett exempel-ID
        Long taskId = 1L;

        // Act & Assert – kontrollera att DELETE fungerar och metoden anropas
        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isOk());

        Mockito.verify(taskService).delete(taskId);
    }

}
