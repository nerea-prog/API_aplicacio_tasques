package com.ra12.projecte1.dto;

import java.sql.Timestamp;

public class TaskResponseDTO {
    private Long id;
    private String title, category, imagePath;
    private Boolean completed;
    private Timestamp dataCreated, dataUpdated;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(long id, String title, String category, String imagePath, boolean completed, Timestamp dataCreated, Timestamp dataUpdated) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.imagePath = imagePath;
        this.completed = completed;
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
    }

    public Timestamp getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public Timestamp getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TaskResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", completed=" + completed +
                ", dataCreated=" + dataCreated +
                ", dataUpdated=" + dataUpdated +
                '}';
    }
}