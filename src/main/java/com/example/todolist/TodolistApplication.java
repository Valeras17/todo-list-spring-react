package com.example.todolist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TaskRepository repository) {
		return (args) -> {
			repository.save(new Task("Купить хлеб"));
			repository.save(new Task("Написать ToDo List"));
			repository.save(new Task("Сделать кофе"));

			System.out.println("Задачи успешно добавлены в базу!");
		};
	}
}
