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
import ru.javarush.todolist.dto.TaskInfo;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Task> tasks = taskService.getAll((page - 1) * limit, limit);
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);

        int allCount = taskService.getAllCount();
        int totalPages = (int) Math.ceil(1.0 * allCount / limit);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }
        return "tasks";
    }

    @PostMapping(value = "/{id}", consumes = {"*/*"})
    public void edit(Model model,
                     @PathVariable Integer id,
                     @RequestBody TaskInfo info) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.edit(id, info.getDescription(), info.getStatus());
    }

    @PostMapping(value = "/", consumes = {"*/*"})
    public void add(Model model,
                    @RequestBody TaskInfo info) {
        if(info.getDescription() != null && !info.getDescription().isEmpty()) {
            taskService.create(info.getDescription(), info.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable Integer id) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);
    }
}
