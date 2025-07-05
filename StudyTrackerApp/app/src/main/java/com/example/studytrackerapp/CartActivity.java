package com.example.studytrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.snackbar.Snackbar;

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
    private Button btnCheckout;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private EditText etAddress;
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

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);
        etAddress = findViewById(R.id.etAddress);
        rgPayment = findViewById(R.id.rgPayment);
        rbCash = findViewById(R.id.rbCash);
        rbQR = findViewById(R.id.rbQR);

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        cartItems = CartManager.getInstance().getCartItems();
        adapter = new CartAdapter(this, cartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateTotalPrice();

        // ✅ Xử lý thanh toán khi nhấn nút
        btnCheckout.setOnClickListener(v -> {
            // Lấy địa chỉ
            String address = etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), "Vui lòng nhập địa chỉ giao hàng", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra phương thức thanh toán
            int selectedPaymentId = rgPayment.getCheckedRadioButtonId();
            if (selectedPaymentId == -1) {
                Snackbar.make(findViewById(R.id.main), "Vui lòng chọn phương thức thanh toán", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Chỉ xử lý tiền mặt
            if (selectedPaymentId != R.id.rbCash) {
                Snackbar.make(findViewById(R.id.main), "Chức năng QR sẽ cập nhật sau", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Lấy userId từ SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            if (userId == -1) {
                Snackbar.make(findViewById(R.id.main), "Vui lòng đăng nhập trước khi thanh toán", Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Tạo danh sách OrderItemCreate
            List<OrderItemCreate> orderItems = new ArrayList<>();
            for (CartItem item : cartItems) {
                orderItems.add(new OrderItemCreate(item.getBookId(), item.getQuantity()));
            }

            // Gửi request lên API
            OrderCreate order = new OrderCreate(userId, "Đang xử lý + Chưa thanh toán",address ,orderItems);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            // 🧠 Thêm đoạn LOG để kiểm tra dữ liệu chuẩn bị gửi lên
            Log.d("OrderDebug", "userId: " + userId);
            Log.d("OrderDebug", "address: " + address);
            Log.d("OrderDebug", "items count: " + orderItems.size());
            Call<OrderResponse> call = apiService.createOrder(order);
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Snackbar.make(CartActivity.this.findViewById(android.R.id.content), "Đặt hàng thành công!", Snackbar.LENGTH_SHORT).show();
                        new android.os.Handler().postDelayed(() -> {
                            CartManager.getInstance().clearCart();
                            Intent intent = new Intent(CartActivity.this, MainActivity.class);
                            intent.putExtra("refresh", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }, 1500);  // kết thúc CartActivity
                    }  else {
                        Snackbar.make(findViewById(R.id.main), "Lỗi khi đặt hàng", Snackbar.LENGTH_SHORT).show();
                        Log.e("CheckoutError", "Response code: " + response.code());

                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Snackbar.make(findViewById(R.id.main), "Lỗi mạng: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
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
}
