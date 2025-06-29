package com.example.studytrackerapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Locale;
import android.view.MenuItem;
import androidx.annotation.NonNull;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvStock, tvCategory, tvAuthor;
    private ImageView imgCover;
    private TextView tvTitle, tvPrice, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiện nút back
        // Khởi tạo view
        initViews();

        // Nhận và hiển thị dữ liệu
        displayData();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Xử lý khi người dùng nhấn nút back trên Toolbar
            finish(); // Trở về màn hình trước
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initViews() {
        imgCover = findViewById(R.id.imgDetailCover);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvStock = findViewById(R.id.tvDetailStock);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvAuthor = findViewById(R.id.tvDetailAuthor);
    }

    private void displayData() {
        // Nhận dữ liệu từ Intent
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String description = getIntent().getStringExtra("description");
        int stock = getIntent().getIntExtra("stock", 0);
        String categoryName = getIntent().getStringExtra("categoryName");
        String authorName = getIntent().getStringExtra("authorName");

        // Hiển thị dữ liệu đơn giản trước
        tvTitle.setText(title);
        tvPrice.setText(formatCurrency(price) + " VNĐ");
        tvDescription.setText(description);
        tvStock.setText("Kho: " + stock);
        tvCategory.setText("Thể loại: " + categoryName);
        tvAuthor.setText("Tác giả: " + authorName);

        // Tải ảnh trong background
        loadImageAsync(imageUrl);
    }

    private void loadImageAsync(String imageUrl) {
        new Thread(() -> {
            // Xử lý tìm ảnh trong background thread
            String imageName = imageUrl.replace(".png", "").toLowerCase();
            final int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            // Quay lại main thread để cập nhật UI
            runOnUiThread(() -> {
                if (imageResId != 0) {
                    imgCover.setImageResource(imageResId);
                } else {
                    imgCover.setImageResource(R.drawable.default_image);
                }
            });
        }).start();
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setGroupingUsed(true);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }
}