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
    public ResponseEntity<String> uploadCSV(@RequestParam MultipartFile tasksFile) {
        return taskService.uploadCSV(tasksFile);
    }
    // UPDATE per ID
    @PutMapping("/api/task/{task_id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long task_id,
            @RequestBody Task taskDetails) {
        return taskService.updateTask(task_id, taskDetails);
    }

    // DELETE task per ID
    @DeleteMapping("/api/task/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long task_id) {
        return taskService.deleteTask(task_id);
    }

    // DELETE totes les tasks
    @DeleteMapping("/api/tasks")
    public ResponseEntity<String> deleteAllTasks() {
        return taskService.deleteAllTasks();
    }

    // POST per afegir la imatge d'una task
    @PostMapping("/api/task/{task_id}/image")
    public ResponseEntity<String> uploadTaskImage(
            @PathVariable Long task_id,
            @RequestParam MultipartFile imageFile) {

        return taskService.saveTaskImage(task_id, imageFile);
    }
}
