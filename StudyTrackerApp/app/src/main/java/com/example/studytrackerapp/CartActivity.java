package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.CartItem;
import com.example.studytrackerapp.Models.CartManager;
import com.example.studytrackerapp.Models.OrderCreate;
import com.example.studytrackerapp.Models.OrderItemCreate;
import com.example.studytrackerapp.Models.OrderResponse;
import com.example.studytrackerapp.apdapters.CartAdapter;
import com.example.studytrackerapp.api.ApiClient;
import com.example.studytrackerapp.api.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTotalPrice;
    private MaterialButton btnCheckout, btnBack;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private TextInputEditText etAddress;
    private RadioGroup rgPayment;
    private RadioButton rbCash, rbQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        recyclerView = findViewById(R.id.recyclerCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        etAddress = findViewById(R.id.etAddress);
        rgPayment = findViewById(R.id.rgPayment);
        rbCash = findViewById(R.id.rbCash);
        rbQR = findViewById(R.id.rbQR);

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Initialize RecyclerView
        cartItems = CartManager.getInstance().getCartItems();
        adapter = new CartAdapter(this, cartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Update empty state visibility
        updateEmptyState();

        // Update total price
        updateTotalPrice();

        // Checkout button click listener
        btnCheckout.setOnClickListener(v -> {
            // Check if cart is empty
            if (cartItems.isEmpty()) {
                showSnack("Giỏ hàng trống, vui lòng thêm sản phẩm");
                return;
            }

            // Get address
            String address = etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), "Vui lòng nhập địa chỉ giao hàng", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Check payment method
            int selectedPaymentId = rgPayment.getCheckedRadioButtonId();
            if (selectedPaymentId == -1) {
                showSnack("Vui lòng chọn phương thức thanh toán");
                return;
            }

            // Only handle cash payment for now
            if (selectedPaymentId != R.id.rbCash) {
                showSnack("Chức năng QR sẽ cập nhật sau");
                return;
            }

            // Get userId from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            if (userId == -1) {
                showSnack("Vui lòng đăng nhập trước khi thanh toán");
                return;
            }

            // Create order items
            List<OrderItemCreate> orderItems = new ArrayList<>();
            for (CartItem item : cartItems) {
                orderItems.add(new OrderItemCreate(item.getBookId(), item.getQuantity()));
            }

            // Send order to API
            OrderCreate order = new OrderCreate(userId, "Đang xử lý + Chưa thanh toán", address, orderItems);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            Log.d("OrderDebug", "userId: " + userId);
            Log.d("OrderDebug", "address: " + address);
            Log.d("OrderDebug", "items count: " + orderItems.size());

            Call<OrderResponse> call = apiService.createOrder(order);
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        showSnack("Đặt hàng thành công!");
                        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                            CartManager.getInstance().clearCart();
                            Intent intent = new Intent(CartActivity.this, MainActivity.class);
                            intent.putExtra("refresh", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }, 1500);
                    } else {
                        showSnack("Lỗi khi đặt hàng: " + response.message());
                        Log.e("CheckoutError", "Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    showSnack("Lỗi mạng: " + t.getMessage());
                }
            });
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMaximumFractionDigits(0);
        tvTotalPrice.setText("Tổng: " + formatter.format(total) + " VNĐ");
    }

    private void updateEmptyState() {
        if (cartItems.isEmpty()) {
            findViewById(R.id.emptyState).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyState).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showSnack(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(com.google.android.material.R.color.material_deep_teal_500))
                .setTextColor(getResources().getColor(android.R.color.white))
                .show();
    }
}
