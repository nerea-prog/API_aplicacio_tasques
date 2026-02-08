package com.ra12.projecte1.services;

import com.ra12.projecte1.dto.TaskRequestDTO;
import com.ra12.projecte1.logging.CustomLogging;
import com.ra12.projecte1.model.Task;
import com.ra12.projecte1.repository.TaskRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

        // Crea una llista de tasques
        public ResponseEntity<String> createTasks(List<Task> tasks) {
            customLogging.logInfo("TaskService", "createTasks",
                    "Creant els customers");
            if (tasks == null || tasks.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La llista de tasques esta buida");
            }

            try {
                for (Task task : tasks) {

                    if (task == null) {
                        customLogging.logError("TaskService", "createTasks",
                                "Existeix una task null dins la llista", null);

                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Existeix una task null dins la llista");
                    }

                    if (task.getTitle() == null || task.getTitle().isEmpty()) {
                        customLogging.logError("TaskService", "createTasks",
                                "Error validació: El títol és obligatori", null);

                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("El títol és obligatori");
                    }

                    if (task.getCategory() == null || task.getCategory().isEmpty()) {
                        customLogging.logError("TaskService", "createTasks",
                                "Error validació: La categoria és obligatoria", null);

                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("La categoria és obligatoria");
                    }

                    taskRepository.insertTask(task);
                }
                // Controlar si las tascas son null
                return ResponseEntity.status(HttpStatus.CREATED).body("Tasca/Tasques creades correctament");
            } catch (Exception e) {
                customLogging.logError("TaskService", "createTasks",
                        "Error creant els customers", e);
                throw e;
            }

        }

    // REtorna totes les tasques
    public ResponseEntity<List<Task>> getAllTasks() {
        customLogging.logInfo("TaskService", "getAllTasks",
                "obtenint tots els customers");
        try {
            List<Task> tasks = taskRepository.getAllTasks();
            if (tasks == null || tasks.isEmpty()) {
                customLogging.logError("TaskService", "getAllTasks",
                        "Error obtenint tots els customers", new RuntimeException("Llista buida"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(tasks);
        } catch (Exception e) {
            customLogging.logError("TaskService", "getAllTasks",
                    "Error obtenint tots els customers", e);
            throw e;
        }

    }

    // REtorna una tasca per ID
    public ResponseEntity<String> getTaskById(long id) {
        customLogging.logInfo("TaskService", "getTaskById",
                "obtenint el customer per id");
        try {
            Task task = taskRepository.getTaskById(id);
            if (task == null) {
                customLogging.logError("TaskService", "getTaskById",
                        "Error obtenint el customer per id", new RuntimeException("Tasca no trobada"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasca no trobada");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Tasca trobada \n\n" + task);
        } catch (Exception e) {
            customLogging.logError("TaskService", "getTaskById",
                    "Error obtenint el customer per id", e);
            throw e;
        }
    }

    // Actualitza una tasca per ID
    public ResponseEntity<?> updateTask(Long id, Task taskDetails) {
        customLogging.logInfo("TaskService", "updateTask", "Modificant task amb id: " + id);

        try {
            Task existingTask = taskRepository.getTaskById(id);

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
            Task existingTask = taskRepository.getTaskById(id);
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

    // Desa una imatge per a un registre d'una tasca
    public ResponseEntity<String> saveTaskImage(Long taskId, MultipartFile imageFile) {
        customLogging.logInfo("TaskService", "saveTaskImage", "Afegint la imatge " + imageFile.getOriginalFilename() + " per a la task amb id: " + taskId);
        try {
            // 1. Comprovar si existeix la task
            Task existingTask = taskRepository.getTaskById(taskId);
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

    // Pujar tasques des d'un CSV
    public ResponseEntity<String> uploadCSV(MultipartFile tasksFile) {
        customLogging.logInfo("TaskService", "uploadCSV",
                "afegint tasques per csv");
        try {
            int totalRegistros = 0;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(tasksFile.getInputStream()))) {
                String linia;
                int numeroLinia = 0;
                while ((linia = br.readLine()) != null) {
                    numeroLinia++;
                    if (numeroLinia == 1) continue;
                    String[] camps = linia.split(",");
                    Task task = new Task();
                    task.setTitle(camps[0]);
                    task.setCategory(camps[1]);
                    task.setCompleted(Boolean.parseBoolean(camps[2]));
                    try {
                        taskRepository.insertTask(task);
                        totalRegistros++;
                    } catch (Exception e) {
                        customLogging.logError("TaskService", "uploadCSV",
                                "Error inserint registre a la linia: " + numeroLinia, e);
                    }
                }
                Path pathDirectori = Paths.get("src/main/resources/private/task_processed");
                if (!Files.exists(pathDirectori)) {
                    Files.createDirectories(pathDirectori);
                }
                Path pathCSV = pathDirectori.resolve(tasksFile.getOriginalFilename());
                Files.copy(tasksFile.getInputStream(), pathCSV, StandardCopyOption.REPLACE_EXISTING);
                return ResponseEntity.status(HttpStatus.CREATED).body("Total de tasques afegides: " + totalRegistros);
            } catch (FileNotFoundException e) {
                customLogging.logError("TaskService", "uploadCSV",
                        "Error: No es troba el fitxer ", e);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: fitxer no trobat");
            } catch (IOException e) {
                customLogging.logError("TaskService", "uploadCSV",
                        "Error accedint al fitxer ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR d'accés al fitxer");
            }

        } catch (Exception e) {
            customLogging.logError("TaskService", "uploadCSV",
                    "Error afegint tasques per csv", e);
            throw e;
        }
    }
}
