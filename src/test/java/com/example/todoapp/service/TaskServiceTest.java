package com.example.todoapp.service;

import com.example.todoapp.model.Task;
import com.example.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Test
    void create_shouldSaveAndReturnTask() {
        // Arrange – skapa en mockad repository och en uppgift
        // Skapa och spara en Task
        TaskRepository mockRepo = mock(TaskRepository.class); // Skapar ett mockat TaskRepository
        TaskService taskService = new TaskService(mockRepo); // Skapar TaskService med mockRepo

        Task task = new Task();
        task.setTitle("Write unit test");
        task.setDescription("VG nivå");
        task.setDone(false);

        // Mockar att när save() anropas, returneras samma task
        when(mockRepo.save(task)).thenReturn(task);

        // Act – kör metoden
        Task result = taskService.create(task);

        // Assert – kontrollera resultatet
        assertNotNull(result);
        assertEquals("Write unit test", result.getTitle());
        assertEquals("VG nivå", result.getDescription());
        assertFalse(result.isDone());
        verify(mockRepo, times(1)).save(task);
        //  Kontrollerar att save() anropades en gång
    }
    @Test
    void getAllTasks_shouldReturnTaskList() {
        // Arrange – skapa ett mockat repository och en lista av uppgifter
        TaskRepository mockRepo = mock(TaskRepository.class);  // Skapar ett mockat TaskRepository
        TaskService taskService = new TaskService(mockRepo);  // Skapar TaskService med mockRepo

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setDone(false);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setDone(true);

        List<Task> taskList = List.of(task1, task2);  // Skapar en lista med två tasks
        when(mockRepo.findAll()).thenReturn(taskList); // Mockar så att findAll() returnerar vår lista

        // Act – kör metoden
        List<Task> result = taskService.getAllTasks(); // Anropar service-metoden

        // Assert – kontrollera resultatet
        assertNotNull(result); // VG-nivå: säkerställ att listan inte är null
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(mockRepo, times(1)).findAll(); // VG-nivå: verifiera exakt anrop
    }
    @Test
    void findById_shouldReturnTask_whenTaskExists() {
        // Arrange – skapa ett mockat repository och en uppgift
        TaskRepository mockRepo = mock(TaskRepository.class);
        TaskService taskService = new TaskService(mockRepo);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("VG uppgift");
        task.setDescription("Beskrivning för test");
        task.setDone(false);

        when(mockRepo.findById(1L)).thenReturn(Optional.of(task));

        // Act – kör metoden
        Optional<Task> result = taskService.findById(1L);

        // Assert – kontrollera att resultatet är korrekt
        assertTrue(result.isPresent()); // VG-nivå: säkerställ att värdet finns
        assertEquals("VG uppgift", result.get().getTitle());
        verify(mockRepo, times(1)).findById(1L); // VG-nivå: verifiera exakt anrop
    }
    @Test
    void getTaskById_shouldReturnCorrectTask() {
        // Arrange – skapa ett mockat repository och en uppgift
        TaskRepository mockRepo = mock(TaskRepository.class);
        TaskService taskService = new TaskService(mockRepo);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("VG Test");
        task.setDescription("Kontrollera ID");
        task.setDone(false);

        when(mockRepo.findById(1L)).thenReturn(Optional.of(task));

        // Act – kör metoden
        Optional<Task> result = taskService.findById(1L);

        // Assert – kontrollera resultatet
        assertTrue(result.isPresent());
        assertEquals("VG Test", result.get().getTitle());
        verify(mockRepo, times(1)).findById(1L); // findById ska anropas exakt en gång
    }



}
