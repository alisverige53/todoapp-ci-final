package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class TaskControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write tests"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        String json = new ObjectMapper().writeValueAsString(task);

        when(taskService.create(any(Task.class))).thenReturn(task);

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
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do homework");
        task.setDescription("Write tests");
        task.setDone(false);

        when(taskService.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Do homework"))
                .andExpect(jsonPath("$.description").value("Write tests"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getTask_shouldReturnNotFound() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_shouldDeleteSuccessfully() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isOk());

        verify(taskService).delete(taskId);
    }
}
