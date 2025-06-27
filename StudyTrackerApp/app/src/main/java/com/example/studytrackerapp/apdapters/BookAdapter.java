package com.example.studytrackerapp.apdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studytrackerapp.BookDetailActivity;
import com.example.studytrackerapp.MainActivity;
import com.example.studytrackerapp.R;
import com.example.studytrackerapp.Models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private Context context;
    private OnBookClickListener listener;

    // ✅ Interface xử lý sự kiện khi click nút Chi tiết
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // ✅ Constructor
    public BookAdapter(Context context, List<Book> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    // ✅ Cập nhật danh sách sách
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
        holder.tvPrice.setText(String.format("%.0f VNĐ", book.price));

        // Xử lý hình ảnh
        String imageName = book.imageUrl.replace(".png", "").toLowerCase();
        int imageResId = context.getResources().getIdentifier(
                imageName,
                "drawable",
                context.getPackageName()
        );
        holder.ivCover.setImageResource(imageResId != 0 ? imageResId : R.drawable.default_image);

        // Xử lý nút "Chi tiết"
        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("title", book.title);
            intent.putExtra("price", book.price);
            intent.putExtra("description", book.description);
            intent.putExtra("imageUrl", book.imageUrl);
            intent.putExtra("stock", book.stock != null ? book.stock : 0);

            // Lấy tên thể loại và tác giả từ MainActivity (ép kiểu context)
            if (context instanceof MainActivity) {
                MainActivity main = (MainActivity) context;
                intent.putExtra("categoryName", main.getCategoryNameById(book.categoryId));
                intent.putExtra("authorName", main.getAuthorNameById(book.authorId));
            } else {
                intent.putExtra("categoryName", "Không rõ");
                intent.putExtra("authorName", "Không rõ");
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    // ✅ ViewHolder class
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvPrice;
        Button btnDetail;

        public BookViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.imgBookCover);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvBookPrice);
            btnDetail = itemView.findViewById(R.id.btnDetail); // ánh xạ nút
        }
    }
}
