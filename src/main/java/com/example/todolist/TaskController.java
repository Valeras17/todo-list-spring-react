package com.example.todolist;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    // Получить все задачи
    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return repository.findAll(Sort.by(direction, sort));
    }


    // Добавить новую задачу
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return repository.save(task);
    }

    // Удалить задачу по ID
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Обновить статус "выполнено" у задачи
    @PatchMapping("/{id}/completed")
    public Task updateTaskCompleted(@PathVariable Long id, @RequestParam boolean completed) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        task.setCompleted(completed);
        return repository.save(task);
    }

    @GetMapping("/filter")
    public List<Task> getFilteredTasks(
            @RequestParam boolean completed,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sort);
        return repository.findByCompleted(completed, sortObj);
    }





}
