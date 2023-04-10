package ru.javarush.todolist.service;

import ru.javarush.todolist.dto.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getAll(int offset, int limit);

    int getAllCount();

    TaskDto edit(int id, String description, String status);

    TaskDto create(String description, String status);

    void delete(int id);
}
