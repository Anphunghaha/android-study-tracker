package com.example.studytrackerapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.Category;
import com.example.studytrackerapp.apdapters.BookAdapter;
import com.example.studytrackerapp.apdapters.CategoryAdapter;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;
import com.example.studytrackerapp.Models.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private RecyclerView rvCategories;
    private SearchView searchView;
    private BookAdapter bookAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Book> allBooks = new ArrayList<>(); // danh sách gốc để tìm kiếm
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBooks = findViewById(R.id.rvBooks);
        rvCategories = findViewById(R.id.rvCategories);
        searchView = findViewById(R.id.searchView);

        // Set layout cho RecyclerView cho sách
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this,new ArrayList<>());
        rvBooks.setAdapter(bookAdapter);

        // Cài đặt RecyclerView cho category (ngang)
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(this, categories, this::onCategoryClick);
        rvCategories.setAdapter(categoryAdapter);

        // Gọi API lấy danh sách sách
        loadBooks();
        loadCategories();
        // Xử lý tìm kiếm
        setupSearch();
    }

    private void loadBooks() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBooks = response.body();
                    bookAdapter.setBooks(allBooks); // hiển thị lên RecyclerView
                } else {
                    Log.e("API", "Không nhận được dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API: " + t.getMessage());
            }
        });
    }
    private void loadCategories() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    Category allCategory = new Category();
                    allCategory.setId(0);
                    allCategory.setName("Tất cả");
                    categories.add(allCategory);
                    categories.addAll(response.body());
                    categoryAdapter.setCategories(categories);
                    Log.d("API", "Tải danh sách category thành công: " + categories.size());
                } else {
                    Log.e("API", "Lỗi tải category: HTTP " + response.code() + " " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("API", "Chi tiết lỗi: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e("API", "Không thể đọc errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API khi tải category: " + t.toString());
                Log.e("API", "Nguyên nhân: ", t);
            }
        });
    }
    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Gọi API tìm kiếm khi nhấn enter
                searchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Gọi API tìm kiếm khi text thay đổi
                if (newText.isEmpty()) {
                    // Nếu không có từ khóa, hiển thị lại danh sách gốc
                    bookAdapter.setBooks(allBooks);
                } else {
                    searchBooks(newText);
                }
                return true;
            }
        });
    }
    private void onCategoryClick(Category category) {
        if (category.getId() == 0) {
            bookAdapter.setBooks(allBooks);
        } else {
            getBooksByCategory(category.getId());
        }
    }
    private void searchBooks(String keyword) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchBooks(keyword).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> filteredBooks = response.body();
                    bookAdapter.setBooks(filteredBooks); // Cập nhật RecyclerView với kết quả tìm kiếm
                } else {
                    Log.e("API", "Không nhận được dữ liệu tìm kiếm");
                    bookAdapter.setBooks(new ArrayList<>()); // Xóa danh sách nếu không có kết quả
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API khi tìm kiếm: " + t.getMessage());
                bookAdapter.setBooks(new ArrayList<>()); // Xóa danh sách nếu lỗi
            }
        });
    }
    private void getBooksByCategory(int categoryId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getBooksByCategory(categoryId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    bookAdapter.setBooks(books);
                    Log.d("API", "Lấy sách theo category ID " + categoryId + " thành công: " + books.size());
                } else {
                    Log.e("API", "Lỗi lấy sách theo category: HTTP " + response.code() + " " + response.message());
                    bookAdapter.setBooks(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API khi lấy sách theo category: " + t.getMessage());
                bookAdapter.setBooks(new ArrayList<>());
            }
        });
    }
}