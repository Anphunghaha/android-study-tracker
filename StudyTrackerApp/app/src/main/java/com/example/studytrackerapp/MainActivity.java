package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.Author;
import com.example.studytrackerapp.Models.Book;
import com.example.studytrackerapp.Models.Category;
import com.example.studytrackerapp.apdapters.BookAdapter;
import com.example.studytrackerapp.apdapters.CategoryAdapter;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;
import com.example.studytrackerapp.ui.theme.CartActivity;

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
    private List<Book> allBooks = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Author> authors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String fullName = prefs.getString("fullName", "User");
            tvWelcome.setText("Hello, " + fullName);
            tvWelcome.setVisibility(View.VISIBLE);
        } else {
            tvWelcome.setVisibility(View.GONE);
        }
        initViews();
        setupSearch();

        loadBooks();
        loadCategories();
        loadAuthors();

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            loadBooks();
            loadCategories();
            loadAuthors();
        }, 300);
    }

    private void initViews() {
        rvBooks = findViewById(R.id.rvBooks);
        rvCategories = findViewById(R.id.rvCategories);
        searchView = findViewById(R.id.searchView);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, new ArrayList<>(), book -> {
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("title", book.title);
            intent.putExtra("imageUrl", book.imageUrl);
            intent.putExtra("price", book.price);
            intent.putExtra("description", book.description);
            intent.putExtra("stock", book.stock);
            intent.putExtra("categoryName", getCategoryNameById(book.categoryId));
            startActivity(intent);
        });
        rvBooks.setAdapter(bookAdapter);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(this, categories, this::onCategoryClick);
        rvCategories.setAdapter(categoryAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
//
//        MenuItem loginItem = menu.findItem(R.id.menu_login);
//        MenuItem logoutItem = menu.findItem(R.id.menu_logout);
//
//        if (isLoggedIn) {
//            String fullName = prefs.getString("fullName", "User");
//            logoutItem.setTitle("Hello, " + fullName);
//            logoutItem.setVisible(true);
//            loginItem.setVisible(false);
//        } else {
//            logoutItem.setVisible(false);
//            loginItem.setVisible(true);
//        }
//
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; // KHÔNG xử lý login/logout ở đây
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu(); // ⚠️ Bắt hệ thống gọi lại onCreateOptionsMenu
        if (getIntent().getBooleanExtra("refresh", false)) {
            loadBooks(); // gọi lại API để cập nhật số lượng sách
            getIntent().removeExtra("refresh"); // tránh load lại nếu người dùng quay lại lần nữa
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE); // ✅ dùng đúng key
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        MenuItem loginItem = menu.findItem(R.id.menu_login);
        MenuItem logoutItem = menu.findItem(R.id.menu_logout);

        if (isLoggedIn) {
            String name = prefs.getString("username", "User");
            logoutItem.setVisible(true);
            logoutItem.setTitle("Hello, " + name); // ✅ hiện tên user ở đây
            loginItem.setVisible(false);
            Log.d("DEBUG_MENU", "isLoggedIn: " + isLoggedIn);
            Log.d("DEBUG_MENU", "fullName: " + name);

        } else {
            logoutItem.setVisible(false);
            loginItem.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_cart) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // Đã đăng nhập → mở giỏ hàng
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            } else {
                // Chưa đăng nhập → chuyển sang trang Login
                Intent loginIntent = new Intent(this, LoginMainActivity.class);
                startActivity(loginIntent);
            }

            return true;
        }
        else if (id == R.id.menu_login) {
            startActivity(new Intent(this, LoginMainActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            // Clear session
            SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            // Ẩn TextView Hello
            TextView tvWelcome = findViewById(R.id.tvWelcome);
            tvWelcome.setVisibility(View.GONE);

            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu(); // Gọi lại menu để cập nhật
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadBooks() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBooks = response.body();
                    bookAdapter.setBooks(allBooks);
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
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API khi tải category: " + t.toString());
            }
        });
    }

    private void loadAuthors() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllAuthors().enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    authors = response.body();
                    Log.d("API", "Tải danh sách author thành công: " + authors.size());
                } else {
                    Log.e("API", "Lỗi tải author: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối khi tải author: " + t.getMessage());
            }
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    bookAdapter.setBooks(allBooks);
                } else {
                    searchBooks(newText);
                }
                return true;
            }
        });
    }

    private void searchBooks(String keyword) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchBooks(keyword).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookAdapter.setBooks(response.body());
                } else {
                    bookAdapter.setBooks(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                bookAdapter.setBooks(new ArrayList<>());
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

    private void getBooksByCategory(int categoryId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getBooksByCategory(categoryId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookAdapter.setBooks(response.body());
                } else {
                    bookAdapter.setBooks(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                bookAdapter.setBooks(new ArrayList<>());
            }
        });
    }

    public String getCategoryNameById(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) return category.getName();
        }
        return "Không rõ";
    }

    public String getAuthorNameById(int authorId) {
        for (Author author : authors) {
            if (author.authorId == authorId) return author.name;
        }
        return "Không rõ tác giả";
    }
}
