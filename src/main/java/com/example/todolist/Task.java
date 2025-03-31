package com.example.todolist;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data                   // геттеры, сеттеры, toString, equals, hashCode
@NoArgsConstructor      // пустой конструктор
@AllArgsConstructor     // конструктор со всеми полями
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Дополнительный конструктор для удобства
    public Task(String title) {
        this.title = title;
        this.completed = false;
    }
}
