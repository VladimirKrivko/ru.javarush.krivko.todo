package ru.javarush.todolist;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javarush.todolist.config.WebConfig;
import ru.javarush.todolist.controller.TaskController;

public class TestGetBean {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);
        TaskController taskController = applicationContext.getBean(TaskController.class);
        taskController.test();
    }
}
