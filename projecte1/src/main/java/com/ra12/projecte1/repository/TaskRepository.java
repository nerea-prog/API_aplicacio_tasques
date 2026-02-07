package com.ra12.projecte1.repository;

import com.ra12.projecte1.logging.CustomLogging;
import com.ra12.projecte1.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomLogging customLogging;

    private static final class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setCategory(rs.getString("category"));
            task.setCompleted(rs.getBoolean("completed"));
            task.setImagePath(rs.getString("imagePath"));
            task.setDataCreated(rs.getTimestamp("dataCreated"));
            task.setDataUpdated(rs.getTimestamp("dataUpdated"));
            return task;
        }
    }

    public void updateTask(Task task) {
        customLogging.logInfo("TaskRepository", "updateTask", "Actualitzant task amb id: " + task.getId());
        try {
            jdbcTemplate.update(
                    "UPDATE tasks SET title = ?, category = ?, completed = ?, imagePath = ?, dataUpdated = ? WHERE id = ?",
                    task.getTitle(),
                    task.getCategory(),
                    task.isCompleted(),
                    task.getImagePath(),
                    task.getDataUpdated(),
                    task.getId()
            );
        } catch (Exception e) {
            customLogging.logError("TaskRepository", "updateTask", "Error actualitzant task amb id: " + task.getId(), e);
            throw e;
        }
    }

    // Funció per eliminar un task per ID
    public void deleteTask(Long id) {
        customLogging.logInfo("TaskRepository", "deleteTask", "Eliminant task amb id: " + id);
        try {
            jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
        } catch (Exception e) {
            customLogging.logError("TaskRepository", "deleteTask", "Error eliminant task amb id: " + id, e);
            throw e;
        }
    }

    // Funció per eliminar totes les tasks
    public void deleteAllTasks() {
        customLogging.logInfo("TaskRepository", "deleteAllTasks", "Eliminant totes les tasks");
        try {
            jdbcTemplate.update("DELETE FROM tasks");
        } catch (Exception e) {
            customLogging.logError("TaskRepository", "deleteAllTasks", "Error eliminant totes les tasks", e);
            throw e;
        }
    }

    // Funció per actualitzar el camp imagePath d'una task
    public void updateTaskImage(Long id, String imagePath) {
        customLogging.logInfo("TaskRepository", "updateTaskImage", "Actualitzant imatge de la task amb id: " + id);
        try {
            jdbcTemplate.update(
                    "UPDATE tasks SET imagePath = ?, dataUpdated = CURRENT_TIMESTAMP WHERE id = ?",
                    imagePath,
                    id
            );
        } catch (Exception e) {
            customLogging.logError("TaskRepository", "updateTaskImage", "Error actualitzant imatge de la task amb id: " + id, e);
            throw e;
        }
    }

}
