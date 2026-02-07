package com.ra12.projecte1.services;

import com.ra12.projecte1.dto.TaskRequestDTO;
import com.ra12.projecte1.logging.CustomLogging;
import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    // DELETE task per ID
    public ResponseEntity<String> deleteTask(Long id) {
        customLogging.logInfo("TaskService", "deleteTask", "Borrant la task amb id: " + id);
        try {
            Task existingTask = taskRepository.findById(id);
            if (existingTask == null) {
                throw new IllegalArgumentException("La task amb id: " + id + " no existeix");
            }

            taskRepository.deleteTask(id);

            return ResponseEntity.status(HttpStatus.OK).body("Task amb ID " + id + " eliminada correctament");
        } catch (Exception e) {
            customLogging.logError("TaskService", "deleteTask", "Error borrant la task amb id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error borrant la task amb ID " + id + ": " + e.getMessage());
        }
    }

    // DELETE totes les tasks
    public ResponseEntity<String> deleteAllTasks() {
        customLogging.logInfo("TaskService", "deleteAllTasks", "Borrant totes les tasks");
        try {
            taskRepository.deleteAllTasks();
            return ResponseEntity.status(HttpStatus.OK).body("Totes les tasks eliminades correctament");
        } catch (Exception e) {
            customLogging.logError("TaskService", "deleteAllTasks", "Error eliminant totes les tasks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error eliminant totes les tasks: " + e.getMessage());
        }
    }

    public ResponseEntity<String> saveTaskImage(Long taskId, MultipartFile imageFile) {
        customLogging.logInfo("TaskService", "saveTaskImage", "Afegint la imatge " + imageFile.getOriginalFilename() + " per a la task amb id: " + taskId);
        try {
            // 1. Comprovar si existeix la task
            Task existingTask = taskRepository.findById(taskId);
            if (existingTask == null) {
                throw new IllegalArgumentException("Task amb ID " + taskId + " no existeix");
            }

            // 2. Crear carpeta src/main/resources/public/images si no existeix
            Path uploadDir = Paths.get("projecte1/src/main/resources/public/images");
            Files.createDirectories(uploadDir);

            // 3. Crear un nom únic per la imatge
            String originalFilename = imageFile.getOriginalFilename();
            String uniqueFilename = taskId + "" + System.currentTimeMillis() + "" + originalFilename;

            // 4. Ruta completa on guardar la imatge
            Path filePath = uploadDir.resolve(uniqueFilename);

            // 5. Guardar la imatge al disc
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 6. Guardar ruta relativa per a la BD
            String relativePath = "images/" + uniqueFilename;

            // 7. Actualitzar la BD amb la ruta de la imatge
            taskRepository.updateTaskImage(taskId, relativePath);

            customLogging.logInfo("TaskService", "saveTaskImage", "La imatge s'ha guardat correctament. El path és: " + relativePath);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Imatge pujada correctament: " + relativePath);

        } catch (Exception e) {
            customLogging.logError("TaskService", "saveTaskImage", "Error afegint la imatge " + imageFile.getOriginalFilename() + " per a la task amb id: " + taskId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error pujant la imatge: " + e.getMessage());
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
