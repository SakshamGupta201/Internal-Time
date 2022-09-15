package com.saksham.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.saksham.demo.model.Task;
import com.saksham.demo.model.User;
import com.saksham.demo.repository.TaskRepository;
import com.saksham.demo.service.TaskService;
import com.saksham.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    private TaskService taskService;
    private UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Task task = new Task();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@ModelAttribute Task task, @PathVariable Long id, Model model) {
        Task task2 = taskService.getTaskById(id);
        model.addAttribute("task", taskService.getTaskById(id));
        int timeSpent = task2.getTimeSpent();
        model.addAttribute("timeSpent", timeSpent);
        return "forms/task-edit";
    }

    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        Task t1 = taskRepository.findById(id).get();
        if (t1.getDate().equals(task.getDate())) {
            return "redirect:/tasks";
        } else {
            task.setOwner(t1.getOwner());
            task.setCreatorName(t1.getCreatorName());
            task.setTimeSpent(task.getTimeSpent() + t1.getTimeSpent());
            taskService.updateTask(id, task);
        }

        return "redirect:/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

}
