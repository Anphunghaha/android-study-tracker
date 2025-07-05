package com.example.studytrackerapp.Models;

public class OrderItemDTO {
    public int orderItemId;
    public int bookId;
    public int quantity;
    public double unitPrice;
    public String bookTitle;

    public String imgUrl;

    // ✅ Add getter + setter
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
