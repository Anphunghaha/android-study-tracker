package com.example.studytrackerapp.Models;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Category {
    public int categoryId;
    public String name;
    public String description;
    public String createdAt;
    @Override
    public String toString() {
        return name; // Hiển thị tên category trong Spinner
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return categoryId;
    }

    public void setId(int id) {
        this.categoryId = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
