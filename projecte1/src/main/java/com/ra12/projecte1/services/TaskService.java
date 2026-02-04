package com.ra12.projecte1.services;

import com.ra12.projecte1.dto.TaskRequestDTO;
import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<String> createTasks(List<Task> tasks) {
        for (Task task : tasks) {
            taskRepository.insertTask(task);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Tasca/Tasques creades correctament");
    }

    public static ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.getAllTasks();
        if (tasks == null || tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tasks);

    }
}
