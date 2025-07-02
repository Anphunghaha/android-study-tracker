package com.example.studytrackerapp.Models;

public class CartItem {
    private int bookId;
    private String title;
    private double price;
    private int quantity;

    public CartItem(int bookId, String title, double price, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
