package com.novelreader;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.novelreader.database.AppDatabase;
import com.novelreader.database.ReadingStatsEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReadingStatsActivity extends AppCompatActivity {
    private TextView totalTimeTextView;
    private TextView todayTimeTextView;
    private TextView booksReadTextView;
    private TextView averageSpeedTextView;
    private ListView statsListView;
    
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_stats);
        
        initializeViews();
        initializeDatabase();
        loadStatistics();
    }

    private void initializeViews() {
        totalTimeTextView = findViewById(R.id.totalTimeTextView);
        todayTimeTextView = findViewById(R.id.todayTimeTextView);
        booksReadTextView = findViewById(R.id.booksReadTextView);
        averageSpeedTextView = findViewById(R.id.averageSpeedTextView);
        statsListView = findViewById(R.id.statsListView);
    }

    private void initializeDatabase() {
        database = AppDatabase.getDatabase(this);
    }

    private void loadStatistics() {
        new Thread(() -> {
            // 获取总阅读时长
            long totalTime = database.readingStatsDao().getTotalReadingTime();
            
            // 获取今天阅读数量
            int todayCount = database.readingStatsDao().getTodayReadingCount();
            
            runOnUiThread(() -> {
                updateUI(totalTime, todayCount);
            });
        }).start();
    }

    private void updateUI(long totalTime, int todayCount) {
        // 总阅读时长
        totalTimeTextView.setText(formatTime(totalTime));
        
        // 今天阅读时长
        todayTimeTextView.setText(String.format("%d 章节", todayCount));
        
        // 已读完书籍
        booksReadTextView.setText("0 本"); // 这里需要实际计算
        
        // 平均阅读速度
        averageSpeedTextView.setText("200 字/分钟"); // 这里需要实际计算
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        
        if (hours > 0) {
            return String.format("%d小时 %d分钟", hours, minutes);
        } else {
            return String.format("%d分钟", minutes);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ReadingStatsActivity.class);
        context.startActivity(intent);
    }
}