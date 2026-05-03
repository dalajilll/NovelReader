package com.novelreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import com.novelreader.model.Novel;
import com.novelreader.tts.TTSManager;
import java.util.ArrayList;
import java.util.List;

public class NovelDetailActivity extends AppCompatActivity {
    private TextView titleTextView, contentTextView, progressTextView;
    private LinearLayout menuLayout;
    private SeekBar fontSizeSeekBar, brightnessSeekBar;
    private Button prevChapterBtn, nextChapterBtn, menuBtn, ttsBtn;
    private GestureDetectorCompat gestureDetector;
    private TTSManager ttsManager;
    private SharedPreferences preferences;
    
    private Novel currentNovel;
    private List<String> chapters;
    private int currentChapterIndex = 0;
    private boolean isMenuVisible = false;
    private boolean isAutoScrolling = false;
    
    // 阅读设置
    private float fontSize = 18f;
    private int backgroundColor = Color.WHITE;
    private int textColor = Color.BLACK;
    private float lineSpacing = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);
        
        initializeViews();
        initializeSettings();
        setupGestureDetector();
        setupTTS();
        loadNovelData();
        setupClickListeners();
        updateDisplay();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        progressTextView = findViewById(R.id.progressTextView);
        menuLayout = findViewById(R.id.menuLayout);
        fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        prevChapterBtn = findViewById(R.id.prevChapterBtn);
        nextChapterBtn = findViewById(R.id.nextChapterBtn);
        menuBtn = findViewById(R.id.menuBtn);
        ttsBtn = findViewById(R.id.ttsBtn);
    }

    private void initializeSettings() {
        preferences = getSharedPreferences("reading_settings", Context.MODE_PRIVATE);
        fontSize = preferences.getFloat("font_size", 18f);
        backgroundColor = preferences.getInt("background_color", Color.WHITE);
        textColor = preferences.getInt("text_color", Color.BLACK);
        lineSpacing = preferences.getFloat("line_spacing", 1.5f);
        
        fontSizeSeekBar.setProgress((int) (fontSize - 12)); // 12-32
        brightnessSeekBar.setProgress(80); // 默认亮度
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                handleScreenTap(e);
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                increaseFontSize();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        previousPage();
                    } else {
                        nextPage();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setupTTS() {
        ttsManager = new TTSManager(this);
    }

    private void loadNovelData() {
        Intent intent = getIntent();
        if (intent != null) {
            currentNovel = (Novel) intent.getSerializableExtra("novel");
            if (currentNovel != null) {
                generateMockChapters();
            }
        }
    }

    private void generateMockChapters() {
        chapters = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String chapterContent = generateChapterContent(i);
            chapters.add(chapterContent);
        }
    }

    private String generateChapterContent(int chapterNum) {
        return String.format("第%d章\n\n这是第%d章的内容。"
                + "\n\n我们在这里放入了很长的文本内容，"
                + "用于测试阅读效果。这些文字会"
                + "在页面上占据大量的空间，让我们"
                + "可以测试分页、滚动和字体调节"
                + "等功能。\n\n每一行都有不同的行长，"
                + "这样可以更好地测试自动排版的"
                + "效果。\n\n请您慢慢阅读，感受这个故事...",
                chapterNum, chapterNum);
    }

    private void setupClickListeners() {
        menuBtn.setOnClickListener(v -> toggleMenu());
        prevChapterBtn.setOnClickListener(v -> previousChapter());
        nextChapterBtn.setOnClickListener(v -> nextChapter());
        ttsBtn.setOnClickListener(v -> toggleTTS());
        findViewById(R.id.settingsBtn).setOnClickListener(v -> showSettingsDialog());
        findViewById(R.id.chapterListBtn).setOnClickListener(v -> showChapterList());
        findViewById(R.id.bookmarkBtn).setOnClickListener(v -> addBookmark());

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontSize = 12 + progress;
                updateFontSize();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustBrightness(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void handleScreenTap(MotionEvent e) {
        float x = e.getX();
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        
        if (x < screenWidth * 0.3) {
            previousPage();
        } else if (x > screenWidth * 0.7) {
            nextPage();
        } else {
            toggleMenu();
        }
    }

    private void toggleMenu() {
        isMenuVisible = !isMenuVisible;
        menuLayout.setVisibility(isMenuVisible ? View.VISIBLE : View.GONE);
    }

    private void previousPage() {
        Toast.makeText(this, "上一页", Toast.LENGTH_SHORT).show();
    }

    private void nextPage() {
        Toast.makeText(this, "下一页", Toast.LENGTH_SHORT).show();
    }

    private void previousChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
            updateDisplay();
        }
    }

    private void nextChapter() {
        if (currentChapterIndex < chapters.size() - 1) {
            currentChapterIndex++;
            updateDisplay();
        }
    }

    private void toggleTTS() {
        if (ttsManager.isPlaying()) {
            ttsManager.stop();
            ttsBtn.setText("开始朗读");
        } else {
            String content = contentTextView.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                ttsManager.speak(content);
                ttsBtn.setText("停止朗读");
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reading_settings, null);
        
        RadioGroup themeGroup = dialogView.findViewById(R.id.themeGroup);
        SeekBar lineSpacingSeekBar = dialogView.findViewById(R.id.lineSpacingSeekBar);

        // 重点修复：把switch换成if判断，避开R.id常量报错
        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.dayModeRadio){
                setTheme(Color.WHITE, Color.BLACK);
            }else if(checkedId == R.id.nightModeRadio){
                setTheme(Color.BLACK, Color.WHITE);
            }else if(checkedId == R.id.eyeModeRadio){
                setTheme(0xFFF5F5DC, Color.BLACK);
            }
        });

        lineSpacingSeekBar.setProgress((int) ((lineSpacing - 1.0f) * 10));
        lineSpacingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lineSpacing = 1.0f + progress * 0.1f;
                updateLineSpacing();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        builder.setView(dialogView)
                .setTitle("阅读设置")
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .show();
    }

    private void showChapterList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("目录");
        
        String[] chapterTitles = new String[chapters.size()];
        for (int i = 0; i < chapters.size(); i++) {
            chapterTitles[i] = "第" + (i + 1) + "章";
        }
        
        builder.setItems(chapterTitles, (dialog, which) -> {
            currentChapterIndex = which;
            updateDisplay();
        });
        
        builder.show();
    }

    private void addBookmark() {
        Toast.makeText(this, "已添加书签", Toast.LENGTH_SHORT).show();
    }

    private void increaseFontSize() {
        fontSize = Math.min(fontSize + 2, 32);
        fontSizeSeekBar.setProgress((int) (fontSize - 12));
        updateFontSize();
    }

    private void setTheme(int bgColor, int txtColor) {
        backgroundColor = bgColor;
        textColor = txtColor;
        updateTheme();
    }

    private void updateDisplay() {
        if (chapters != null && !chapters.isEmpty()) {
            titleTextView.setText("第" + (currentChapterIndex + 1) + "章");
            contentTextView.setText(chapters.get(currentChapterIndex));
            progressTextView.setText(String.format("%d/%d", currentChapterIndex + 1, chapters.size()));
            
            prevChapterBtn.setEnabled(currentChapterIndex > 0);
            nextChapterBtn.setEnabled(currentChapterIndex < chapters.size() - 1);
        }
    }

    private void updateFontSize() {
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        saveSettings();
    }

    private void updateLineSpacing() {
        contentTextView.setLineSpacing(0, lineSpacing);
        saveSettings();
    }

    private void updateTheme() {
        contentTextView.setBackgroundColor(backgroundColor);
        contentTextView.setTextColor(textColor);
        saveSettings();
    }

    private void adjustBrightness(int progress) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = progress / 100.0f;
        getWindow().setAttributes(layoutParams);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("font_size", fontSize);
        editor.putInt("background_color", backgroundColor);
        editor.putInt("text_color", textColor);
        editor.putFloat("line_spacing", lineSpacing);
        editor.apply();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutdown();
        }
        saveSettings();
    }

    public static void start(Context context, Novel novel) {
        Intent intent = new Intent(context, NovelDetailActivity.class);
        intent.putExtra("novel", novel);
        context.startActivity(intent);
    }
}
