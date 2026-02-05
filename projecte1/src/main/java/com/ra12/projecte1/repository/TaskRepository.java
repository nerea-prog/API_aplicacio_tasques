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

    public void insertTask(Task task) {
        customLogging.logInfo("TaskRepository", "insertTask",
                "Executant consulta: INSERT INTO tasks (title, category, completed, imagePath, " +
                        "dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)");
        try {
            LocalDateTime now = LocalDateTime.now();
            jdbcTemplate.update(
                    "INSERT INTO tasks (title, category, completed, imagePath, dataCreated, dataUpdated) " + "VALUES (?, ?, ?, ?, ?, ?)",
                    task.getTitle(),
                    task.getCategory(),
                    task.isCompleted(),
                    task.getImagePath(),
                    now,
                    now
            );

        } catch (Exception e) {
            customLogging.logError("TaskRepository", "insertTask",
                    "Error executant la consulta: INSERT INTO tasks (title, category, completed, " +
                            "imagePath, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", e);
            throw e;
        }

    }

    public List<Task> getAllTasks() {
        customLogging.logInfo("TaskRepository", "getAllTasks",
                "Executant consulta: SELECT * FROM tasks");
        try {
            String sql = "SELECT * FROM tasks";
            return jdbcTemplate.query(sql, new TaskRowMapper());

        } catch (Exception e) {
            customLogging.logError("TaskRepository", "getAllTasks",
                    "Error executant la consulta: SELECT * FROM tasks", e);
            throw e;
        }
    }


    public Task getTaskById(long id) {
        customLogging.logInfo("TaskRepository", "getTaskById",
                "Executant consulta: select * from tasks where id = ?");
        try {
            String sql = "select * from tasks where id = ?";
            List<Task> task = jdbcTemplate.query(sql, new TaskRowMapper(), id);
            if (task.isEmpty()) {
                return null;
            }
            return task.get(0);

        } catch (Exception e) {
            customLogging.logError("TaskRepository", "getTaskById",
                    "Error executant la consulta: select * from tasks where id = ?", e);
            throw e;
        }
    }
}