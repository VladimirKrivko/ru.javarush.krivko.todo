package ru.javarush.todolist.dao;

import ru.javarush.todolist.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    List<Task> getAll(int offset, int limit);

    int getAllCount();

    Optional<Task> getById(int id);

    void saveOrUpdate(Task task);

    void delete(Task task);
}
