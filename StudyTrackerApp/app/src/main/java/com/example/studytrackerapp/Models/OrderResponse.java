package com.example.studytrackerapp.Models;

import java.util.List;

public class OrderResponse {
    public int orderId;
    public String orderDate;
    public double totalAmount;
    public List<OrderItemResponse> items;
}
