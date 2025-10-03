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
// Anger att testet använder en separat properties-fil för testdatabasen
@TestPropertySource(locations = "classpath:application-test.properties")

// Aktiverar test-profilen
@ActiveProfiles("test")

// Startar hela Spring Boot-applikationen för integrationstest
@SpringBootTest(classes = TodoappApplication.class) //för todoapp

// Gör MockMvc tillgänglig för HTTP-anrop i testerna
@AutoConfigureMockMvc

class TaskIntegrationTest {

    // MockMvc för att simulera HTTP-anrop
    @Autowired
    private MockMvc mockMvc;

    // Riktiga TaskRepository kopplat till H2-databas
    @Autowired
    private TaskRepository taskRepository;

    //  Riktiga UserRepository kopplat till H2-databas
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("Ali");
        User savedUser = userRepository.save(user);  // Sparar en riktig användare i databasen

        Task task = new Task();
        task.setTitle("Do homework");
        task.setDescription("Write integration test");
        task.setDone(false);
        task.setUser(savedUser);
        taskRepository.save(task);  // Sparar en riktig Task i databasen
    }

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())  //  Förväntar 200 OK
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Do homework"))
                .andExpect(jsonPath("$[0].description").value("Write integration test"))
                .andExpect(jsonPath("$[0].done").value(false)); //  Status ska vara false
    }

    @Test
    void getTaskById_shouldReturnCorrectTask() throws Exception {
        Task task = new Task();
        task.setTitle("Write code");
        task.setDescription("Test GET by ID");
        task.setDone(true);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))  //  GET med id
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
        User savedUser = userRepository.save(user);   // Sparar användare i databasen

        String json = """
                {
                    "title": "New Task",
                    "description": "Created via integration test",
                    "done": false,
                    "user": { "id": %s }
                }
                """.formatted(savedUser.getId());  // JSON-body för ny task, kopplad till sparad user

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())   //  id ska genereras
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Created via integration test"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void deleteTask_shouldRemoveTask() throws Exception {
        User user = new User();
        user.setName("Temporary User");
        User savedUser = userRepository.save(user);  // Sparar användaren i databasen (H2)

        Task task = new Task();
        task.setTitle("Temporary task");
        task.setDescription("This task should be deleted");
        task.setDone(false);
        task.setUser(savedUser);
        task = taskRepository.save(task);


        //  Simulerar DELETE-anrop till /api/tasks/{id}
        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk()); //  Förväntar att status = 200 OK (lyckad radering)


        //  Efter radering försöker vi hämta samma task
        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isNotFound());  //  Förväntar 404 Not Found → visar att tasken verkligen är borttagen
    }
}
