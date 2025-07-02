package com.example.studytrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studytrackerapp.Models.UserRegister;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteMainActivity extends AppCompatActivity {
    private EditText etFullName, etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registe);
        TextView tvMessage = findViewById(R.id.tvMessage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegiste = findViewById(R.id.btnRegiste);

        // Sự kiện đăng ký
        btnRegiste.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            // Kiểm tra đầu vào
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty()) {
                showMessage(tvMessage, "Vui lòng nhập đầy đủ thông tin");

                return;
            }

            if (!password.equals(confirmPassword)) {
                showMessage(tvMessage, "Mật khẩu xác nhận không khớp");
                return;
            }

            // Tạo đối tượng gửi lên server
            UserRegister user = new UserRegister(fullName, email, password, confirmPassword, username);

            // Gọi API
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.registerUser(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(RegisteMainActivity.this, LoginMainActivity.class);
                        intent.putExtra("register_success", "Đăng ký thành công !!!");
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String error = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                            showMessage(tvMessage, "Đăng ký thất bại");
                        } catch (Exception e) {
                            showMessage(tvMessage, "Đăng ký thất bại");
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisteMainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // Gợi ý chuyển sang màn Login
        TextView tvLoginHint = findViewById(R.id.tvLoginHint);
        tvLoginHint.setOnClickListener(v -> {
            Intent intent = new Intent(RegisteMainActivity.this, LoginMainActivity.class);
            startActivity(intent);
            finish();
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


}
