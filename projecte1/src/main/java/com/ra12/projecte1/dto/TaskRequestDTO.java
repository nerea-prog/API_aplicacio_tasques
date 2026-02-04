package com.ra12.projecte1.dto;

import java.sql.Timestamp;

public class TaskRequestDTO {
    private String title, category, imagePath;
    private Boolean completed;

    public TaskRequestDTO() {
    }

    public TaskRequestDTO(String title, String category, String imagePath, boolean completed) {
        this.title = title;
        this.category = category;
        this.imagePath = imagePath;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "TaskRequestDTO{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", completed=" + completed +
                '}';
    }
}
