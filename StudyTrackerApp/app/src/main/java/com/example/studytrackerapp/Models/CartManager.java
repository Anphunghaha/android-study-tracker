package com.example.studytrackerapp.Models;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Book book) {
        for (CartItem item : cartItems) {
            if (item.getBookId() == book.bookID) {
                if (item.getQuantity() < book.getStock()) {
                    item.setQuantity(item.getQuantity() + 1);
                }
                return;
            }
        }

        // ✅ Dùng constructor đầy đủ
        CartItem newItem = new CartItem(
                book.getBookID(),
                book.getTitle(),
                book.getPrice(),
                1,                          // quantity mặc định khi thêm
                book.getImageUrl(),        // truyền imageUrl ở đây
                book.getStock()            // truyền stock để giới hạn số lượng
        );

        cartItems.add(newItem);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    // 🆕 Thêm phương thức này
    public void setCartItems(List<CartItem> items) {
        this.cartItems = items;
    }
    public void clearCart() {
        cartItems.clear();
    }
    // ✅ Thêm phương thức này để hỗ trợ adapter
    public void removeFromCart(int bookId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getBookId() == bookId) {
                cartItems.remove(i);
                break;
            }
        }
    }

    // ✅ Thêm nếu cần cập nhật số lượng
    public void updateItem(CartItem updatedItem) {
        for (CartItem item : cartItems) {
            if (item.getBookId() == updatedItem.getBookId()) {
                item.setQuantity(updatedItem.getQuantity());
                break;
            }
        }
    }

    public void updateQuantity(int bookId, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getBookId() == bookId) {
                if (newQuantity > item.getStock()) {
                    item.setQuantity(item.getStock()); // Gán max là stock
                } else if (newQuantity < 1) {
                    item.setQuantity(1); // Gán min là 1
                } else {
                    item.setQuantity(newQuantity);
                }
                return;
            }
        }
    }

}
