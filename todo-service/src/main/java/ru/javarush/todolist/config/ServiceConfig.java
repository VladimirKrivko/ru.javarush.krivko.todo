package ru.javarush.todolist.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("ru.javarush.todolist.service")
@Import({DaoConfig.class})
public class ServiceConfig {
}
