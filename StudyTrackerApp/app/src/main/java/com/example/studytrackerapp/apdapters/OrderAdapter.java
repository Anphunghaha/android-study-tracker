package com.example.studytrackerapp.apdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.Models.OrderDTO;
import com.example.studytrackerapp.Models.OrderItemDTO;
import com.example.studytrackerapp.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<OrderDTO> orderList;

    public OrderAdapter(Context context, List<OrderDTO> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDTO order = orderList.get(position);
        holder.tvOrderId.setText("Mã đơn: #" + order.orderId);
        holder.tvOrderDate.setText("Ngày: " + order.orderDate);
        holder.tvTotal.setText("Tổng tiền: " + order.totalAmount + "đ");

        StringBuilder itemDetails = new StringBuilder();
        for (OrderItemDTO item : order.items) {
            itemDetails.append("• ").append(item.bookTitle)
                    .append(" x").append(item.quantity)
                    .append(" - ").append(item.unitPrice).append("đ\n");
        }
        holder.tvItems.setText(itemDetails.toString().trim());

        // ✅ Xử lý ảnh: lấy ảnh của cuốn sách đầu tiên trong đơn hàng
        if (!order.items.isEmpty()) {
            OrderItemDTO firstItem = order.items.get(0);
            if (firstItem.imgUrl != null) {
                String imageName = firstItem.imgUrl.replace(".png", "").toLowerCase();
                int imageResId = context.getResources().getIdentifier(
                        imageName,
                        "drawable",
                        context.getPackageName()
                );

                //thêm log để check
                Log.d("OrderAdapter", "imgUrl = " + firstItem.imgUrl);
                Log.d("OrderAdapter", "imageName = " + imageName);
                Log.d("OrderAdapter", "imageResId = " + imageResId);

                holder.imgBook.setImageResource(imageResId != 0 ? imageResId : R.drawable.default_image);
            } else {
                holder.imgBook.setImageResource(R.drawable.default_image);
            }
        } else {
            holder.imgBook.setImageResource(R.drawable.default_image);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotal, tvItems;
        ImageView imgBook;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvItems = itemView.findViewById(R.id.tvItems);
            imgBook = itemView.findViewById(R.id.imgBook);
        }
    }
}
