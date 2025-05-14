package com.example.todoapp.integration;

import com.example.todoapp.model.Task;
import com.example.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        Task task = new Task();
        task.setTitle("Do homework");
        task.setDescription("Write integration test");
        task.setDone(false);
        taskRepository.save(task);
    }
    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write integration test"))
                .andExpect(jsonPath("$[0].done").value(false));
    }
    @Test
    void getTaskById_shouldReturnCorrectTask() throws Exception {
        // Arrange
        Task task = new Task();
        task.setTitle("Write code");
        task.setDescription("Test GET by ID");
        task.setDone(true);
        Task savedTask = taskRepository.save(task);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTask.getId()))
                .andExpect(jsonPath("$.title").value("Write code"))
                .andExpect(jsonPath("$.description").value("Test GET by ID"))
                .andExpect(jsonPath("$.done").value(true));
    }
    @Test
    void createTask_shouldSaveAndReturnTask() throws Exception {
        // Arrange
        String json = """
        {
            "title": "New Task",
            "description": "Created via integration test",
            "done": false
        }
        """;

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Created via integration test"))
                .andExpect(jsonPath("$.done").value(false));
    }
    @Test
    void deleteTask_shouldRemoveTask() throws Exception {
        // Arrange – skapa och spara en uppgift i databasen
        Task task = new Task();
        task.setTitle("Tillfällig uppgift");
        task.setDescription("Den ska tas bort");
        task.setDone(false);
        task = taskRepository.save(task);

        // Act & Assert – ta bort uppgiften och kontrollera att den inte längre finns
        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isNotFound());
    }




}
