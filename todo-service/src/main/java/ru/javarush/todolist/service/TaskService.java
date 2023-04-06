package ru.javarush.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todolist.dao.TaskDao;
import ru.javarush.todolist.entity.Status;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.exception.TaskNotFoundException;

import java.util.List;

@Service
public class TaskService {
    private final TaskDao taskDao;

    @Autowired
    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<Task> getAll(int offset, int limit) {
        return taskDao.getAll(offset, limit);
    }

    public int getAllCount() {
        return taskDao.getAllCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        Task task = taskDao.getById(id)
                .orElseThrow(() -> new TaskNotFoundException("task with id=%d not found".formatted(id)));
        task.setDescription(description);
        task.setStatus(status);
        taskDao.saveOrUpdate(task);
        return task;
    }

    public Task create(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDao.saveOrUpdate(task);
        return task;
    }

    @Transactional
    public void delete(int id) {
        Task task = taskDao.getById(id)
                .orElseThrow(() -> new TaskNotFoundException("task with id=%d not found".formatted(id)));
        taskDao.delete(task);
    }
}
