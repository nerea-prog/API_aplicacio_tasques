package com.ra12.projecte1.services;

import com.ra12.projecte1.dto.TaskRequestDTO;
import com.ra12.projecte1.logging.CustomLogging;
import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CustomLogging customLogging;

    public ResponseEntity<?> updateTask(Long id, Task taskDetails) {
        customLogging.logInfo("TaskService", "updateTask", "Modificant task amb id: " + id);

        try {
            Task existingTask = taskRepository.findById(id);

            // Si no existe
            if (existingTask == null) {
                throw new IllegalArgumentException("La task amb id: " + id + " no existeix");
            }

            // Validaciones (solo si vienen informadas)
            if (taskDetails.getTitle() != null) validateTitle(taskDetails.getTitle());
            if (taskDetails.getCategory() != null) validateCategory(taskDetails.getCategory());

            // Actualizamos campos
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setCategory(taskDetails.getCategory());
            existingTask.setCompleted(taskDetails.isCompleted());
            existingTask.setDataUpdated(new Timestamp(System.currentTimeMillis()));

            taskRepository.updateTask(existingTask);

            return ResponseEntity.status(HttpStatus.OK).body(existingTask);

        } catch (IllegalArgumentException e) {
            customLogging.logError("TaskService", "updateTask", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            customLogging.logError("TaskService", "updateTask", "Error actualitzant task amb id: " + id, e);
            throw e;
        }
    }

    // MÉTODES DE VALIDACIÓ
    private void validateTitle(String title) {
        if (title == null || title.trim().length() < 3) {
            throw new IllegalArgumentException("El títol ha de tenir com a mínim 3 caràcters");
        }
    }

    private void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria no pot estar buida");
        }
    }

}
