package com.ra12.projecte1.repository;

import com.ra12.projecte1.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class TaskRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    }
}