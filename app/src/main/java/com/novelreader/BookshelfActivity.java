package com.novelreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.novelreader.database.AppDatabase;
import com.novelreader.model.Novel;
import java.util.ArrayList;
import java.util.List;

public class BookshelfActivity extends AppCompatActivity {
    private RecyclerView bookshelfRecyclerView;
    private TabHost tabHost;
    private NovelAdapter allNovelsAdapter;
    private NovelAdapter favoriteNovelsAdapter;
    private NovelAdapter downloadedNovelsAdapter;
    
    private AppDatabase database;
    private List<AppDatabase.NovelEntity> allNovels = new ArrayList<>();
    private List<AppDatabase.NovelEntity> favoriteNovels = new ArrayList<>();
    private List<AppDatabase.NovelEntity> downloadedNovels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);
        
        initializeViews();
        initializeDatabase();
        setupTabHost();
        setupRecyclerViews();
        loadData();
    }

    private void initializeViews() {
        tabHost = findViewById(R.id.tabHost);
        bookshelfRecyclerView = findViewById(R.id.bookshelfRecyclerView);
        bookshelfRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(this);
    }

    private void setupTabHost() {
        tabHost.setup();
        
        // 全部书籍
        TabHost.TabSpec allTab = tabHost.newTabSpec("all");
        allTab.setIndicator("全部");
        allTab.setContent(R.id.allNovelsTab);
        tabHost.addTab(allTab);
        
        // 收藏书籍
        TabHost.TabSpec favoriteTab = tabHost.newTabSpec("favorite");
        favoriteTab.setIndicator("收藏");
        favoriteTab.setContent(R.id.favoriteNovelsTab);
        tabHost.addTab(favoriteTab);
        
        // 离线书籍
        TabHost.TabSpec downloadedTab = tabHost.newTabSpec("downloaded");
        downloadedTab.setIndicator("离线");
        downloadedTab.setContent(R.id.downloadedNovelsTab);
        tabHost.addTab(downloadedTab);
        
        tabHost.setOnTabChangedListener(this::onTabChanged);
    }

    private void setupRecyclerViews() {
        // 全部书籍
        allNovelsAdapter = new NovelAdapter(convertToNovelList(allNovels), this::onNovelClick);
        
        // 收藏书籍  
        favoriteNovelsAdapter = new NovelAdapter(convertToNovelList(favoriteNovels), this::onNovelClick);
        
        // 离线书籍
        downloadedNovelsAdapter = new NovelAdapter(convertToNovelList(downloadedNovels), this::onNovelClick);
    }

    private void loadData() {
        new Thread(() -> {
            allNovels = database.novelDao().getAllNovels();
            favoriteNovels = database.novelDao().getFavoriteNovels();
            downloadedNovels = database.novelDao().getDownloadedNovels();
            
            runOnUiThread(() -> {
                updateAdapters();
            });
        }).start();
    }

    private void updateAdapters() {
        allNovelsAdapter.updateData(convertToNovelList(allNovels));
        favoriteNovelsAdapter.updateData(convertToNovelList(favoriteNovels));
        downloadedNovelsAdapter.updateData(convertToNovelList(downloadedNovels));
    }

    private void onTabChanged(String tabId) {
        switch (tabId) {
            case "all":
                bookshelfRecyclerView.setAdapter(allNovelsAdapter);
                break;
            case "favorite":
                bookshelfRecyclerView.setAdapter(favoriteNovelsAdapter);
                break;
            case "downloaded":
                bookshelfRecyclerView.setAdapter(downloadedNovelsAdapter);
                break;
        }
    }

    private void onNovelClick(Novel novel) {
        // 跳转到阅读页面
        NovelDetailActivity.start(this, novel);
    }

    private List<Novel> convertToNovelList(List<AppDatabase.NovelEntity> entities) {
        List<Novel> novels = new ArrayList<>();
        for (AppDatabase.NovelEntity entity : entities) {
            Novel novel = new Novel();
            novel.setId(entity.id);
            novel.setTitle(entity.title);
            novel.setAuthor(entity.author);
            novel.setDescription(entity.description);
            novel.setCurrentChapter(entity.currentChapter);
            novels.add(novel);
        }
        return novels;
    }

    // 书架管理操作
    public void onNovelLongClick(Novel novel) {
        showNovelOptionsDialog(novel);
    }

    private void showNovelOptionsDialog(Novel novel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] options = {"收藏/取消收藏", "下载全文", "删除", "更多信息"};
        
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    toggleFavorite(novel);
                    break;
                case 1:
                    downloadNovel(novel);
                    break;
                case 2:
                    deleteNovel(novel);
                    break;
                case 3:
                    showNovelInfo(novel);
                    break;
            }
        });
        
        builder.show();
    }

    private void toggleFavorite(Novel novel) {
        new Thread(() -> {
            // 切换收藏状态
            // database.novelDao().toggleFavorite(novel.getId());
            runOnUiThread(this::loadData);
        }).start();
    }

    private void downloadNovel(Novel novel) {
        // 开始下载小说
        Toast.makeText(this, "开始下载: " + novel.getTitle(), Toast.LENGTH_SHORT).show();
        // 这里调用下载逻辑
    }

    private void deleteNovel(Novel novel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除小说")
                .setMessage("确定要删除这本小说吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    new Thread(() -> {
                        // database.novelDao().deleteNovel(novelEntity);
                        runOnUiThread(this::loadData);
                    }).start();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showNovelInfo(Novel novel) {
        // 显示小说详细信息
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(novel.getTitle())
                .setMessage("作者: " + novel.getAuthor() + "\n" +
                        "简介: " + novel.getDescription())
                .setPositiveButton("确定", null)
                .show();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BookshelfActivity.class);
        context.startActivity(intent);
    }
}
