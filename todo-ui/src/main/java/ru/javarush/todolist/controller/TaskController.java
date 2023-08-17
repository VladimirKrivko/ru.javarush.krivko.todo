package ru.javarush.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javarush.todolist.dto.TaskDto;
import ru.javarush.todolist.exception.IncorrectAddressBarParameterException;
import ru.javarush.todolist.exception.TaskIdInvalidException;
import ru.javarush.todolist.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public String tasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") String limit) {
        try {
            if (!page.matches("\\d+") || page.matches("0+") || page.isEmpty()) {
                throw new IncorrectAddressBarParameterException("Invalid parameter is set in the address bar >>page=%s<< ".formatted(page) +
                        "The 'page' parameter must only be a positive integer.");
            }
            if (!limit.matches("\\d+") || limit.matches("0+") || limit.isEmpty()) {
                throw new IncorrectAddressBarParameterException("Invalid parameter is set in the address bar >>limit=%s<< ".formatted(limit) +
                        "The 'limit' parameter must only be a positive integer.");
            }

            int pageInt = Integer.parseInt(page);
            int limitInt = Integer.parseInt(limit);
            List<TaskDto> tasks = taskService.getAll((pageInt - 1) * limitInt, limitInt);
            model.addAttribute("tasks", tasks);
            model.addAttribute("current_page", pageInt);

            int allCount = taskService.getAllCount();
            int totalPages = (int) Math.ceil(1.0 * allCount / limitInt);
            if (totalPages > 1) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("page_numbers", pageNumbers);
            }
            return "tasks";
        } catch (IncorrectAddressBarParameterException e) {
            model.addAttribute("message", e.getMessage());
            return "error_page";
        }
    }

    @PostMapping(value = "/{id}", consumes = {"*/*"})
    public String edit(Model model,
                     @PathVariable Integer id,
                     @RequestBody TaskDto info) {
        try {
            if (isNull(id) || id <= 0) {
                throw new TaskIdInvalidException("Task with id=%d does not exist".formatted(id));
            }
            taskService.edit(id, info.getDescription(), info.getStatus());
            return "tasks";
        } catch (RuntimeException e) {
            // is it really necessary?
            model.addAttribute("message", e.getMessage());
            return "error_page";
        }
    }

    @PostMapping(value = "/", consumes = {"*/*"})
    public void add(Model model,
                    @RequestBody TaskDto info) {
        if (info.getDescription() != null && !info.getDescription().isEmpty()) {
            taskService.create(info.getDescription(), info.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable Integer id) {
        if (isNull(id) || id <= 0) {
            throw new TaskIdInvalidException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model, "1", "10");
    }
}
