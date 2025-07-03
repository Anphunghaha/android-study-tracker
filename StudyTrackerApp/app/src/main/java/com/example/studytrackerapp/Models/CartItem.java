package com.example.studytrackerapp.Models;

public class CartItem {
    private int bookId;
    private String title;
    private double price;
    private int quantity;
    private int stock;
    private String imageUrl;  // thêm

    public CartItem(int bookId, String title, double price, int quantity,  String imageUrl ,int stock) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.stock = stock;

    }
    public CartItem(int bookId, String title, double price, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;


    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
