package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfileActivity extends AppCompatActivity {
    TextView tvFullName, tvUsername, tvEmail, tvRole;
    Button btnViewOrders, btnUpdateProfile, btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnBack = findViewById(R.id.btnBack);


        // Lấy thông tin từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String fullName = prefs.getString("fullName", "Chưa rõ");
        String username = prefs.getString("username", "User rõ");
        String email = prefs.getString("email", "Chưa rõ");
        String role = prefs.getString("role", "Khách Hàng");

        // Hiển thị lên UI
        tvFullName.setText("Họ tên: " + fullName);
        tvUsername.setText("Tên đăng nhập: " + username);
        tvEmail.setText("Email: " + email);
        tvRole.setText("Vai trò: " + role);

        btnViewOrders.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, OrderHistoryActivity.class));
        });

        btnUpdateProfile.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, UpdateProfileActivity.class));
        });

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish(); // Optional: Close UserProfileActivity to prevent stacking
        });
    }
}