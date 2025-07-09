package com.example.studytrackerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.OrderDTO;
import com.example.studytrackerapp.apdapters.OrderAdapter;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView rvOrders;
    OrderAdapter orderAdapter;
    List<OrderDTO> orders = new ArrayList<>();
    TextView tvEmpty;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ
        rvOrders = findViewById(R.id.rvOrders);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnBack = findViewById(R.id.btnBack);

        // Cấu hình RecyclerView
        orderAdapter = new OrderAdapter(this, orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(orderAdapter);

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            finish(); // Return to previous activity (UserProfileActivity)
        });

        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId != -1) {
            fetchOrders(userId);
        } else {
            tvEmpty.setText("Không tìm thấy người dùng.");
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void fetchOrders(int userId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<OrderDTO>> call = apiService.getOrdersByUser(userId);
        call.enqueue(new Callback<List<OrderDTO>>() {
            @Override
            public void onResponse(Call<List<OrderDTO>> call, Response<List<OrderDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orders.clear();
                    orders.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();

                    if (orders.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("OrderHistory", "Lỗi khi nhận dữ liệu: " + response.message());
                    tvEmpty.setText("Không thể tải đơn hàng.");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<OrderDTO>> call, Throwable t) {
                Log.e("OrderHistory", "Lỗi kết nối API: " + t.getMessage());
                tvEmpty.setText("Lỗi kết nối. Vui lòng thử lại.");
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}