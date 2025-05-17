package com.example.todoapp.integration;

import com.example.todoapp.TodoappApplication;
import com.example.todoapp.model.Task;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@SpringBootTest(classes = TodoappApplication.class) //för todoapp
@AutoConfigureMockMvc

class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskRepository.flush();
        userRepository.flush();

        User user = new User();
        user.setName("Ali");
        User savedUser = userRepository.save(user);

        Task task = new Task();
        task.setTitle("Do homework");
        task.setDescription("Write integration test");
        task.setDone(false);
        task.setUser(savedUser);
        taskRepository.save(task);
    }

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write integration test"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void getTaskById_shouldReturnCorrectTask() throws Exception {
        Task task = new Task();
        task.setTitle("Write code");
        task.setDescription("Test GET by ID");
        task.setDone(true);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTask.getId()))
                .andExpect(jsonPath("$.title").value("Write code"))
                .andExpect(jsonPath("$.description").value("Test GET by ID"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void createTask_shouldSaveAndReturnTask() throws Exception {
        User user = new User();
        user.setName("UserForCreate");
        User savedUser = userRepository.save(user);

        String json = """
                {
                    "title": "New Task",
                    "description": "Created via integration test",
                    "done": false,
                    "user": { "id": %s }
                }
                """.formatted(savedUser.getId());

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
        User user = new User();
        user.setName("Temporary User");
        User savedUser = userRepository.save(user);

        Task task = new Task();
        task.setTitle("Temporary task");
        task.setDescription("This task should be deleted");
        task.setDone(false);
        task.setUser(savedUser);
        task = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isNotFound());
    }
}
