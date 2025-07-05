package com.example.studytrackerapp.Models;

import java.util.List;

public class OrderDTO {
    public int orderId;
    public String orderDate;
    public double totalAmount;
    public String shippingAddress;
    public String status;
    public List<OrderItemDTO> items;
}
