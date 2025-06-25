package com.example.studytrackerapp.apdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.R;
import com.example.studytrackerapp.Models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private Context context;

    // ✅ Constructor nhận Context
    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    // ✅ Cập nhật lại danh sách khi cần (ví dụ: sau tìm kiếm)
    public void setBooks(List<Book> books) {
        this.bookList = books;
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvTitle.setText(book.title);
        holder.tvPrice.setText(book.price + " VNĐ");
        // Xử lý tên ảnh - bỏ phần mở rộng .png
        String imageName = book.imageUrl.replace(".png", "").toLowerCase();

        // ✅ Lấy ảnh từ tên imageName trong drawable
        int imageResId = context.getResources().getIdentifier(
                imageName, // ví dụ: "book_csharp"
                "drawable",
                context.getPackageName()
        );

        if (imageResId != 0) {
            holder.ivCover.setImageResource(imageResId);
        } else {
            holder.ivCover.setImageResource(R.drawable.default_image); // ảnh mặc định nếu không tìm thấy
        }
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    // ✅ ViewHolder class
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvPrice;

        public BookViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.imgBookCover);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvBookPrice);
        }
    }
}
