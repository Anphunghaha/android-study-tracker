package com.example.studytrackerapp.Models;
import java.util.List;

public class OrderCreate {
    public int userId;
    public List<OrderItemCreate> items;

    public OrderCreate(int userId, List<OrderItemCreate> items) {
        this.userId = userId;
        this.items = items;
    }
}
