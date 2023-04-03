package ru.javarush.todolist.dao;

import ru.javarush.todolist.entity.Task;

import java.util.List;

public interface TaskDao {

    List<Task> getAll(int offset, int limit);

    int getAllCount();

    Task getById(int id);

    void saveOrUpdate(Task task);

    void delete(Task task);
}
