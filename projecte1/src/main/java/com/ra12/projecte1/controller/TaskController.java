package com.ra12.projecte1.controller;


import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/taskbuddy")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/api/task")
    public ResponseEntity<String> createTasks(@RequestBody List<Task> tasks) {
        return taskService.createTasks(tasks);
    }

    @GetMapping("/api/task")
    public ResponseEntity<List<Task>> getAllTask() {
        return taskService.getAllTasks();
    }

    @GetMapping("/api/task/{id}")
    public ResponseEntity<String> getTaskById(@PathVariable long id){
        return taskService.getTaskById(id);
    }

    @PostMapping("/api/task/csv")
    public ResponseEntity<String> uploadCSV(@RequestParam MultipartFile tasksFile){
        return taskService.uploadCSV(tasksFile);
    }
}
