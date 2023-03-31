package ru.javarush.todolist;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javarush.todolist.config.ServiceConfig;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.service.TaskService;

import java.util.List;

public class TestGetBean {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServiceConfig.class);
        TaskService taskService = applicationContext.getBean(TaskService.class);
        List<Task> tasks = taskService.getAll(0, 10);
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
