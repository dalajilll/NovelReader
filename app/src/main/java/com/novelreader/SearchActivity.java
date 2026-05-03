package com.novelreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.novelreader.model.Novel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private ProgressBar loadingProgressBar;
    private NovelAdapter searchResultsAdapter;
    
    private List<Novel> searchResults = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private BookSourceManager sourceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        initializeViews();
        setupSearchAdapter();
        setupSearchListeners();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        
        sourceManager = new BookSourceManager(this);
    }

    private void setupSearchAdapter() {
        searchResultsAdapter = new NovelAdapter(searchResults, this::onNovelClick);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
    }

    private void setupSearchListeners() {
        // 实时搜索
        searchEditText.addTextChangedListener(new TextWatcher() {
            private String lastQuery = "";
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.length() >= 2 && !query.equals(lastQuery)) {
                    lastQuery = query;
                    performSearch(query);
                }
            }
        });
        
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            }
        });
    }

    private void performSearch(String query) {
        showLoading(true);
        searchResults.clear();
        searchResultsAdapter.notifyDataSetChanged();
        
        sourceManager.searchNovels(query, results -> {
            runOnUiThread(() -> {
                showLoading(false);
                searchResults.clear();
                searchResults.addAll(results);
                searchResultsAdapter.notifyDataSetChanged();
                
                if (results.isEmpty()) {
                    Toast.makeText(this, "未找到相关结果", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void onNovelClick(Novel novel) {
        // 跳转到小说详细页面
        NovelDetailActivity.start(this, novel);
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        searchButton.setEnabled(!show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
