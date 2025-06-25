package com.example.studytrackerapp.Models;

public class Book {
    public int bookID;
    public String title;
    public int authorId;
    public int categoryId;
    public String description;
    public double price;
    public Integer stock; // nullable nên dùng Integer thay vì int
    public String imageUrl;
}

