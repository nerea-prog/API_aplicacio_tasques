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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CustomLogging customLogging;

    public ResponseEntity<String> createTasks(List<Task> tasks) {
        customLogging.logInfo("TaskService", "createTasks",
                "Creant els customers");
        try {
            for (Task task : tasks) {
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

    public ResponseEntity<String> getTaskById(long id) {
        customLogging.logInfo("TaskService", "getTaskById",
                "obtenint el customer per id");
        try {
            Task task = taskRepository.getTaskById(id);
            if (task == null) {
                customLogging.logError("TaskService", "getAllTasks",
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
