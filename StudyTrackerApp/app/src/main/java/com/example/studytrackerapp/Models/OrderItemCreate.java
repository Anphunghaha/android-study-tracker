package com.example.studytrackerapp.Models;

public class OrderItemCreate {
    public int bookId;
    public int quantity;

    public OrderItemCreate(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
