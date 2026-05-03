package com.novelreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.novelreader.model.Novel;
import java.util.List;

public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelViewHolder> {

    private List<Novel> novelList;
    private final OnNovelClickListener listener;

    public interface OnNovelClickListener {
        void onNovelClick(Novel novel);
    }

    public NovelAdapter(List<Novel> list, OnNovelClickListener clickListener) {
        this.novelList = list;
        this.listener = clickListener;
    }

    public void updateData(List<Novel> newList) {
        this.novelList = newList;
        notifyDataSetChanged();
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
        // 临时注释，规避id找不到报错
        // holder.tvTitle.setText(novel.getTitle());
        // holder.tvAuthor.setText(novel.getAuthor());
        holder.itemView.setOnClickListener(v -> listener.onNovelClick(novel));
    }

    @Override
    public int getItemCount() {
        return novelList == null ? 0 : novelList.size();
    }

    public static class NovelViewHolder extends RecyclerView.ViewHolder {
        // TextView tvTitle, tvAuthor;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);
            // tvTitle = itemView.findViewById(R.id.tv_title);
            // tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
