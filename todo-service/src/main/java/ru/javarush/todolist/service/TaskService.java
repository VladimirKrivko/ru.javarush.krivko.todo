package ru.javarush.todolist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todolist.dao.TaskHibernateDao;
import ru.javarush.todolist.entity.Status;
import ru.javarush.todolist.entity.Task;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskHibernateDao taskHibernateDao;

    public TaskService(TaskHibernateDao taskHibernateDao) {
        this.taskHibernateDao = taskHibernateDao;
    }

    public List<Task> getAll(int offset, int limit) {
        return taskHibernateDao.getAll(offset, limit);
    }

    public int getAllCount() {
        return taskHibernateDao.getAllCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        Task task = taskHibernateDao.getById(id);
        if (isNull(task)) {
            throw new RuntimeException("Not found");
        }
        task.setDescription(description);
        task.setStatus(status);
        taskHibernateDao.saveOrUpdate(task);
        return task;
    }

    public Task create(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskHibernateDao.saveOrUpdate(task);
        return task;
    }

    @Transactional
    public void delete(int id) {
        Task task = taskHibernateDao.getById(id);
        if (isNull(task)) {
            throw new RuntimeException("Not found");
        }
        taskHibernateDao.delete(task);
    }
}
