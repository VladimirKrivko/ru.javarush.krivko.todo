package ru.javarush.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todolist.dao.TaskDao;
import ru.javarush.todolist.dto.TaskDto;
import ru.javarush.todolist.entity.Status;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.exception.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskDao taskDao;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public List<TaskDto> getAll(int offset, int limit) {
        List<Task> tasks = taskDao.getAll(offset, limit);
        List<TaskDto> dtoTasks = new ArrayList<>();
        for (Task task : tasks) {
            dtoTasks.add(taskConvertToTaskInfo(task));
        }
        return dtoTasks;
    }

    @Override
    public int getAllCount() {
        return taskDao.getAllCount();
    }

    @Override
    @Transactional
    public TaskDto edit(int id, String description, String status) {
        Task task = taskDao.getById(id)
                .orElseThrow(() -> new TaskNotFoundException("task with id=%d not found".formatted(id)));
        task.setDescription(description);
        task.setStatus(Status.valueOf(status));
        taskDao.saveOrUpdate(task);
        return taskConvertToTaskInfo(task);
    }

    @Override
    public TaskDto create(String description, String status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(Status.valueOf(status));
        taskDao.saveOrUpdate(task);
        return taskConvertToTaskInfo(task);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Task task = taskDao.getById(id)
                .orElseThrow(() -> new TaskNotFoundException("task with id=%d not found".formatted(id)));
        taskDao.delete(task);
    }

    private TaskDto taskConvertToTaskInfo(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus().name());
        return taskDto;
    }
}
