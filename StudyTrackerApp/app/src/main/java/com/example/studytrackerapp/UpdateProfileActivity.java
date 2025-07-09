package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studytrackerapp.Models.UserUpdateDTO;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText edtUsername, edtFullName, edtCurrentPassword, edtNewPassword, edtRePassword;
    Button btnUpdate;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);

        // Nếu layout bạn chưa có id="main", hãy thêm vào ConstraintLayout/LinearLayout gốc
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUsername = findViewById(R.id.edtUsername);
        edtFullName = findViewById(R.id.edtFullName);
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        String currentUsername = prefs.getString("username", "");
        String currentFullName = prefs.getString("fullName", "");

        edtUsername.setText(currentUsername);
        edtFullName.setText(currentFullName);

        btnUpdate.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String username = edtUsername.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String rePassword = edtRePassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            showSnack("Vui lòng nhập mật khẩu hiện tại");
            return;
        }

        if (!newPassword.isEmpty() && !newPassword.equals(rePassword)) {
            showSnack("Mật khẩu mới không trùng khớp");
            return;
        }

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.userId = userId;
        dto.username = username;
        dto.fullName = fullName;
        dto.currentPassword = currentPassword;
        dto.newPassword = newPassword;

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.updateUser(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSnack("Cập nhật thành công");

                    // Cập nhật SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putString("fullName", fullName);
                    editor.apply();

                    // Đợi 1.5s rồi quay lại UserProfileActivity
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    showSnack("Lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showSnack("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showSnack(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG).show();
    }
}
