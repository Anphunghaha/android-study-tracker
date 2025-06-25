package com.example.studytrackerapp.Models;
import java.util.List;
import java.util.Date;

public class Order {
    public int orderId;
    public Date orderDate;
    public Double totalAmount;
    public List<OrderItem> items;
}
