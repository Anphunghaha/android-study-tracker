package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studytrackerapp.Models.UserLogin;
import com.example.studytrackerapp.Models.UserResponse;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMainActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tvLoginMessage = findViewById(R.id.tvLoginMessage);
        TextView tvLoginMessage1 = findViewById(R.id.tvLoginMessage1);

        // Hiển thị thông báo nếu có
        String message = getIntent().getStringExtra("register_success");
        if (message != null) {
            showMessage(tvLoginMessage, message);
        }
        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Back icon click → MainActivity
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Find views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // "Đăng ký" click → RegisterActivity
        TextView tvRegister = findViewById(R.id.tvRegisterHint);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginMainActivity.this, RegisteMainActivity.class);
            startActivity(intent);
        });

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showMessage(tvLoginMessage1, "Vui lòng nhập email và mật khẩu");
                return;
            }

            loginUser(email, password);
        });

    }
    private void showMessage(TextView textView, String message) {
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);

        // Tự động ẩn sau 10 giây (10000 milliseconds)
        new android.os.Handler().postDelayed(() -> {
            textView.setVisibility(View.GONE);
        }, 5000);
    }
    private void loginUser(String email, String password) {
        UserLogin login = new UserLogin(email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<UserResponse> call = apiService.login(login);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                TextView tvLoginMessage1 = findViewById(R.id.tvLoginMessage1);
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    // Lưu thông tin user
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("userId", user.getUserId());
                    editor.putString("username", user.getUsername());
                    editor.putString("fullName", user.getFullName());
                    editor.putString("email", user.getEmail());
                    editor.putString("role", user.getRole());
                    editor.apply();

                    Toast.makeText(LoginMainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Đóng LoginMainActivity
                } else {
                    showMessage(tvLoginMessage1, "Vui lòng nhập email và mật khẩu");

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginMainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
