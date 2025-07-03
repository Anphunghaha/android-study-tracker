package com.example.studytrackerapp.apdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.CartItem;
import com.example.studytrackerapp.Models.CartManager;
import com.example.studytrackerapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public void setCartItems(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText(formatCurrency(item.getPrice()) + " VNĐ");
        holder.etQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvTotal.setText("Thành tiền: " + formatCurrency(item.getPrice() * item.getQuantity()) + " VNĐ");

        // Sửa số lượng (và giới hạn không vượt stock nếu bạn có dữ liệu đó)
        holder.etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int newQty = Integer.parseInt(holder.etQuantity.getText().toString());
                    if (newQty < 1) newQty = 1;
                    if (newQty > item.getStock()) newQty = item.getStock(); // nếu có stock
                    item.setQuantity(newQty);
                    CartManager.getInstance().updateItem(item); // ✅ Thêm dòng này
                    notifyItemChanged(holder.getAdapterPosition());
                } catch (NumberFormatException e) {
                    holder.etQuantity.setText(String.valueOf(item.getQuantity()));
                }
            }
        });

        // Xóa item khỏi cart
        holder.btnRemove.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            cartItems.remove(adapterPosition);
            CartManager.getInstance().removeFromCart(item.getBookId()); // ✅ Thay cách cũ
            notifyItemRemoved(adapterPosition);
        });
        // Gán ảnh minh họa (nếu có)
        String imageUrl = item.getImageUrl();
        int imageResId = 0;

        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageResId = context.getResources().getIdentifier(
                    imageUrl.replace(".png", "").toLowerCase(),
                    "drawable",
                    context.getPackageName()
            );
        }
        holder.ivCover.setImageResource(imageResId != 0 ? imageResId : R.drawable.placeholder_book);
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvTotal;

        EditText etQuantity;
        ImageView ivCover;
        Button btnRemove;

        public CartViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCartBookName);
            etQuantity = itemView.findViewById(R.id.etCartQuantity);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvTotal = itemView.findViewById(R.id.tvCartTotal);
            // Nếu bạn có ImageView trong item_cart.xml, thì mới cần dòng này:
            ivCover = itemView.findViewById(R.id.imgCartBook); // Nếu không có thì có thể xóa dòng này
            btnRemove = itemView.findViewById(R.id.btnRemove);

        }
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setGroupingUsed(true);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }
}
