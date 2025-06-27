package com.example.studytrackerapp;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvStock, tvCategory, tvAuthor;
    private ImageView imgCover;
    private TextView tvTitle, tvPrice, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        imgCover = findViewById(R.id.imgDetailCover);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvStock = findViewById(R.id.tvDetailStock);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvAuthor = findViewById(R.id.tvDetailAuthor);
        // Nhận dữ liệu từ Intent
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String description = getIntent().getStringExtra("description");
        int stock = getIntent().getIntExtra("stock", 0);
        String categoryName = getIntent().getStringExtra("categoryName");
        String authorName = getIntent().getStringExtra("authorName");
        // Hiển thị
        tvTitle.setText(title);
        tvPrice.setText(String.format("%.0f VNĐ", price));
        tvDescription.setText(description);
        tvStock.setText("Kho: " + stock);
        tvCategory.setText("Thể loại: " + categoryName);
        tvAuthor.setText("Tác giả: " + authorName);
        tvAuthor.setText("Tác giả: " + authorName);
        // Lấy ảnh từ drawable theo tên
        String imageName = imageUrl.replace(".png", "").toLowerCase();
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageResId != 0) {
            imgCover.setImageResource(imageResId);
        } else {
            imgCover.setImageResource(R.drawable.default_image);
        }
    }
}
