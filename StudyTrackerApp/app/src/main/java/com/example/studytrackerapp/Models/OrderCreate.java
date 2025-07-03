package com.example.studytrackerapp.Models;
import java.util.List;

public class OrderCreate {
    public int userId;
    public List<OrderItemCreate> items;

    public String shippingAddress;
    public String status;

    public OrderCreate(int userId, String status, String shippingAddress, List<OrderItemCreate> items) {
        this.userId = userId;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.items = items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemCreate> getItems() {
        return items;
    }

    public void setItems(List<OrderItemCreate> items) {
        this.items = items;
    }
}
