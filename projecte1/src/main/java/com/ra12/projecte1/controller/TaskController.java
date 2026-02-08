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

    // POST: crea diverses tasques enviades en format JSON
    @PostMapping("/api/task")
    public ResponseEntity<String> createTasks(@RequestBody List<Task> tasks) {
        return taskService.createTasks(tasks);
    }

    // GET: retorna totes les tasques
    @GetMapping("/api/task")
    public ResponseEntity<List<Task>> getAllTask() {
        return taskService.getAllTasks();
    }

    // GET: retorna una tasca específica segons el seu ID
    @GetMapping("/api/task/{id}")
    public ResponseEntity<String> getTaskById(@PathVariable long id){
        return taskService.getTaskById(id);
    }

    // POST: puja un fitxer CSV amb diverses tasques
    @PostMapping("/api/task/csv")
    public ResponseEntity<String> uploadCSV(@RequestParam MultipartFile tasksFile) {
        return taskService.uploadCSV(tasksFile);
    }

    // PUT: actualitza una tasca segons el seu ID
    @PutMapping("/api/task/{task_id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long task_id,
            @RequestBody Task taskDetails) {
        return taskService.updateTask(task_id, taskDetails);
    }

    // DELETE: elimina una tasca segons el seu ID
    @DeleteMapping("/api/task/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long task_id) {
        return taskService.deleteTask(task_id);
    }

    // DELETE: elimina totes les tasques
    @DeleteMapping("/api/tasks")
    public ResponseEntity<String> deleteAllTasks() {
        return taskService.deleteAllTasks();
    }

    // POST: puja una imatge per a una tasca concreta
    @PostMapping("/api/task/{task_id}/image")
    public ResponseEntity<String> uploadTaskImage(
            @PathVariable Long task_id,
            @RequestParam MultipartFile imageFile) {

        return taskService.saveTaskImage(task_id, imageFile);
    }
}
