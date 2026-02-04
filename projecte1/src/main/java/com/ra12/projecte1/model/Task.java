package com.ra12.projecte1.model;

import java.sql.Timestamp;

public class Task {
    private Long id;
    private String title, category, imagePath;
    private boolean completed;
    private Timestamp dataCreated, dataUpdated;

    public Task() {

    }

    public Task(Long id, String title, String category, String imagePath, boolean completed, Timestamp dataCreated, Timestamp dataUpdated) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.imagePath = imagePath;
        this.completed = completed;
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Timestamp getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }

    public Timestamp getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    @Override
    public String toString() {
        return "Task{" +
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


