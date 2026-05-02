package com.novelreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.novelreader.model.Novel;
import com.novelreader.model.BookSource;
import com.novelreader.tts.TTSManager;
import com.novelreader.source.BookSourceManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView novelRecyclerView;
    private EditText searchEditText;
    private Button searchButton, ttsButton, settingsButton;
    private NovelAdapter novelAdapter;
    private TTSManager ttsManager;
    private BookSourceManager sourceManager;
    private List<Novel> novelList;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeManagers();
        setupRecyclerView();
        setupClickListeners();
        loadSavedNovels();
    }

    private void initializeViews() {
        novelRecyclerView = findViewById(R.id.novelRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        ttsButton = findViewById(R.id.ttsButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void initializeManagers() {
        ttsManager = new TTSManager(this);
        sourceManager = new BookSourceManager(this);
        preferences = getSharedPreferences("novel_reader", Context.MODE_PRIVATE);
        novelList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        novelAdapter = new NovelAdapter(novelList, this::onNovelClick);
        novelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        novelRecyclerView.setAdapter(novelAdapter);
    }

    private void setupClickListeners() {
        searchButton.setOnClickListener(v -> performSearch());
        ttsButton.setOnClickListener(v -> toggleTTS());
        settingsButton.setOnClickListener(v -> openSettings());
    }

    private void onNovelClick(Novel novel) {
        // 打开小说详情页面
        NovelDetailActivity.start(this, novel);
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            sourceManager.searchNovels(query, this::onSearchResults);
        }
    }

    private void onSearchResults(List<Novel> results) {
        novelList.clear();
        novelList.addAll(results);
        novelAdapter.notifyDataSetChanged();
    }

    private void toggleTTS() {
        if (ttsManager.isPlaying()) {
            ttsManager.stop();
            ttsButton.setText("开始朗读");
        } else {
            // 获取当前选中的小说内容
            ttsManager.speak("开始朗读小说内容");
            ttsButton.setText("停止朗读");
        }
    }

    private void openSettings() {
        SettingsActivity.start(this);
    }

    private void loadSavedNovels() {
        // 从本地存储加载已保存的小说
        // 这里可以添加数据库操作
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutdown();
        }
    }
}