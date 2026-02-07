package com.ra12.projecte1.controller;


import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PutMapping("/task/{task_id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long task_id,
            @RequestBody Task taskDetails) {
        return taskService.updateTask(task_id, taskDetails);
    }
}
