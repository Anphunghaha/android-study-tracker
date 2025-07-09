package com.example.studytrackerapp.api;
import com.example.studytrackerapp.Models.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // 📚 Lấy danh sách sách
    @GET("Books")
    Call<List<Book>> getAllBooks();

    @GET("Books/search")
    Call<List<Book>> searchBooks(@Query("keyword") String keyword);
    // 🔐 Đăng nhập
    @POST("User/login")
    Call<UserResponse> login(@Body UserLogin loginDto);

    // update user
    @PUT("User/update")
    Call<Void> updateUser(@Body UserUpdateDTO updateDTO);
    // 🛒 Tạo đơn hàng
    @POST("Order/create")
    Call<OrderResponse> createOrder(@Body OrderCreate orderCreate);

    @GET("Order/GetOrderByUser/{userId}")
    Call<List<OrderDTO>> getOrdersByUser(@Path("userId") int userId);

    @GET("Books/BookBycategory/{categoryId}")
    Call<List<Book>> getBooksByCategory(@Path("categoryId") int categoryId);
    // 📂 Lấy danh mục
    @GET("Category")
    Call<List<Category>> getAllCategories();

    // ✍️ Lấy tác giả
    @GET("Author")
    Call<List<Author>> getAllAuthors();
    @GET("Author/{id}")
    Call<Author> getAuthorById(@Path("id") int id);

    // 📖 Lấy chi tiết sách theo id
    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") int bookId);
   // Đăng Ký
    @POST("User/register")
    Call<Void> registerUser(@Body UserRegister user);

}
