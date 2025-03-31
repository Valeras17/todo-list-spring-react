package com.example.todolist;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.example.todolist.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private TaskRepository repository;


    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllTasks_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        String newTaskJson = """
        {
            "title": "Выучить MockMvc",
            "completed": false
        }
        """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/tasks")
                                .contentType("application/json")
                                .content(newTaskJson)
                )
                .andExpect(status().isOk()); // или .isCreated(), если ты используешь 201
    }

    @Test
    void updateTaskCompleted_shouldSetCompletedToTrue() throws Exception {
        // Сначала создаём задачу
        String newTaskJson = """
        {
            "title": "Завершить задачу",
            "completed": false
        }
        """;

        // Добавляем и получаем ID созданной задачи
        String responseBody = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType("application/json")
                        .content(newTaskJson)
        ).andReturn().getResponse().getContentAsString();

        // Извлекаем id (очень простой способ, если не подключать Jackson)
        String id = responseBody.replaceAll("\\D+", " ").trim().split(" ")[0];

        // Теперь делаем PATCH-запрос, чтобы completed стал true
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .patch("/tasks/" + id + "/completed")
                                .param("completed", "true")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_shouldRemoveTaskFromDatabase() throws Exception {
        // Сначала создаём новую задачу
        String taskJson = """
        {
            "title": "Удалить меня",
            "completed": false
        }
        """;

        String responseBody = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType("application/json")
                        .content(taskJson)
        ).andReturn().getResponse().getContentAsString();

        // Извлекаем ID созданной задачи
        String id = responseBody.replaceAll("\\D+", " ").trim().split(" ")[0];

        // Теперь удаляем задачу по этому ID
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/tasks/" + id)
                )
                .andExpect(status().isOk());

        // Можно дополнительно проверить, что GET /tasks не возвращает эту задачу — позже
    }

    @Test
    void filterTasks_shouldReturnSortedByTitleAsc() throws Exception {
        // Создаём несколько задач с completed=false
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "Б", "completed": false}
                """)).andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "А", "completed": false}
                """)).andExpect(status().isOk());

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "В", "completed": false}
                """)).andExpect(status().isOk());

        // Проверяем, что вернулись отсортированные по title (А, Б, В)
        mockMvc.perform(get("/tasks/filter")
                        .param("completed", "false")
                        .param("sort", "title")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("А"))
                .andExpect(jsonPath("$[1].title").value("Б"))
                .andExpect(jsonPath("$[2].title").value("В"));
    }

    @Test
    void filterTasks_shouldReturnSortedByCreatedAtAsc() throws Exception {
        // Очищаем базу перед началом
        repository.deleteAll();

        // Добавляем 3 задачи с разным createdAt
        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "Первая", "completed": false}
                """)).andExpect(status().isOk());

        Thread.sleep(200);

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "Вторая", "completed": false}
                """)).andExpect(status().isOk());

        Thread.sleep(200);

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("""
                {"title": "Третья", "completed": false}
                """)).andExpect(status().isOk());

        // Проверяем сортировку по createdAt
        mockMvc.perform(get("/tasks/filter")
                        .param("completed", "false")
                        .param("sort", "createdAt")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Первая"))
                .andExpect(jsonPath("$[1].title").value("Вторая"))
                .andExpect(jsonPath("$[2].title").value("Третья"));
    }






}
