package ru.javarush.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.service.TaskService;

import java.util.List;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void test() {
        List<Task> tasks = taskService.getAll(1, 10);
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
