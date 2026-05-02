package com.novelreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.novelreader.R;
import com.novelreader.model.Novel;
import java.util.List;

public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelViewHolder> {
    private List<Novel> novelList;
    private OnNovelClickListener listener;

    public interface OnNovelClickListener {
        void onNovelClick(Novel novel);
    }

    public NovelAdapter(List<Novel> novelList, OnNovelClickListener listener) {
        this.novelList = novelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_novel, parent, false);
        return new NovelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder holder, int position) {
        Novel novel = novelList.get(position);
        holder.bind(novel, listener);
    }

    @Override
    public int getItemCount() {
        return novelList.size();
    }

    public void updateData(List<Novel> newData) {
        this.novelList = newData;
        notifyDataSetChanged();
    }

    static class NovelViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView descriptionTextView;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(Novel novel, OnNovelClickListener listener) {
            titleTextView.setText(novel.getTitle());
            authorTextView.setText(novel.getAuthor());
            descriptionTextView.setText(novel.getDescription());
            
            itemView.setOnClickListener(v -> listener.onNovelClick(novel));
        }
    }
}